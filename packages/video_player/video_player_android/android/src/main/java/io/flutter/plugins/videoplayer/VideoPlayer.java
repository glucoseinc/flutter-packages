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
import android.os.Build;;
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
import androidx.media3.common.PlaybackException;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.common.Player.Listener;
import androidx.media3.common.VideoSize;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
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
              eventSink.error("VideoError", "Video player had error " + error, null);
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

    MediaSession.Builder builder = new MediaSession.Builder(context, exoPlayer).setId("VideoPlayer");
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
    return new ExoPlayer.Builder(context).setMediaSourceFactory(mediaSourceFactory).build();
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
