// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.videoplayer;

import static androidx.media3.common.Player.REPEAT_MODE_ALL;
import static androidx.media3.common.Player.REPEAT_MODE_OFF;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import androidx.media3.session.MediaSession;
import android.util.Log;
import android.view.Surface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.annotation.VisibleForTesting;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.common.Player.Listener;
import androidx.media3.common.VideoSize;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.datasource.HttpDataSource;
import androidx.media3.exoplayer.DefaultLoadControl;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import androidx.media3.ui.PlayerNotificationManager;
import io.flutter.plugin.common.EventChannel;
import io.flutter.view.TextureRegistry;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.InputStream;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

final class VideoPlayer {
  private static final String TAG = "VideoPlayer";

  private static final String FORMAT_SS = "ss";
  private static final String FORMAT_DASH = "dash";
  private static final String FORMAT_HLS = "hls";
  private static final String FORMAT_OTHER = "other";

  private static final int NOTIFICATION_ID = 2020101921;
  private static final String NOTIFICATION_CHANNEL = "SHIRASU_PLAYER_NOTIFICATION";

  /**
   * 現在の再生位置より前に保持しておくバッファの長さ (ms)。
   *
   * <p>ExoPlayer の既定値は 0 で、再生済みの区間は即座に破棄される。そのため 10 秒戻しのような
   * わずかな巻き戻しでもセグメントを取り直すことになり、毎回ローディングが挟まっていた
   * (AVPlayer は既定で再生済み区間を保持するため iOS では発生しない)。
   * 巻き戻しがバッファ内で完結するよう、直近 30 秒ぶんを残しておく。
   *
   * <p>バイト数の上限 ({@code targetBufferBytes}) は既定のまま (映像+音声で約 144MB) にしている。
   * ここを絞ると保持中の過去サンプルも同じ枠を食うため、バックバッファのぶんだけ先読みが
   * 削られて「10 秒スキップを数回連打するとバッファ外に出る」という別の待ちを生む。
   * 実際のバッファ量は時間ベースのしきい値 (先読み 50 秒 + バックバッファ 30 秒) で決まる。
   */
  private static final int BACK_BUFFER_DURATION_MS = 30_000;

  /**
   * シーク後・再生開始時に、再生を始めるために必要なバッファの長さ (ms)。
   *
   * <p>ExoPlayer の既定値は 2500ms。バッファ済みでないシーク先へ飛んだときはこの長さが
   * 貯まるまで待たされるので、体感待ち時間を減らすために短くしている。
   * HLS のセグメント長より短い値なので、実質「1 セグメント取得できたら再生する」になる。
   */
  private static final int BUFFER_FOR_PLAYBACK_MS = 1_000;

  /**
   * バッファ枯渇による停止から再生を再開するために必要なバッファの長さ (ms)。
   *
   * <p>ExoPlayer の既定値は 5000ms。シーク時には適用されない (シークは rebuffering 扱いでは
   * ないため) が、シーク直後に回線が細くて詰まった場合の復帰も遅いので合わせて短くする。
   */
  private static final int BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS = 3_000;

  public ExoPlayer exoPlayer;

  private Surface surface;

  private final TextureRegistry.SurfaceTextureEntry textureEntry;

  private QueuingEventSink eventSink;

  private final EventChannel eventChannel;

  private static final String USER_AGENT = "User-Agent";

  @VisibleForTesting
  boolean isInitialized = false;

  private final VideoPlayerOptions options;

  private final DefaultHttpDataSource.Factory httpDataSourceFactory;

  private String dataSource;
  private String formatHint;
  private Map<String, String> httpHeaders;

  VideoPlayer(
      Context context,
      EventChannel eventChannel,
      TextureRegistry.SurfaceTextureEntry textureEntry,
      String dataSource,
      String formatHint,
      @NonNull Map<String, String> httpHeaders,
      VideoPlayerOptions options) {
    this.dataSource = dataSource;
    this.formatHint = formatHint;
    this.eventChannel = eventChannel;
    this.textureEntry = textureEntry;
    this.options = options;
    this.httpHeaders = httpHeaders;

    MediaItem mediaItem =
        new MediaItem.Builder()
            .setUri(dataSource)
            .setMimeType(mimeFromFormatHint(formatHint))
            .build();

    httpDataSourceFactory = new DefaultHttpDataSource.Factory();
    configureHttpDataSourceFactory(httpHeaders);

    ExoPlayer exoPlayer = buildExoPlayer(context, httpDataSourceFactory);

    exoPlayer.setMediaItem(mediaItem);
    exoPlayer.prepare();

    setUpVideoPlayer(exoPlayer, new QueuingEventSink());
  }

  // Constructor used to directly test members of this class.
  @VisibleForTesting
  VideoPlayer(
      ExoPlayer exoPlayer,
      EventChannel eventChannel,
      TextureRegistry.SurfaceTextureEntry textureEntry,
      VideoPlayerOptions options,
      QueuingEventSink eventSink,
      DefaultHttpDataSource.Factory httpDataSourceFactory) {
    this.eventChannel = eventChannel;
    this.textureEntry = textureEntry;
    this.options = options;
    this.httpDataSourceFactory = httpDataSourceFactory;

    setUpVideoPlayer(exoPlayer, eventSink);
  }

  @VisibleForTesting
  public void configureHttpDataSourceFactory(@NonNull Map<String, String> httpHeaders) {
    final boolean httpHeadersNotEmpty = !httpHeaders.isEmpty();
    final String userAgent = httpHeadersNotEmpty && httpHeaders.containsKey(USER_AGENT)
        ? httpHeaders.get(USER_AGENT)
        : "ExoPlayer";

    unstableUpdateDataSourceFactory(
        httpDataSourceFactory, httpHeaders, userAgent, httpHeadersNotEmpty);
  }

  private void setUpVideoPlayer(ExoPlayer exoPlayer, QueuingEventSink eventSink) {
    this.exoPlayer = exoPlayer;
    this.eventSink = eventSink;

    eventChannel.setStreamHandler(
        new EventChannel.StreamHandler() {
          @Override
          public void onListen(Object o, EventChannel.EventSink sink) {
            eventSink.setDelegate(sink);
          }

          @Override
          public void onCancel(Object o) {
            eventSink.setDelegate(null);
          }
        });

    surface = new Surface(textureEntry.surfaceTexture());
    exoPlayer.setVideoSurface(surface);
    setAudioAttributes(exoPlayer, options.mixWithOthers);

    exoPlayer.addListener(
        new Listener() {
          private boolean isBuffering = false;

          public void setBuffering(boolean buffering) {
            if (isBuffering != buffering) {
              isBuffering = buffering;
              Map<String, Object> event = new HashMap<>();
              event.put("event", isBuffering ? "bufferingStart" : "bufferingEnd");
              eventSink.success(event);
            }
          }

          @Override
          public void onPlaybackStateChanged(final int playbackState) {
            if (playbackState == Player.STATE_BUFFERING) {
              setBuffering(true);
              sendBufferingUpdate();
            } else if (playbackState == Player.STATE_READY) {
              if (!isInitialized) {
                isInitialized = true;
                sendInitialized();
              }
            } else if (playbackState == Player.STATE_ENDED) {
              Map<String, Object> event = new HashMap<>();
              event.put("event", "completed");
              eventSink.success(event);
            }

            if (playbackState != Player.STATE_BUFFERING) {
              setBuffering(false);
            }
          }

          @Override
          public void onPlayerError(@NonNull final PlaybackException error) {
            setBuffering(false);
            if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
              // See https://exoplayer.dev/live-streaming.html#behindlivewindowexception-and-error_code_behind_live_window
              exoPlayer.seekToDefaultPosition();
              exoPlayer.prepare();
            } else if (eventSink != null) {
              eventSink.error("VideoError", "Video player had error " + describePlaybackException(error), null);
            }
          }

          @Override
          public void onIsPlayingChanged(boolean isPlaying) {
            if (eventSink != null) {
              Map<String, Object> event = new HashMap<>();
              event.put("event", "isPlayingStateUpdate");
              event.put("isPlaying", isPlaying);
              eventSink.success(event);
            }
          }
        });
  }

  // 原因例外をたどる深さと、1件あたりのメッセージ長の上限。
  // 通信失敗は原因が数段ネストするので複数段たどるが、無制限に連ねても読めないので打ち切る。
  private static final int MAX_CAUSE_DEPTH = 3;
  private static final int MAX_CAUSE_MESSAGE_LENGTH = 120;

  // 再生エラーの原因を Sentry から辿れるようにするための説明文を組み立てる。
  //
  // PlaybackException.toString() は難読化されたクラス名と "Source error" のような大分類しか
  // 含まず、実際の原因 (HTTPステータス・タイムアウト等) が落ちてしまう。エラーコード名は
  // 定数文字列なので難読化されず、Dart側でグルーピングキーとして使える。
  @VisibleForTesting
  static String describePlaybackException(PlaybackException error) {
    StringBuilder description = new StringBuilder("[").append(error.getErrorCodeName()).append("] ");
    description.append(error.getMessage() == null ? "no message" : error.getMessage());

    Throwable cause = error.getCause();
    for (int depth = 0; cause != null && depth < MAX_CAUSE_DEPTH; depth++) {
      description.append(" <- ").append(describeCause(cause));
      cause = cause.getCause();
    }
    return description.toString();
  }

  // 原因例外1件分の説明。難読化でクラス名が潰れるため、判別に使える情報を型ごとに取り出す。
  private static String describeCause(Throwable cause) {
    if (cause instanceof HttpDataSource.InvalidResponseCodeException) {
      // 署名切れ(403)やセグメント欠落(404)などを切り分けるための最重要情報
      return "http status " + ((HttpDataSource.InvalidResponseCodeException) cause).responseCode;
    }
    if (cause instanceof HttpDataSource.HttpDataSourceException) {
      // TYPE_OPEN / TYPE_READ / TYPE_CLOSE のどこで落ちたか
      return "http io type=" + ((HttpDataSource.HttpDataSourceException) cause).type + describeCauseMessage(cause);
    }
    if (cause instanceof ParserException) {
      return "parser error" + describeCauseMessage(cause);
    }

    // java.* / android.* は難読化されないライブラリクラスなので、名前がそのまま手がかりになる
    // (SocketTimeoutException / UnknownHostException など)
    String name = cause.getClass().getName();
    if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
      return name + describeCauseMessage(cause);
    }

    // 難読化されたクラスは名前に意味がないので、メッセージだけを残す
    String message = cause.getMessage();
    return message == null ? "unknown error" : truncateCauseMessage(message);
  }

  private static String describeCauseMessage(Throwable cause) {
    String message = cause.getMessage();
    return message == null ? "" : ": " + truncateCauseMessage(message);
  }

  private static String truncateCauseMessage(String message) {
    return message.length() <= MAX_CAUSE_MESSAGE_LENGTH
        ? message
        : message.substring(0, MAX_CAUSE_MESSAGE_LENGTH) + "...";
  }

  void sendBufferingUpdate() {
    Map<String, Object> event = new HashMap<>();
    event.put("event", "bufferingUpdate");
    List<? extends Number> range = Arrays.asList(0, exoPlayer.getBufferedPosition());
    // iOS supports a list of buffered ranges, so here is a list with a single
    // range.
    event.put("values", Collections.singletonList(range));
    eventSink.success(event);
  }

  private static void setAudioAttributes(ExoPlayer exoPlayer, boolean isMixMode) {
    exoPlayer.setAudioAttributes(
        new AudioAttributes.Builder().setContentType(C.AUDIO_CONTENT_TYPE_MOVIE).build(),
        !isMixMode);
  }

  /// Notification

  private Boolean initialized = false;

  private PlayerNotificationManager playerNotificationManager;

  @SuppressWarnings("deprecation")
  void setupNotification(Context context,
      String title, String artist, Boolean isLiveStream,
      String artworkUrl, String defaultArtworkAssetPath) {
    if (initialized)
      return;

    setupNotificationChannel(context);

    playerNotificationManager = new PlayerNotificationManager.Builder(context,
        NOTIFICATION_ID,
        NOTIFICATION_CHANNEL)
        .setMediaDescriptionAdapter(
            createMediaDescriptionAdapter(
                context,
                title, artist, isLiveStream,
                artworkUrl, defaultArtworkAssetPath))
        .build();

    playerNotificationManager.setPlayer(exoPlayer);

    playerNotificationManager.setUseFastForwardAction(false);
    playerNotificationManager.setUseRewindAction(false);
    playerNotificationManager.setUseNextAction(false);
    playerNotificationManager.setUsePreviousAction(false);
    playerNotificationManager.setUseStopAction(false);

    setupMediaSession(context);

    playerNotificationManager.setMediaSessionToken(mediaSession.getSessionCompatToken());

    initialized = true;
  }

  private void setupNotificationChannel(Context context) {
    NotificationChannel channel = new NotificationChannel(
        NOTIFICATION_CHANNEL,
        NOTIFICATION_CHANNEL,
        NotificationManager.IMPORTANCE_LOW);
    channel.setDescription(NOTIFICATION_CHANNEL);
    NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
    notificationManager.createNotificationChannel(channel);
  }

  private MediaSession mediaSession;

  private MediaSession setupMediaSession(Context context) {
    if (this.mediaSession != null)
      this.mediaSession.release();

    PendingIntent sessionActivity = buildSessionActivityPendingIntent(context);

    // media3 は MediaSession の ID をプロセス内で一意に扱い、同じ ID のセッションが生きている
    // うちにもう一つ作ると IllegalStateException を投げる。画面遷移の都合でプレイヤーが一時的に
    // 2 つ並ぶことがあるため、固定 ID だと後から再生を始めた側が必ず落ちていた (SRS-3368)。
    // 通知 ID は共有のままなので、通知に出るのは最後に再生を始めたプレイヤーになる。
    MediaSession.Builder builder =
        new MediaSession.Builder(context, exoPlayer).setId("VideoPlayer-" + textureEntry.id());
    if(sessionActivity != null){
      builder.setSessionActivity(sessionActivity);
    }

    this.mediaSession = builder.build();
    return this.mediaSession;
  }

  private PendingIntent buildSessionActivityPendingIntent(android.content.Context context) {
    PackageManager pm = context.getPackageManager();
    Intent launch = pm.getLaunchIntentForPackage(context.getPackageName());
    if (launch == null) {
      return null;
    }

    launch.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

    int flags = PendingIntent.FLAG_UPDATE_CURRENT;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      flags |= PendingIntent.FLAG_IMMUTABLE;
    }

    try {
      return PendingIntent.getActivity(context, 0, launch, flags);
    } catch (Throwable t) {
      return null;
    }
  }

  // Picassoは渡されたTargetを弱参照で扱うので、保持しておかないとGCされてしまうことがある
  private Target _targetRef;

  // Android 11以降はMediaSessionにMediaMetadataを設定する方法もあるが、
  // Android 10には反映されないのと、動的にArtworkを差し替えるのが難しいので、MediaDescriptionAdapterを使う
  private PlayerNotificationManager.MediaDescriptionAdapter createMediaDescriptionAdapter(Context context,
      String title, String artist, Boolean isLiveStream,
      String artworkUrl, String defaultArtworkAssetPath) {
    return new PlayerNotificationManager.MediaDescriptionAdapter() {
      @Override
      public String getCurrentContentTitle(Player player) {
        return title;
      }

      @Override
      public String getCurrentContentText(Player player) {
        return artist;
      }

      @Override
      public PendingIntent createCurrentContentIntent(Player player) {
        String packageName = context.getPackageName();
        Intent notificationIntent = new Intent();
        notificationIntent.setClassName(packageName, packageName + ".MainActivity");
        notificationIntent.setFlags(
          Intent.FLAG_ACTIVITY_CLEAR_TOP
          | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        return PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
      }

      private Bitmap _bmp;

      @Override
      public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
        // LiveStreamの場合は頻繁に呼ばれるのでキャッシュしておく
        if (_bmp != null)
          return _bmp;

        if (defaultArtworkAssetPath != null && !defaultArtworkAssetPath.isBlank()) {
          try {
            InputStream stream = context.getAssets().open(defaultArtworkAssetPath);
            _bmp = BitmapFactory.decodeStream(stream);
          } catch (Exception exception) {
            Log.e(TAG, exception.toString());
          }
        }

        if (artworkUrl != null && !artworkUrl.isBlank()) {
          _targetRef = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
              if (bitmap != null) {
                _bmp = bitmap;
                callback.onBitmap(bitmap);
              }

              _targetRef = null;
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
          };
          Picasso.get().load(artworkUrl).into(_targetRef);

        }

        return _bmp;
      }
    };
  }

  ///

  void play() {
    exoPlayer.setPlayWhenReady(true);
  }

  void pause() {
    exoPlayer.setPlayWhenReady(false);
  }

  void setLooping(boolean value) {
    exoPlayer.setRepeatMode(value ? REPEAT_MODE_ALL : REPEAT_MODE_OFF);
  }

  void setVolume(double value) {
    float bracketedValue = (float) Math.max(0.0, Math.min(1.0, value));
    exoPlayer.setVolume(bracketedValue);
  }

  void setPlaybackSpeed(double value) {
    // We do not need to consider pitch and skipSilence for now as we do not handle
    // them and
    // therefore never diverge from the default values.
    final PlaybackParameters playbackParameters = new PlaybackParameters(((float) value));

    exoPlayer.setPlaybackParameters(playbackParameters);
  }

  void seekTo(int location) {
    exoPlayer.seekTo(location);
  }

  long getPosition() {
    return exoPlayer.getCurrentPosition();
  }

  void replaceDataSource(String newDataSource, Map<String, String> headers) {
    httpHeaders = headers;

    if (newDataSource.equals(dataSource))
      return;

    dataSource = newDataSource;
    // MediaSource mediaSource = buildMediaSource(Uri.parse(newDataSource), resolvingDataSourceFactory, formatHint);
    MediaItem item = new MediaItem.Builder()
      .setUri(newDataSource)
      .setMimeType(mimeFromFormatHint(formatHint))
      .build();

    exoPlayer.stop();
    exoPlayer.setMediaItem(item);
    exoPlayer.prepare();
  }

  @SuppressWarnings("SuspiciousNameCombination")
  @VisibleForTesting
  void sendInitialized() {
    if (isInitialized) {
      Map<String, Object> event = new HashMap<>();
      event.put("event", "initialized");
      event.put("duration", exoPlayer.getDuration());

      VideoSize videoSize = exoPlayer.getVideoSize();
      int width = videoSize.width;
      int height = videoSize.height;
      if (width != 0 && height != 0) {
        int rotationDegrees = videoSize.unappliedRotationDegrees;
        // Switch the width/height if video was taken in portrait mode
        if (rotationDegrees == 90 || rotationDegrees == 270) {
          width = videoSize.height;
          height = videoSize.width;
        }
        event.put("width", width);
        event.put("height", height);

        // Rotating the video with ExoPlayer does not seem to be possible with a
        // Surface,
        // so inform the Flutter code that the widget needs to be rotated to prevent
        // upside-down playback for videos with rotationDegrees of 180 (other
        // orientations work
        // correctly without correction).
        if (rotationDegrees == 180) {
          event.put("rotationCorrection", rotationDegrees);
        }
      }

      eventSink.success(event);
    }
  }

  void dispose() {
    if (mediaSession != null) {
      mediaSession.release();
    }
    mediaSession = null;

    if (playerNotificationManager != null) {
      playerNotificationManager.setPlayer(null);
    }

    if (isInitialized) {
      exoPlayer.stop();
    }
    textureEntry.release();
    eventChannel.setStreamHandler(null);
    if (surface != null) {
      surface.release();
    }
    if (exoPlayer != null) {
      exoPlayer.release();
    }
  }

  @NonNull
  private static ExoPlayer buildExoPlayer(
      Context context, DataSource.Factory baseDataSourceFactory) {
    DataSource.Factory dataSourceFactory =
        new DefaultDataSource.Factory(context, baseDataSourceFactory);
    DefaultMediaSourceFactory mediaSourceFactory =
        new DefaultMediaSourceFactory(context).setDataSourceFactory(dataSourceFactory);
    return new ExoPlayer.Builder(context)
        .setMediaSourceFactory(mediaSourceFactory)
        .setLoadControl(buildLoadControl())
        .build();
  }

  /**
   * シーク時のローディングを抑えるための LoadControl を組み立てる。
   *
   * <p>既定の {@link DefaultLoadControl} は再生済みの区間を保持しないため、巻き戻しのたびに
   * セグメントを取り直してローディングが挟まる。バックバッファを持たせ、再生再開に必要な
   * バッファ量も切り詰めることで、通常のシーク操作で待ちが発生しないようにする。
   */
  @OptIn(markerClass = UnstableApi.class)
  @NonNull
  private static DefaultLoadControl buildLoadControl() {
    return new DefaultLoadControl.Builder()
        .setBufferDurationsMs(
            DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
            DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
            BUFFER_FOR_PLAYBACK_MS,
            BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS)
        .setBackBuffer(BACK_BUFFER_DURATION_MS, /* retainBackBufferFromKeyframe= */ true)
        .build();
  }

  @Nullable
  private static String mimeFromFormatHint(@Nullable String formatHint) {
    if (formatHint == null) {
      return null;
    }
    switch (formatHint) {
      case FORMAT_SS:
        return MimeTypes.APPLICATION_SS;
      case FORMAT_DASH:
        return MimeTypes.APPLICATION_MPD;
      case FORMAT_HLS:
        return MimeTypes.APPLICATION_M3U8;
      case FORMAT_OTHER:
      default:
        return null;
    }
  }

  // TODO: migrate to stable API, see https://github.com/flutter/flutter/issues/147039
  @OptIn(markerClass = UnstableApi.class)
  private static void unstableUpdateDataSourceFactory(
      DefaultHttpDataSource.Factory factory,
      @NonNull Map<String, String> httpHeaders,
      String userAgent,
      boolean httpHeadersNotEmpty) {
    factory.setUserAgent(userAgent).setAllowCrossProtocolRedirects(true);

    if (httpHeadersNotEmpty) {
      factory.setDefaultRequestProperties(httpHeaders);
    }
  }
}
