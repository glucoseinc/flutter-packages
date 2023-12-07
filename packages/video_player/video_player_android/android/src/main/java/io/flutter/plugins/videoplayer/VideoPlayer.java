// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.videoplayer;

import static com.google.android.exoplayer2.Player.REPEAT_MODE_ALL;
import static com.google.android.exoplayer2.Player.REPEAT_MODE_OFF;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.Surface;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.Listener;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import static com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector.MediaMetadataProvider;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.ResolvingDataSource;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import static com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter;
import com.google.android.exoplayer2.util.Util;
import io.flutter.plugin.common.EventChannel;
import io.flutter.view.TextureRegistry;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileDescriptor;

final class VideoPlayer {
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

  private DefaultHttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSource.Factory();
  private ResolvingDataSource.Factory resolvingDataSourceFactory;

  private String dataSource;
  private String formatHint;
  private Map<String, String> httpHeaders = new HashMap<>();

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

    ExoPlayer exoPlayer = new ExoPlayer.Builder(context).build();
    Uri uri = Uri.parse(dataSource);

    buildHttpDataSourceFactory(httpHeaders);
    DataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(context, httpDataSourceFactory);

    resolvingDataSourceFactory = new ResolvingDataSource.Factory(
        dataSourceFactory,
        dataSpec -> dataSpec.withRequestHeaders(httpHeaders));

    MediaSource mediaSource = buildMediaSource(uri, resolvingDataSourceFactory, formatHint);

    exoPlayer.setMediaSource(mediaSource);
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
  public void buildHttpDataSourceFactory(@NonNull Map<String, String> httpHeaders) {
    final boolean httpHeadersNotEmpty = !httpHeaders.isEmpty();
    final String userAgent = httpHeadersNotEmpty && httpHeaders.containsKey(USER_AGENT)
        ? httpHeaders.get(USER_AGENT)
        : "ExoPlayer";

    httpDataSourceFactory.setUserAgent(userAgent).setAllowCrossProtocolRedirects(true);
  }

  private MediaSource buildMediaSource(
      Uri uri, DataSource.Factory mediaDataSourceFactory, String formatHint) {
    int type;
    if (formatHint == null) {
      type = Util.inferContentType(uri);
    } else {
      switch (formatHint) {
        case FORMAT_SS:
          type = C.CONTENT_TYPE_SS;
          break;
        case FORMAT_DASH:
          type = C.CONTENT_TYPE_DASH;
          break;
        case FORMAT_HLS:
          type = C.CONTENT_TYPE_HLS;
          break;
        case FORMAT_OTHER:
          type = C.CONTENT_TYPE_OTHER;
          break;
        default:
          type = -1;
          break;
      }
    }
    switch (type) {
      case C.CONTENT_TYPE_SS:
        return new SsMediaSource.Factory(
            new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mediaDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri));
      case C.CONTENT_TYPE_DASH:
        return new DashMediaSource.Factory(
            new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mediaDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri));
      case C.CONTENT_TYPE_HLS:
        return new HlsMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri));
      case C.CONTENT_TYPE_OTHER:
        return new ProgressiveMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri));
      default: {
        throw new IllegalStateException("Unsupported type: " + type);
      }
    }
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
            if (eventSink != null) {
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

  void setupNotification(Context context,
      String title, String artist, Boolean isLiveStream,
      String artworkUrl, String defaultArtworkAssetPath) {
    if (initialized)
      return;

    setupNotificationChannel(context);

    PlayerNotificationManager.Builder playerNotificationManagerBuilder = new PlayerNotificationManager.Builder(context,
        NOTIFICATION_ID,
        NOTIFICATION_CHANNEL);
    // Android 10 では MediaMetadata が参照されないので、Notification にメタデータを設定する
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
      playerNotificationManagerBuilder.setMediaDescriptionAdapter(
          createMediaDescriptionAdapter(
              context,
              title, artist, isLiveStream,
              artworkUrl, defaultArtworkAssetPath));
    }
    playerNotificationManager = playerNotificationManagerBuilder.build();

    playerNotificationManager.setPlayer(exoPlayer);

    // For Android 10
    // Android 11 以降はMediaSession側の設定が優先される
    playerNotificationManager.setUseFastForwardAction(false);
    playerNotificationManager.setUseRewindAction(false);
    playerNotificationManager.setUseNextAction(false);
    playerNotificationManager.setUsePreviousAction(false);
    playerNotificationManager.setUseStopAction(false);

    setupMediaSession(context, createMediaMetadataProvider(
        context,
        title, artist, isLiveStream,
        artworkUrl, defaultArtworkAssetPath));

    playerNotificationManager.setMediaSessionToken(mediaSession.getSessionToken());
    System.out.println("🐤🐤🐤");

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

  private MediaSessionCompat mediaSession;

  private MediaSessionCompat setupMediaSession(Context context, MediaMetadataProvider provider) {
    if (this.mediaSession != null)
      this.mediaSession.release();

    PendingIntent pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        new Intent(Intent.ACTION_MEDIA_BUTTON),
        PendingIntent.FLAG_IMMUTABLE);

    MediaSessionCompat mediaSession = new MediaSessionCompat(context,
        "VideoPlayer",
        null,
        pendingIntent);

    mediaSession.setActive(true);
    MediaSessionConnector mediaSessionConnector = new MediaSessionConnector(mediaSession);
    mediaSessionConnector.setEnabledPlaybackActions(
        PlaybackStateCompat.ACTION_PLAY_PAUSE
            | PlaybackStateCompat.ACTION_PLAY
            | PlaybackStateCompat.ACTION_PAUSE);
    mediaSessionConnector.setPlayer(exoPlayer);
    mediaSessionConnector.setMediaMetadataProvider(provider);

    this.mediaSession = mediaSession;
    return mediaSession;
  }

  // Metadata for Android >= 11
  private MediaMetadataProvider createMediaMetadataProvider(Context context,
      String title, String artist, Boolean isLiveStream,
      String artworkUrl, String defaultArtworkAssetPath) {
    return new MediaMetadataProvider() {
      @Override
      public MediaMetadataCompat getMetadata​(Player player) {
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, title)
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
            .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, artworkUrl);

        if (!isLiveStream) {
          builder
              .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, exoPlayer.getDuration());
        }

        return builder.build();
      }
    };
  }

  // Metadata for Android 10
  private MediaDescriptionAdapter createMediaDescriptionAdapter(Context context,
      String title, String artist, Boolean isLiveStream,
      String artworkUrl, String defaultArtworkAssetPath) {
    return new MediaDescriptionAdapter() {
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
        return PendingIntent.getActivity(context, 0, new Intent(Intent.ACTION_MEDIA_BUTTON),
            PendingIntent.FLAG_IMMUTABLE);
      }

      @Override
      public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {

        if (defaultArtworkAssetPath != null) {
          try {
            AssetFileDescriptor fd = context.getAssets().openFd(defaultArtworkAssetPath);
            Bitmap bmp = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor());
          } catch (Exception exception) {
            System.out.println("🐤🐤🐤 " + exception);
          }
        }
        return null;

        // OneTimeWorkRequest imageWorkRequest = new
        // OneTimeWorkRequest.Builder(ImageWorker.class)
        // .addTag(imageUrl)
        // .setInputData(
        // new Data.Builder()
        // .putString(BetterPlayerPlugin.URL_PARAMETER, imageUrl)
        // .build())
        // .build();

        // workManager.enqueue(imageWorkRequest);

        // Observer<WorkInfo> workInfoObserver = workInfo -> {
        // try {
        // if (workInfo != null) {
        // WorkInfo.State state = workInfo.getState();
        // if (state == WorkInfo.State.SUCCEEDED) {

        // Data outputData = workInfo.getOutputData();
        // String filePath =
        // outputData.getString(BetterPlayerPlugin.FILE_PATH_PARAMETER);
        // // Bitmap here is already processed and it's very small, so it won't
        // // break anything.
        // bitmap = BitmapFactory.decodeFile(filePath);
        // callback.onBitmap(bitmap);

        // }
        // if (state == WorkInfo.State.SUCCEEDED
        // || state == WorkInfo.State.CANCELLED
        // || state == WorkInfo.State.FAILED) {
        // final UUID uuid = imageWorkRequest.getId();
        // Observer<WorkInfo> observer = workerObserverMap.remove(uuid);
        // if (observer != null) {
        // workManager.getWorkInfoByIdLiveData(uuid).removeObserver(observer);
        // }
        // }
        // }

        // } catch (Exception exception) {
        // Log.e(TAG, "Image select error: " + exception);
        // }
        // };

        // final UUID workerUuid = imageWorkRequest.getId();
        // workManager.getWorkInfoByIdLiveData(workerUuid)
        // .observeForever(workInfoObserver);
        // workerObserverMap.put(workerUuid, workInfoObserver);

        // return null;
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
    MediaSource mediaSource = buildMediaSource(Uri.parse(newDataSource), resolvingDataSourceFactory, formatHint);

    exoPlayer.stop();
    exoPlayer.setMediaSource(mediaSource);
    exoPlayer.prepare();
  }

  @SuppressWarnings("SuspiciousNameCombination")
  @VisibleForTesting
  void sendInitialized() {
    if (isInitialized) {
      Map<String, Object> event = new HashMap<>();
      event.put("event", "initialized");
      event.put("duration", exoPlayer.getDuration());

      if (exoPlayer.getVideoFormat() != null) {
        Format videoFormat = exoPlayer.getVideoFormat();
        int width = videoFormat.width;
        int height = videoFormat.height;
        int rotationDegrees = videoFormat.rotationDegrees;
        // Switch the width/height if video was taken in portrait mode
        if (rotationDegrees == 90 || rotationDegrees == 270) {
          width = exoPlayer.getVideoFormat().height;
          height = exoPlayer.getVideoFormat().width;
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
}
