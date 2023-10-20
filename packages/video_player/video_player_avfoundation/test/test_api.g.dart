// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
// Autogenerated from Pigeon (v10.1.6), do not edit directly.
// See also: https://pub.dev/packages/pigeon
// ignore_for_file: public_member_api_docs, non_constant_identifier_names, avoid_as, unused_import, unnecessary_parenthesis, unnecessary_import
// ignore_for_file: avoid_relative_lib_imports
import 'dart:async';
import 'dart:typed_data' show Float64List, Int32List, Int64List, Uint8List;
import 'package:flutter/foundation.dart' show ReadBuffer, WriteBuffer;
import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:video_player_avfoundation/src/messages.g.dart';

class _TestHostVideoPlayerApiCodec extends StandardMessageCodec {
  const _TestHostVideoPlayerApiCodec();
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

abstract class TestHostVideoPlayerApi {
  static TestDefaultBinaryMessengerBinding? get _testBinaryMessengerBinding => TestDefaultBinaryMessengerBinding.instance;
  static const MessageCodec<Object?> codec = _TestHostVideoPlayerApiCodec();

  void initialize();

  TextureMessage create(CreateMessage msg);

  void dispose(TextureMessage msg);

  void setLooping(LoopingMessage msg);

  void setVolume(VolumeMessage msg);

  void setPlaybackSpeed(PlaybackSpeedMessage msg);

  void play(TextureMessage msg);

  PositionMessage position(TextureMessage msg);

  Future<void> seekTo(PositionMessage msg);

  void pause(TextureMessage msg);

  void setMixWithOthers(MixWithOthersMessage msg);

  void setupMux(MuxConfigMessage msg);

  bool isPictureInPictureSupported();

  void setPictureInPictureOverlayRect(SetPictureInPictureOverlayRectMessage msg);

  void setAutomaticallyStartsPictureInPicture(AutomaticallyStartsPictureInPictureMessage msg);

  void startPictureInPicture(StartPictureInPictureMessage msg);

  void stopPictureInPicture(StopPictureInPictureMessage msg);

  void replaceDataSource(ReplaceDataSourceMessage msg);

  static void setup(TestHostVideoPlayerApi? api, {BinaryMessenger? binaryMessenger}) {
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.initialize', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          // ignore message
          api.initialize();
          return <Object?>[];
        });
      }
    }
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.create', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.create was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final CreateMessage? arg_msg = (args[0] as CreateMessage?);
          assert(arg_msg != null,
              'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.create was null, expected non-null CreateMessage.');
          final TextureMessage output = api.create(arg_msg!);
          return <Object?>[output];
        });
      }
    }
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.dispose', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.dispose was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final TextureMessage? arg_msg = (args[0] as TextureMessage?);
          assert(arg_msg != null,
              'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.dispose was null, expected non-null TextureMessage.');
          api.dispose(arg_msg!);
          return <Object?>[];
        });
      }
    }
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setLooping', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setLooping was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final LoopingMessage? arg_msg = (args[0] as LoopingMessage?);
          assert(arg_msg != null,
              'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setLooping was null, expected non-null LoopingMessage.');
          api.setLooping(arg_msg!);
          return <Object?>[];
        });
      }
    }
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setVolume', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setVolume was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final VolumeMessage? arg_msg = (args[0] as VolumeMessage?);
          assert(arg_msg != null,
              'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setVolume was null, expected non-null VolumeMessage.');
          api.setVolume(arg_msg!);
          return <Object?>[];
        });
      }
    }
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setPlaybackSpeed', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setPlaybackSpeed was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final PlaybackSpeedMessage? arg_msg = (args[0] as PlaybackSpeedMessage?);
          assert(arg_msg != null,
              'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setPlaybackSpeed was null, expected non-null PlaybackSpeedMessage.');
          api.setPlaybackSpeed(arg_msg!);
          return <Object?>[];
        });
      }
    }
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.play', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.play was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final TextureMessage? arg_msg = (args[0] as TextureMessage?);
          assert(arg_msg != null,
              'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.play was null, expected non-null TextureMessage.');
          api.play(arg_msg!);
          return <Object?>[];
        });
      }
    }
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.position', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.position was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final TextureMessage? arg_msg = (args[0] as TextureMessage?);
          assert(arg_msg != null,
              'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.position was null, expected non-null TextureMessage.');
          final PositionMessage output = api.position(arg_msg!);
          return <Object?>[output];
        });
      }
    }
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.seekTo', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.seekTo was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final PositionMessage? arg_msg = (args[0] as PositionMessage?);
          assert(arg_msg != null,
              'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.seekTo was null, expected non-null PositionMessage.');
          await api.seekTo(arg_msg!);
          return <Object?>[];
        });
      }
    }
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.pause', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.pause was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final TextureMessage? arg_msg = (args[0] as TextureMessage?);
          assert(arg_msg != null,
              'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.pause was null, expected non-null TextureMessage.');
          api.pause(arg_msg!);
          return <Object?>[];
        });
      }
    }
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setMixWithOthers', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setMixWithOthers was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final MixWithOthersMessage? arg_msg = (args[0] as MixWithOthersMessage?);
          assert(arg_msg != null,
              'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setMixWithOthers was null, expected non-null MixWithOthersMessage.');
          api.setMixWithOthers(arg_msg!);
          return <Object?>[];
        });
      }
    }
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setupMux', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setupMux was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final MuxConfigMessage? arg_msg = (args[0] as MuxConfigMessage?);
          assert(arg_msg != null,
              'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setupMux was null, expected non-null MuxConfigMessage.');
          api.setupMux(arg_msg!);
          return <Object?>[];
        });
      }
    }
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.isPictureInPictureSupported', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          // ignore message
          final bool output = api.isPictureInPictureSupported();
          return <Object?>[output];
        });
      }
    }
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setPictureInPictureOverlayRect', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setPictureInPictureOverlayRect was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final SetPictureInPictureOverlayRectMessage? arg_msg = (args[0] as SetPictureInPictureOverlayRectMessage?);
          assert(arg_msg != null,
              'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setPictureInPictureOverlayRect was null, expected non-null SetPictureInPictureOverlayRectMessage.');
          api.setPictureInPictureOverlayRect(arg_msg!);
          return <Object?>[];
        });
      }
    }
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setAutomaticallyStartsPictureInPicture', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setAutomaticallyStartsPictureInPicture was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final AutomaticallyStartsPictureInPictureMessage? arg_msg = (args[0] as AutomaticallyStartsPictureInPictureMessage?);
          assert(arg_msg != null,
              'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.setAutomaticallyStartsPictureInPicture was null, expected non-null AutomaticallyStartsPictureInPictureMessage.');
          api.setAutomaticallyStartsPictureInPicture(arg_msg!);
          return <Object?>[];
        });
      }
    }
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.startPictureInPicture', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.startPictureInPicture was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final StartPictureInPictureMessage? arg_msg = (args[0] as StartPictureInPictureMessage?);
          assert(arg_msg != null,
              'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.startPictureInPicture was null, expected non-null StartPictureInPictureMessage.');
          api.startPictureInPicture(arg_msg!);
          return <Object?>[];
        });
      }
    }
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.stopPictureInPicture', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.stopPictureInPicture was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final StopPictureInPictureMessage? arg_msg = (args[0] as StopPictureInPictureMessage?);
          assert(arg_msg != null,
              'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.stopPictureInPicture was null, expected non-null StopPictureInPictureMessage.');
          api.stopPictureInPicture(arg_msg!);
          return <Object?>[];
        });
      }
    }
    {
      final BasicMessageChannel<Object?> channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.replaceDataSource', codec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, null);
      } else {
        _testBinaryMessengerBinding!.defaultBinaryMessenger.setMockDecodedMessageHandler<Object?>(channel, (Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.replaceDataSource was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final ReplaceDataSourceMessage? arg_msg = (args[0] as ReplaceDataSourceMessage?);
          assert(arg_msg != null,
              'Argument for dev.flutter.pigeon.video_player_avfoundation.AVFoundationVideoPlayerApi.replaceDataSource was null, expected non-null ReplaceDataSourceMessage.');
          api.replaceDataSource(arg_msg!);
          return <Object?>[];
        });
      }
    }
  }
}
