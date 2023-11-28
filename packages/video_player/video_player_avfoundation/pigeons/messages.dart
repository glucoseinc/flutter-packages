// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(PigeonOptions(
  dartOut: 'lib/src/messages.g.dart',
  dartTestOut: 'test/test_api.g.dart',
  objcHeaderOut: 'ios/Classes/messages.g.h',
  objcSourceOut: 'ios/Classes/messages.g.m',
  objcOptions: ObjcOptions(
    prefix: 'FLT',
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

class AutomaticallyStartsPictureInPictureMessage {
  AutomaticallyStartsPictureInPictureMessage(
    this.textureId,
    this.enableStartPictureInPictureAutomaticallyFromInline,
  );

  int textureId;
  bool enableStartPictureInPictureAutomaticallyFromInline;
}

class SetPictureInPictureOverlayRectMessage {
  SetPictureInPictureOverlayRectMessage(
    this.textureId,
    this.rect,
  );

  int textureId;
  PictureInPictureOverlayRect? rect;
}

class PictureInPictureOverlayRect {
  PictureInPictureOverlayRect({
    required this.top,
    required this.left,
    required this.width,
    required this.height,
  });

  double top;
  double left;
  double width;
  double height;
}

class StartPictureInPictureMessage {
  StartPictureInPictureMessage(this.textureId);

  int textureId;
}

class StopPictureInPictureMessage {
  StopPictureInPictureMessage(this.textureId);

  int textureId;
}

class ReplaceDataSourceMessage {
  ReplaceDataSourceMessage(this.textureId, {required this.httpHeaders});

  int textureId;
  String? asset;
  String? uri;
  String? packageName;
  String? formatHint;
  Map<String?, String?> httpHeaders;
}

@HostApi(dartHostTestHandler: 'TestHostVideoPlayerApi')
abstract class AVFoundationVideoPlayerApi {
  @ObjCSelector('initialize')
  void initialize();

  @ObjCSelector('create:')
  TextureMessage create(CreateMessage msg);

  @ObjCSelector('dispose:')
  void dispose(TextureMessage msg);

  @ObjCSelector('setLooping:')
  void setLooping(LoopingMessage msg);

  @ObjCSelector('setVolume:')
  void setVolume(VolumeMessage msg);

  @ObjCSelector('setPlaybackSpeed:')
  void setPlaybackSpeed(PlaybackSpeedMessage msg);

  @ObjCSelector('play:')
  void play(TextureMessage msg);

  @ObjCSelector('position:')
  PositionMessage position(TextureMessage msg);

  @async
  @ObjCSelector('seekTo:')
  void seekTo(PositionMessage msg);

  @ObjCSelector('pause:')
  void pause(TextureMessage msg);

  @ObjCSelector('setMixWithOthers:')
  void setMixWithOthers(MixWithOthersMessage msg);

  @ObjCSelector('setupMux:')
  void setupMux(MuxConfigMessage msg);

  @ObjCSelector('isPictureInPictureSupported')
  bool isPictureInPictureSupported();

  @ObjCSelector('setPictureInPictureOverlayRect:')
  void setPictureInPictureOverlayRect(SetPictureInPictureOverlayRectMessage msg);

  @ObjCSelector('setAutomaticallyStartsPictureInPicture:')
  void setAutomaticallyStartsPictureInPicture(AutomaticallyStartsPictureInPictureMessage msg);

  @ObjCSelector('startPictureInPicture:')
  void startPictureInPicture(StartPictureInPictureMessage msg);

  @ObjCSelector('stopPictureInPicture:')
  void stopPictureInPicture(StopPictureInPictureMessage msg);

  @ObjCSelector('replaceDataSource:')
  void replaceDataSource(ReplaceDataSourceMessage msg);
}
