// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(PigeonOptions(
  dartOut: 'lib/src/messages.g.dart',
  dartTestOut: 'test/test_api.g.dart',
  javaOut: 'android/src/main/java/io/flutter/plugins/videoplayer/Messages.java',
  javaOptions: JavaOptions(
    package: 'io.flutter.plugins.videoplayer',
  ),
  copyrightHeader: 'pigeons/copyright.txt',
))
class TextureMessage {
  TextureMessage(this.textureId);
  int textureId;
}

class LoopingMessage {
  LoopingMessage(this.textureId, this.isLooping);
  int textureId;
  bool isLooping;
}

class VolumeMessage {
  VolumeMessage(this.textureId, this.volume);
  int textureId;
  double volume;
}

class PlaybackSpeedMessage {
  PlaybackSpeedMessage(this.textureId, this.speed);
  int textureId;
  double speed;
}

class PositionMessage {
  PositionMessage(this.textureId, this.position);
  int textureId;
  int position;
}

class CreateMessage {
  CreateMessage({required this.httpHeaders, this.title = '', this.artist = '', this.isLiveStream = false});

  String? asset;
  String? uri;
  String? packageName;
  String? formatHint;
  Map<String?, String?> httpHeaders;
  String title;
  String artist;
  String? artworkUrl;
  String? defaultArtworkAssetPath;
  bool isLiveStream;
}

class MixWithOthersMessage {
  MixWithOthersMessage(this.mixWithOthers);
  bool mixWithOthers;
}

class ReplaceDataSourceMessage {
  ReplaceDataSourceMessage(this.textureId, this.asset, this.uri, this.packageName, this.formatHint, this.httpHeaders);
  int textureId;
  String? asset;
  String? uri;
  String? packageName;
  String? formatHint;
  Map<String?, String?> httpHeaders;
}

class MuxConfigMessage {
  int? textureId;
  String? envKey;
  String? playerName;
  String? viewerUserId;
  String? pageType;
  String? experimentName;
  String? subPropertyId;
  String? playerVersion;
  int? playerInitTime;
  String? videoId;
  String? videoTitle;
  String? videoSeries;
  String? videoVariantName;
  String? videoVariantId;
  String? videoLanguageCode;
  String? videoContentType;
  int? videoDuration;
  String? videoStreamType;
  String? videoProducer;
  String? videoEncodingVariant;
  String? videoCdn;
}

@HostApi(dartHostTestHandler: 'TestHostVideoPlayerApi')
abstract class AndroidVideoPlayerApi {
  void initialize();
  TextureMessage create(CreateMessage msg);
  void dispose(TextureMessage msg);
  void setLooping(LoopingMessage msg);
  void setVolume(VolumeMessage msg);
  void setPlaybackSpeed(PlaybackSpeedMessage msg);
  void play(TextureMessage msg);
  PositionMessage position(TextureMessage msg);
  void seekTo(PositionMessage msg);
  void pause(TextureMessage msg);
  void setMixWithOthers(MixWithOthersMessage msg);
  void replaceDataSource(ReplaceDataSourceMessage msg);
  void setupMux(MuxConfigMessage msg);
}
