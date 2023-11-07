// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
// Autogenerated from Pigeon (v10.1.6), do not edit directly.
// See also: https://pub.dev/packages/pigeon
// ignore_for_file: public_member_api_docs, non_constant_identifier_names, avoid_as, unused_import, unnecessary_parenthesis, prefer_null_aware_operators, omit_local_variable_types, unused_shown_name, unnecessary_import

import 'dart:async';
import 'dart:typed_data' show Float64List, Int32List, Int64List, Uint8List;

import 'package:flutter/foundation.dart' show ReadBuffer, WriteBuffer;
import 'package:flutter/services.dart';

class TextureMessage {
  TextureMessage({
    required this.textureId,
  });

  int textureId;

  Object encode() {
    return <Object?>[
      textureId,
    ];
  }

  static TextureMessage decode(Object result) {
    result as List<Object?>;
    return TextureMessage(
      textureId: result[0]! as int,
    );
  }
}

class LoopingMessage {
  LoopingMessage({
    required this.textureId,
    required this.isLooping,
  });

  int textureId;

  bool isLooping;

  Object encode() {
    return <Object?>[
      textureId,
      isLooping,
    ];
  }

  static LoopingMessage decode(Object result) {
    result as List<Object?>;
    return LoopingMessage(
      textureId: result[0]! as int,
      isLooping: result[1]! as bool,
    );
  }
}

class VolumeMessage {
  VolumeMessage({
    required this.textureId,
    required this.volume,
  });

  int textureId;

  double volume;

  Object encode() {
    return <Object?>[
      textureId,
      volume,
    ];
  }

  static VolumeMessage decode(Object result) {
    result as List<Object?>;
    return VolumeMessage(
      textureId: result[0]! as int,
      volume: result[1]! as double,
    );
  }
}

class PlaybackSpeedMessage {
  PlaybackSpeedMessage({
    required this.textureId,
    required this.speed,
  });

  int textureId;

  double speed;

  Object encode() {
    return <Object?>[
      textureId,
      speed,
    ];
  }

  static PlaybackSpeedMessage decode(Object result) {
    result as List<Object?>;
    return PlaybackSpeedMessage(
      textureId: result[0]! as int,
      speed: result[1]! as double,
    );
  }
}

class PositionMessage {
  PositionMessage({
    required this.textureId,
    required this.position,
  });

  int textureId;

  int position;

  Object encode() {
    return <Object?>[
      textureId,
      position,
    ];
  }

  static PositionMessage decode(Object result) {
    result as List<Object?>;
    return PositionMessage(
      textureId: result[0]! as int,
      position: result[1]! as int,
    );
  }
}

class CreateMessage {
  CreateMessage({
    this.asset,
    this.uri,
    this.packageName,
    this.formatHint,
    required this.httpHeaders,
    required this.title,
    required this.artist,
    this.imageUrl,
    required this.isLiveStream,
  });

  String? asset;

  String? uri;

  String? packageName;

  String? formatHint;

  Map<String?, String?> httpHeaders;

  String title;

  String artist;

  String? imageUrl;

  bool isLiveStream;

  Object encode() {
    return <Object?>[
      asset,
      uri,
      packageName,
      formatHint,
      httpHeaders,
      title,
      artist,
      imageUrl,
      isLiveStream,
    ];
  }

  static CreateMessage decode(Object result) {
    result as List<Object?>;
    return CreateMessage(
      asset: result[0] as String?,
      uri: result[1] as String?,
      packageName: result[2] as String?,
      formatHint: result[3] as String?,
      httpHeaders: (result[4] as Map<Object?, Object?>?)!.cast<String?, String?>(),
      title: result[5]! as String,
      artist: result[6]! as String,
      imageUrl: result[7] as String?,
      isLiveStream: result[8]! as bool,
    );
  }
}

class MixWithOthersMessage {
  MixWithOthersMessage({
    required this.mixWithOthers,
  });

  bool mixWithOthers;

  Object encode() {
    return <Object?>[
      mixWithOthers,
    ];
  }

  static MixWithOthersMessage decode(Object result) {
    result as List<Object?>;
    return MixWithOthersMessage(
      mixWithOthers: result[0]! as bool,
    );
  }
}

class MuxConfigMessage {
  MuxConfigMessage({
    this.textureId,
    this.envKey,
    this.playerName,
    this.viewerUserId,
    this.pageType,
    this.experimentName,
    this.subPropertyId,
    this.playerVersion,
    this.playerInitTime,
    this.videoId,
    this.videoTitle,
    this.videoSeries,
    this.videoVariantName,
    this.videoVariantId,
    this.videoLanguageCode,
    this.videoContentType,
    this.videoDuration,
    this.videoStreamType,
    this.videoProducer,
    this.videoEncodingVariant,
    this.videoCdn,
  });

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

  Object encode() {
    return <Object?>[
      textureId,
      envKey,
      playerName,
      viewerUserId,
      pageType,
      experimentName,
      subPropertyId,
      playerVersion,
      playerInitTime,
      videoId,
      videoTitle,
      videoSeries,
      videoVariantName,
      videoVariantId,
      videoLanguageCode,
      videoContentType,
      videoDuration,
      videoStreamType,
      videoProducer,
      videoEncodingVariant,
      videoCdn,
    ];
  }

  static MuxConfigMessage decode(Object result) {
    result as List<Object?>;
    return MuxConfigMessage(
      textureId: result[0] as int?,
      envKey: result[1] as String?,
      playerName: result[2] as String?,
      viewerUserId: result[3] as String?,
      pageType: result[4] as String?,
      experimentName: result[5] as String?,
      subPropertyId: result[6] as String?,
      playerVersion: result[7] as String?,
      playerInitTime: result[8] as int?,
      videoId: result[9] as String?,
      videoTitle: result[10] as String?,
      videoSeries: result[11] as String?,
      videoVariantName: result[12] as String?,
      videoVariantId: result[13] as String?,
      videoLanguageCode: result[14] as String?,
      videoContentType: result[15] as String?,
      videoDuration: result[16] as int?,
      videoStreamType: result[17] as String?,
      videoProducer: result[18] as String?,
      videoEncodingVariant: result[19] as String?,
      videoCdn: result[20] as String?,
    );
  }
}

class AutomaticallyStartsPictureInPictureMessage {
  AutomaticallyStartsPictureInPictureMessage({
    required this.textureId,
    required this.enableStartPictureInPictureAutomaticallyFromInline,
  });

  int textureId;

  bool enableStartPictureInPictureAutomaticallyFromInline;

  Object encode() {
    return <Object?>[
      textureId,
      enableStartPictureInPictureAutomaticallyFromInline,
    ];
  }

  static AutomaticallyStartsPictureInPictureMessage decode(Object result) {
    result as List<Object?>;
    return AutomaticallyStartsPictureInPictureMessage(
      textureId: result[0]! as int,
      enableStartPictureInPictureAutomaticallyFromInline: result[1]! as bool,
    );
  }
}

class SetPictureInPictureOverlayRectMessage {
  SetPictureInPictureOverlayRectMessage({
    required this.textureId,
    this.rect,
  });

  int textureId;

  PictureInPictureOverlayRect? rect;

  Object encode() {
    return <Object?>[
      textureId,
      rect?.encode(),
    ];
  }

  static SetPictureInPictureOverlayRectMessage decode(Object result) {
    result as List<Object?>;
    return SetPictureInPictureOverlayRectMessage(
      textureId: result[0]! as int,
      rect: result[1] != null
          ? PictureInPictureOverlayRect.decode(result[1]! as List<Object?>)
          : null,
    );
  }
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

  Object encode() {
    return <Object?>[
      top,
      left,
      width,
      height,
    ];
  }

  static PictureInPictureOverlayRect decode(Object result) {
    result as List<Object?>;
    return PictureInPictureOverlayRect(
      top: result[0]! as double,
      left: result[1]! as double,
      width: result[2]! as double,
      height: result[3]! as double,
    );
  }
}

class StartPictureInPictureMessage {
  StartPictureInPictureMessage({
    required this.textureId,
  });

  int textureId;

  Object encode() {
    return <Object?>[
      textureId,
    ];
  }

  static StartPictureInPictureMessage decode(Object result) {
    result as List<Object?>;
    return StartPictureInPictureMessage(
      textureId: result[0]! as int,
    );
  }
}

class StopPictureInPictureMessage {
  StopPictureInPictureMessage({
    required this.textureId,
  });

  int textureId;

  Object encode() {
    return <Object?>[
      textureId,
    ];
  }

  static StopPictureInPictureMessage decode(Object result) {
    result as List<Object?>;
    return StopPictureInPictureMessage(
      textureId: result[0]! as int,
    );
  }
}

class ReplaceDataSourceMessage {
  ReplaceDataSourceMessage({
    required this.textureId,
    this.asset,
    this.uri,
    this.packageName,
    this.formatHint,
    required this.httpHeaders,
  });

  int textureId;

  String? asset;

  String? uri;

  String? packageName;

  String? formatHint;

  Map<String?, String?> httpHeaders;

  Object encode() {
    return <Object?>[
      textureId,
      asset,
      uri,
      packageName,
      formatHint,
      httpHeaders,
    ];
  }

  static ReplaceDataSourceMessage decode(Object result) {
    result as List<Object?>;
    return ReplaceDataSourceMessage(
      textureId: result[0]! as int,
      asset: result[1] as String?,
      uri: result[2] as String?,
      packageName: result[3] as String?,
      formatHint: result[4] as String?,
      httpHeaders: (result[5] as Map<Object?, Object?>?)!.cast<String?, String?>(),
    );
  }
}

class _AVFoundationVideoPlayerApiCodec extends StandardMessageCodec {
  const _AVFoundationVideoPlayerApiCodec();
  @override
  void writeValue(WriteBuffer buffer, Object? value) {
    if (value is AutomaticallyStartsPictureInPictureMessage) {
      buffer.putUint8(128);
      writeValue(buffer, value.encode());
    } else if (value is CreateMessage) {
      buffer.putUint8(129);
      writeValue(buffer, value.encode());
    } else if (value is LoopingMessage) {
      buffer.putUint8(130);
      writeValue(buffer, value.encode());
    } else if (value is MixWithOthersMessage) {
      buffer.putUint8(131);
      writeValue(buffer, value.encode());
    } else if (value is MuxConfigMessage) {
      buffer.putUint8(132);
      writeValue(buffer, value.encode());
    } else if (value is PictureInPictureOverlayRect) {
      buffer.putUint8(133);
      writeValue(buffer, value.encode());
    } else if (value is PlaybackSpeedMessage) {
      buffer.putUint8(134);
      writeValue(buffer, value.encode());
    } else if (value is PositionMessage) {
      buffer.putUint8(135);
      writeValue(buffer, value.encode());
    } else if (value is ReplaceDataSourceMessage) {
      buffer.putUint8(136);
      writeValue(buffer, value.encode());
    } else if (value is SetPictureInPictureOverlayRectMessage) {
      buffer.putUint8(137);
      writeValue(buffer, value.encode());
    } else if (value is StartPictureInPictureMessage) {
      buffer.putUint8(138);
      writeValue(buffer, value.encode());
    } else if (value is StopPictureInPictureMessage) {
      buffer.putUint8(139);
      writeValue(buffer, value.encode());
    } else if (value is TextureMessage) {
      buffer.putUint8(140);
      writeValue(buffer, value.encode());
    } else if (value is VolumeMessage) {
      buffer.putUint8(141);
      writeValue(buffer, value.encode());
    } else {
      super.writeValue(buffer, value);
    }
  }

  @override
  Object? readValueOfType(int type, ReadBuffer buffer) {
    switch (type) {
      case 128: 
        return AutomaticallyStartsPictureInPictureMessage.decode(readValue(buffer)!);
      case 129: 
        return CreateMessage.decode(readValue(buffer)!);
      case 130: 
        return LoopingMessage.decode(readValue(buffer)!);
      case 131: 
        return MixWithOthersMessage.decode(readValue(buffer)!);
      case 132: 
        return MuxConfigMessage.decode(readValue(buffer)!);
      case 133: 
        return PictureInPictureOverlayRect.decode(readValue(buffer)!);
      case 134: 
        return PlaybackSpeedMessage.decode(readValue(buffer)!);
      case 135: 
        return PositionMessage.decode(readValue(buffer)!);
      case 136: 
        return ReplaceDataSourceMessage.decode(readValue(buffer)!);
      case 137: 
        return SetPictureInPictureOverlayRectMessage.decode(readValue(buffer)!);
      case 138: 
        return StartPictureInPictureMessage.decode(readValue(buffer)!);
      case 139: 
        return StopPictureInPictureMessage.decode(readValue(buffer)!);
      case 140: 
        return TextureMessage.decode(readValue(buffer)!);
      case 141: 
        return VolumeMessage.decode(readValue(buffer)!);
      default:
        return super.readValueOfType(type, buffer);
    }
  }
}

class AVFoundationVideoPlayerApi {
  /// Constructor for [AVFoundationVideoPlayerApi].  The [binaryMessenger] named argument is
  /// available for dependency injection.  If it is left null, the default
  /// BinaryMessenger will be used which routes to the host platform.
  AVFoundationVideoPlayerApi({BinaryMessenger? binaryMessenger})
      : _binaryMessenger = binaryMessenger;
  final BinaryMessenger? _binaryMessenger;

  static const MessageCodec<Object?> codec = _AVFoundationVideoPlayerApiCodec();

  Future<void> initialize() async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.initialize', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(null) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else {
      return;
    }
  }

  Future<TextureMessage> create(CreateMessage arg_msg) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.create', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(<Object?>[arg_msg]) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else if (replyList[0] == null) {
      throw PlatformException(
        code: 'null-error',
        message: 'Host platform returned null value for non-null return value.',
      );
    } else {
      return (replyList[0] as TextureMessage?)!;
    }
  }

  Future<void> dispose(TextureMessage arg_msg) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.dispose', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(<Object?>[arg_msg]) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else {
      return;
    }
  }

  Future<void> setLooping(LoopingMessage arg_msg) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setLooping', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(<Object?>[arg_msg]) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else {
      return;
    }
  }

  Future<void> setVolume(VolumeMessage arg_msg) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setVolume', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(<Object?>[arg_msg]) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else {
      return;
    }
  }

  Future<void> setPlaybackSpeed(PlaybackSpeedMessage arg_msg) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setPlaybackSpeed', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(<Object?>[arg_msg]) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else {
      return;
    }
  }

  Future<void> play(TextureMessage arg_msg) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.play', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(<Object?>[arg_msg]) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else {
      return;
    }
  }

  Future<PositionMessage> position(TextureMessage arg_msg) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.position', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(<Object?>[arg_msg]) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else if (replyList[0] == null) {
      throw PlatformException(
        code: 'null-error',
        message: 'Host platform returned null value for non-null return value.',
      );
    } else {
      return (replyList[0] as PositionMessage?)!;
    }
  }

  Future<void> seekTo(PositionMessage arg_msg) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.seekTo', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(<Object?>[arg_msg]) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else {
      return;
    }
  }

  Future<void> pause(TextureMessage arg_msg) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.pause', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(<Object?>[arg_msg]) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else {
      return;
    }
  }

  Future<void> setMixWithOthers(MixWithOthersMessage arg_msg) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setMixWithOthers', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(<Object?>[arg_msg]) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else {
      return;
    }
  }

  Future<void> setupMux(MuxConfigMessage arg_msg) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setupMux', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(<Object?>[arg_msg]) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else {
      return;
    }
  }

  Future<bool> isPictureInPictureSupported() async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.isPictureInPictureSupported', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(null) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else if (replyList[0] == null) {
      throw PlatformException(
        code: 'null-error',
        message: 'Host platform returned null value for non-null return value.',
      );
    } else {
      return (replyList[0] as bool?)!;
    }
  }

  Future<void> setPictureInPictureOverlayRect(SetPictureInPictureOverlayRectMessage arg_msg) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setPictureInPictureOverlayRect', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(<Object?>[arg_msg]) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else {
      return;
    }
  }

  Future<void> setAutomaticallyStartsPictureInPicture(AutomaticallyStartsPictureInPictureMessage arg_msg) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setAutomaticallyStartsPictureInPicture', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(<Object?>[arg_msg]) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else {
      return;
    }
  }

  Future<void> startPictureInPicture(StartPictureInPictureMessage arg_msg) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.startPictureInPicture', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(<Object?>[arg_msg]) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else {
      return;
    }
  }

  Future<void> stopPictureInPicture(StopPictureInPictureMessage arg_msg) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.stopPictureInPicture', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(<Object?>[arg_msg]) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else {
      return;
    }
  }

  Future<void> replaceDataSource(ReplaceDataSourceMessage arg_msg) async {
    final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
        'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.replaceDataSource', codec,
        binaryMessenger: _binaryMessenger);
    final List<Object?>? replyList =
        await channel.send(<Object?>[arg_msg]) as List<Object?>?;
    if (replyList == null) {
      throw PlatformException(
        code: 'channel-error',
        message: 'Unable to establish connection on channel.',
      );
    } else if (replyList.length > 1) {
      throw PlatformException(
        code: replyList[0]! as String,
        message: replyList[1] as String?,
        details: replyList[2],
      );
    } else {
      return;
    }
  }
}
