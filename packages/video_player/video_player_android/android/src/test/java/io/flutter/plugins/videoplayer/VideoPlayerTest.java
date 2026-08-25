// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.videoplayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

import android.graphics.SurfaceTexture;
import android.net.Uri;
import androidx.media3.common.ParserException;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.VideoSize;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.datasource.HttpDataSource;
import androidx.media3.exoplayer.ExoPlayer;
import io.flutter.plugin.common.EventChannel;
import io.flutter.view.TextureRegistry;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class VideoPlayerTest {
  private ExoPlayer fakeExoPlayer;
  private EventChannel fakeEventChannel;
  private TextureRegistry.SurfaceTextureEntry fakeSurfaceTextureEntry;
  private SurfaceTexture fakeSurfaceTexture;
  private VideoPlayerOptions fakeVideoPlayerOptions;
  private QueuingEventSink fakeEventSink;
  private DefaultHttpDataSource.Factory httpDataSourceFactorySpy;

  @Captor private ArgumentCaptor<HashMap<String, Object>> eventCaptor;

  @Before
  public void before() {
    MockitoAnnotations.openMocks(this);

    fakeExoPlayer = mock(ExoPlayer.class);
    fakeEventChannel = mock(EventChannel.class);
    fakeSurfaceTextureEntry = mock(TextureRegistry.SurfaceTextureEntry.class);
    fakeSurfaceTexture = mock(SurfaceTexture.class);
    when(fakeSurfaceTextureEntry.surfaceTexture()).thenReturn(fakeSurfaceTexture);
    fakeVideoPlayerOptions = mock(VideoPlayerOptions.class);
    fakeEventSink = mock(QueuingEventSink.class);
    httpDataSourceFactorySpy = spy(new DefaultHttpDataSource.Factory());
  }

  @Test
  public void videoPlayer_buildsHttpDataSourceFactoryProperlyWhenHttpHeadersNull() {
    VideoPlayer videoPlayer =
        new VideoPlayer(
            fakeExoPlayer,
            fakeEventChannel,
            fakeSurfaceTextureEntry,
            fakeVideoPlayerOptions,
            fakeEventSink,
            httpDataSourceFactorySpy);

    videoPlayer.configureHttpDataSourceFactory(new HashMap<>());

    verify(httpDataSourceFactorySpy).setUserAgent("ExoPlayer");
    verify(httpDataSourceFactorySpy).setAllowCrossProtocolRedirects(true);
    verify(httpDataSourceFactorySpy, never()).setDefaultRequestProperties(any());
  }

  @Test
  public void
      videoPlayer_buildsHttpDataSourceFactoryProperlyWhenHttpHeadersNonNullAndUserAgentSpecified() {
    VideoPlayer videoPlayer =
        new VideoPlayer(
            fakeExoPlayer,
            fakeEventChannel,
            fakeSurfaceTextureEntry,
            fakeVideoPlayerOptions,
            fakeEventSink,
            httpDataSourceFactorySpy);
    Map<String, String> httpHeaders =
        new HashMap<String, String>() {
          {
            put("header", "value");
            put("User-Agent", "userAgent");
          }
        };

    videoPlayer.configureHttpDataSourceFactory(httpHeaders);

    verify(httpDataSourceFactorySpy).setUserAgent("userAgent");
    verify(httpDataSourceFactorySpy).setAllowCrossProtocolRedirects(true);
    verify(httpDataSourceFactorySpy).setDefaultRequestProperties(httpHeaders);
  }

  @Test
  public void
      videoPlayer_buildsHttpDataSourceFactoryProperlyWhenHttpHeadersNonNullAndUserAgentNotSpecified() {
    VideoPlayer videoPlayer =
        new VideoPlayer(
            fakeExoPlayer,
            fakeEventChannel,
            fakeSurfaceTextureEntry,
            fakeVideoPlayerOptions,
            fakeEventSink,
            httpDataSourceFactorySpy);
    Map<String, String> httpHeaders =
        new HashMap<String, String>() {
          {
            put("header", "value");
          }
        };

    videoPlayer.configureHttpDataSourceFactory(httpHeaders);

    verify(httpDataSourceFactorySpy).setUserAgent("ExoPlayer");
    verify(httpDataSourceFactorySpy).setAllowCrossProtocolRedirects(true);
    verify(httpDataSourceFactorySpy).setDefaultRequestProperties(httpHeaders);
  }

  @Test
  public void sendInitializedSendsExpectedEvent_90RotationDegrees() {
    VideoPlayer videoPlayer =
        new VideoPlayer(
            fakeExoPlayer,
            fakeEventChannel,
            fakeSurfaceTextureEntry,
            fakeVideoPlayerOptions,
            fakeEventSink,
            httpDataSourceFactorySpy);
    VideoSize testVideoSize = new VideoSize(100, 200, 90, 1f);

    when(fakeExoPlayer.getVideoSize()).thenReturn(testVideoSize);
    when(fakeExoPlayer.getDuration()).thenReturn(10L);

    videoPlayer.isInitialized = true;
    videoPlayer.sendInitialized();

    verify(fakeEventSink).success(eventCaptor.capture());
    HashMap<String, Object> event = eventCaptor.getValue();

    assertEquals(event.get("event"), "initialized");
    assertEquals(event.get("duration"), 10L);
    assertEquals(event.get("width"), 200);
    assertEquals(event.get("height"), 100);
    assertEquals(event.get("rotationCorrection"), null);
  }

  @Test
  public void sendInitializedSendsExpectedEvent_270RotationDegrees() {
    VideoPlayer videoPlayer =
        new VideoPlayer(
            fakeExoPlayer,
            fakeEventChannel,
            fakeSurfaceTextureEntry,
            fakeVideoPlayerOptions,
            fakeEventSink,
            httpDataSourceFactorySpy);
    VideoSize testVideoSize = new VideoSize(100, 200, 270, 1f);

    when(fakeExoPlayer.getVideoSize()).thenReturn(testVideoSize);
    when(fakeExoPlayer.getDuration()).thenReturn(10L);

    videoPlayer.isInitialized = true;
    videoPlayer.sendInitialized();

    verify(fakeEventSink).success(eventCaptor.capture());
    HashMap<String, Object> event = eventCaptor.getValue();

    assertEquals(event.get("event"), "initialized");
    assertEquals(event.get("duration"), 10L);
    assertEquals(event.get("width"), 200);
    assertEquals(event.get("height"), 100);
    assertEquals(event.get("rotationCorrection"), null);
  }

  @Test
  public void sendInitializedSendsExpectedEvent_0RotationDegrees() {
    VideoPlayer videoPlayer =
        new VideoPlayer(
            fakeExoPlayer,
            fakeEventChannel,
            fakeSurfaceTextureEntry,
            fakeVideoPlayerOptions,
            fakeEventSink,
            httpDataSourceFactorySpy);
    VideoSize testVideoSize = new VideoSize(100, 200, 0, 1f);

    when(fakeExoPlayer.getVideoSize()).thenReturn(testVideoSize);
    when(fakeExoPlayer.getDuration()).thenReturn(10L);

    videoPlayer.isInitialized = true;
    videoPlayer.sendInitialized();

    verify(fakeEventSink).success(eventCaptor.capture());
    HashMap<String, Object> event = eventCaptor.getValue();

    assertEquals(event.get("event"), "initialized");
    assertEquals(event.get("duration"), 10L);
    assertEquals(event.get("width"), 100);
    assertEquals(event.get("height"), 200);
    assertEquals(event.get("rotationCorrection"), null);
  }

  @Test
  public void sendInitializedSendsExpectedEvent_180RotationDegrees() {
    VideoPlayer videoPlayer =
        new VideoPlayer(
            fakeExoPlayer,
            fakeEventChannel,
            fakeSurfaceTextureEntry,
            fakeVideoPlayerOptions,
            fakeEventSink,
            httpDataSourceFactorySpy);
    VideoSize testVideoSize = new VideoSize(100, 200, 180, 1f);

    when(fakeExoPlayer.getVideoSize()).thenReturn(testVideoSize);
    when(fakeExoPlayer.getDuration()).thenReturn(10L);

    videoPlayer.isInitialized = true;
    videoPlayer.sendInitialized();

    verify(fakeEventSink).success(eventCaptor.capture());
    HashMap<String, Object> event = eventCaptor.getValue();

    assertEquals(event.get("event"), "initialized");
    assertEquals(event.get("duration"), 10L);
    assertEquals(event.get("width"), 100);
    assertEquals(event.get("height"), 200);
    assertEquals(event.get("rotationCorrection"), 180);
  }

  @Test
  public void onIsPlayingChangedSendsExpectedEvent() {
    VideoPlayer videoPlayer =
        new VideoPlayer(
            fakeExoPlayer,
            fakeEventChannel,
            fakeSurfaceTextureEntry,
            fakeVideoPlayerOptions,
            fakeEventSink,
            httpDataSourceFactorySpy);

    doAnswer(
            (Answer<Void>)
                invocation -> {
                  Map<String, Object> event = new HashMap<>();
                  event.put("event", "isPlayingStateUpdate");
                  event.put("isPlaying", (Boolean) invocation.getArguments()[0]);
                  fakeEventSink.success(event);
                  return null;
                })
        .when(fakeExoPlayer)
        .setPlayWhenReady(anyBoolean());

    videoPlayer.play();

    verify(fakeEventSink).success(eventCaptor.capture());
    HashMap<String, Object> event1 = eventCaptor.getValue();

    assertEquals(event1.get("event"), "isPlayingStateUpdate");
    assertEquals(event1.get("isPlaying"), true);

    videoPlayer.pause();

    verify(fakeEventSink, times(2)).success(eventCaptor.capture());
    HashMap<String, Object> event2 = eventCaptor.getValue();

    assertEquals(event2.get("event"), "isPlayingStateUpdate");
    assertEquals(event2.get("isPlaying"), false);
  }

  @Test
  public void behindLiveWindowErrorResetsPlayerToDefaultPosition() {
    List<Player.Listener> listeners = new LinkedList<>();
    doAnswer(invocation -> listeners.add(invocation.getArgument(0)))
        .when(fakeExoPlayer)
        .addListener(any());

    VideoPlayer unused =
        new VideoPlayer(
            fakeExoPlayer,
            fakeEventChannel,
            fakeSurfaceTextureEntry,
            fakeVideoPlayerOptions,
            fakeEventSink,
            httpDataSourceFactorySpy);

    PlaybackException exception =
        new PlaybackException(null, null, PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW);
    listeners.forEach(listener -> listener.onPlayerError(exception));

    verify(fakeExoPlayer).seekToDefaultPosition();
    verify(fakeExoPlayer).prepare();
  }

  @Test
  public void describePlaybackExceptionIncludesErrorCodeNameAndMessage() {
    PlaybackException exception =
        new PlaybackException("Source error", null, PlaybackException.ERROR_CODE_IO_UNSPECIFIED);

    assertEquals(
        "[ERROR_CODE_IO_UNSPECIFIED] Source error",
        VideoPlayer.describePlaybackException(exception));
  }

  @Test
  public void describePlaybackExceptionFallsBackWhenMessageIsMissing() {
    PlaybackException exception =
        new PlaybackException(null, null, PlaybackException.ERROR_CODE_IO_UNSPECIFIED);

    assertEquals(
        "[ERROR_CODE_IO_UNSPECIFIED] no message", VideoPlayer.describePlaybackException(exception));
  }

  @Test
  public void describePlaybackExceptionExposesHttpStatusCode() {
    HttpDataSource.InvalidResponseCodeException cause =
        new HttpDataSource.InvalidResponseCodeException(
            403,
            "Forbidden",
            null,
            Collections.emptyMap(),
            new DataSpec(Uri.parse("http://[::1]:8080/segment.ts")),
            new byte[0]);
    PlaybackException exception =
        new PlaybackException(
            "Source error", cause, PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS);

    assertEquals(
        "[ERROR_CODE_IO_BAD_HTTP_STATUS] Source error <- http status 403",
        VideoPlayer.describePlaybackException(exception));
  }

  @Test
  public void describePlaybackExceptionExposesHttpFailureType() {
    HttpDataSource.HttpDataSourceException cause =
        new HttpDataSource.HttpDataSourceException(
            "Unable to connect",
            new DataSpec(Uri.parse("http://[::1]:8080/segment.ts")),
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
            HttpDataSource.HttpDataSourceException.TYPE_OPEN);
    PlaybackException exception =
        new PlaybackException(
            "Source error", cause, PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED);

    assertEquals(
        "[ERROR_CODE_IO_NETWORK_CONNECTION_FAILED] Source error <- http io type="
            + HttpDataSource.HttpDataSourceException.TYPE_OPEN
            + ": Unable to connect",
        VideoPlayer.describePlaybackException(exception));
  }

  @Test
  public void describePlaybackExceptionLabelsParserErrors() {
    PlaybackException exception =
        new PlaybackException(
            "Source error",
            ParserException.createForMalformedManifest("Input does not start with #EXTM3U", null),
            PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED);

    assertTrue(
        VideoPlayer.describePlaybackException(exception)
            .startsWith(
                "[ERROR_CODE_PARSING_MANIFEST_MALFORMED] Source error <- parser error: "
                    + "Input does not start with #EXTM3U"));
  }

  @Test
  public void describePlaybackExceptionKeepsLibraryExceptionNames() {
    PlaybackException exception =
        new PlaybackException(
            "Source error",
            new SocketTimeoutException("timeout"),
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT);

    assertEquals(
        "[ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT] Source error"
            + " <- java.net.SocketTimeoutException: timeout",
        VideoPlayer.describePlaybackException(exception));
  }

  @Test
  public void describePlaybackExceptionStopsFollowingCausesAtThirdLevel() {
    IOException deepest = new IOException("level4");
    PlaybackException exception =
        new PlaybackException(
            "Source error",
            new IOException("level1", new IOException("level2", new IOException("level3", deepest))),
            PlaybackException.ERROR_CODE_IO_UNSPECIFIED);

    assertEquals(
        "[ERROR_CODE_IO_UNSPECIFIED] Source error"
            + " <- java.io.IOException: level1"
            + " <- java.io.IOException: level2"
            + " <- java.io.IOException: level3",
        VideoPlayer.describePlaybackException(exception));
  }

  @Test
  public void describePlaybackExceptionTruncatesLongCauseMessages() {
    String longMessage = new String(new char[200]).replace('\0', 'a');
    PlaybackException exception =
        new PlaybackException(
            "Source error",
            new SocketTimeoutException(longMessage),
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT);

    assertEquals(
        "[ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT] Source error"
            + " <- java.net.SocketTimeoutException: "
            + longMessage.substring(0, 120)
            + "...",
        VideoPlayer.describePlaybackException(exception));
  }
}
