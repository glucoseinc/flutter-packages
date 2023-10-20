// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
// Autogenerated from Pigeon (v10.1.6), do not edit directly.
// See also: https://pub.dev/packages/pigeon

#import <Foundation/Foundation.h>

@protocol FlutterBinaryMessenger;
@protocol FlutterMessageCodec;
@class FlutterError;
@class FlutterStandardTypedData;

NS_ASSUME_NONNULL_BEGIN

@class FLTTextureMessage;
@class FLTLoopingMessage;
@class FLTVolumeMessage;
@class FLTPlaybackSpeedMessage;
@class FLTPositionMessage;
@class FLTCreateMessage;
@class FLTMixWithOthersMessage;
@class FLTMuxConfigMessage;
@class FLTAutomaticallyStartsPictureInPictureMessage;
@class FLTSetPictureInPictureOverlayRectMessage;
@class FLTPictureInPictureOverlayRect;
@class FLTStartPictureInPictureMessage;
@class FLTStopPictureInPictureMessage;
@class FLTReplaceDataSourceMessage;

@interface FLTTextureMessage : NSObject
/// `init` unavailable to enforce nonnull fields, see the `make` class method.
- (instancetype)init NS_UNAVAILABLE;
+ (instancetype)makeWithTextureId:(NSNumber *)textureId;
@property(nonatomic, strong) NSNumber * textureId;
@end

@interface FLTLoopingMessage : NSObject
/// `init` unavailable to enforce nonnull fields, see the `make` class method.
- (instancetype)init NS_UNAVAILABLE;
+ (instancetype)makeWithTextureId:(NSNumber *)textureId
    isLooping:(NSNumber *)isLooping;
@property(nonatomic, strong) NSNumber * textureId;
@property(nonatomic, strong) NSNumber * isLooping;
@end

@interface FLTVolumeMessage : NSObject
/// `init` unavailable to enforce nonnull fields, see the `make` class method.
- (instancetype)init NS_UNAVAILABLE;
+ (instancetype)makeWithTextureId:(NSNumber *)textureId
    volume:(NSNumber *)volume;
@property(nonatomic, strong) NSNumber * textureId;
@property(nonatomic, strong) NSNumber * volume;
@end

@interface FLTPlaybackSpeedMessage : NSObject
/// `init` unavailable to enforce nonnull fields, see the `make` class method.
- (instancetype)init NS_UNAVAILABLE;
+ (instancetype)makeWithTextureId:(NSNumber *)textureId
    speed:(NSNumber *)speed;
@property(nonatomic, strong) NSNumber * textureId;
@property(nonatomic, strong) NSNumber * speed;
@end

@interface FLTPositionMessage : NSObject
/// `init` unavailable to enforce nonnull fields, see the `make` class method.
- (instancetype)init NS_UNAVAILABLE;
+ (instancetype)makeWithTextureId:(NSNumber *)textureId
    position:(NSNumber *)position;
@property(nonatomic, strong) NSNumber * textureId;
@property(nonatomic, strong) NSNumber * position;
@end

@interface FLTCreateMessage : NSObject
/// `init` unavailable to enforce nonnull fields, see the `make` class method.
- (instancetype)init NS_UNAVAILABLE;
+ (instancetype)makeWithAsset:(nullable NSString *)asset
    uri:(nullable NSString *)uri
    packageName:(nullable NSString *)packageName
    formatHint:(nullable NSString *)formatHint
    httpHeaders:(NSDictionary<NSString *, NSString *> *)httpHeaders;
@property(nonatomic, copy, nullable) NSString * asset;
@property(nonatomic, copy, nullable) NSString * uri;
@property(nonatomic, copy, nullable) NSString * packageName;
@property(nonatomic, copy, nullable) NSString * formatHint;
@property(nonatomic, strong) NSDictionary<NSString *, NSString *> * httpHeaders;
@end

@interface FLTMixWithOthersMessage : NSObject
/// `init` unavailable to enforce nonnull fields, see the `make` class method.
- (instancetype)init NS_UNAVAILABLE;
+ (instancetype)makeWithMixWithOthers:(NSNumber *)mixWithOthers;
@property(nonatomic, strong) NSNumber * mixWithOthers;
@end

@interface FLTMuxConfigMessage : NSObject
+ (instancetype)makeWithTextureId:(nullable NSNumber *)textureId
    envKey:(nullable NSString *)envKey
    playerName:(nullable NSString *)playerName
    viewerUserId:(nullable NSString *)viewerUserId
    pageType:(nullable NSString *)pageType
    experimentName:(nullable NSString *)experimentName
    subPropertyId:(nullable NSString *)subPropertyId
    playerVersion:(nullable NSString *)playerVersion
    playerInitTime:(nullable NSNumber *)playerInitTime
    videoId:(nullable NSString *)videoId
    videoTitle:(nullable NSString *)videoTitle
    videoSeries:(nullable NSString *)videoSeries
    videoVariantName:(nullable NSString *)videoVariantName
    videoVariantId:(nullable NSString *)videoVariantId
    videoLanguageCode:(nullable NSString *)videoLanguageCode
    videoContentType:(nullable NSString *)videoContentType
    videoDuration:(nullable NSNumber *)videoDuration
    videoStreamType:(nullable NSString *)videoStreamType
    videoProducer:(nullable NSString *)videoProducer
    videoEncodingVariant:(nullable NSString *)videoEncodingVariant
    videoCdn:(nullable NSString *)videoCdn;
@property(nonatomic, strong, nullable) NSNumber * textureId;
@property(nonatomic, copy, nullable) NSString * envKey;
@property(nonatomic, copy, nullable) NSString * playerName;
@property(nonatomic, copy, nullable) NSString * viewerUserId;
@property(nonatomic, copy, nullable) NSString * pageType;
@property(nonatomic, copy, nullable) NSString * experimentName;
@property(nonatomic, copy, nullable) NSString * subPropertyId;
@property(nonatomic, copy, nullable) NSString * playerVersion;
@property(nonatomic, strong, nullable) NSNumber * playerInitTime;
@property(nonatomic, copy, nullable) NSString * videoId;
@property(nonatomic, copy, nullable) NSString * videoTitle;
@property(nonatomic, copy, nullable) NSString * videoSeries;
@property(nonatomic, copy, nullable) NSString * videoVariantName;
@property(nonatomic, copy, nullable) NSString * videoVariantId;
@property(nonatomic, copy, nullable) NSString * videoLanguageCode;
@property(nonatomic, copy, nullable) NSString * videoContentType;
@property(nonatomic, strong, nullable) NSNumber * videoDuration;
@property(nonatomic, copy, nullable) NSString * videoStreamType;
@property(nonatomic, copy, nullable) NSString * videoProducer;
@property(nonatomic, copy, nullable) NSString * videoEncodingVariant;
@property(nonatomic, copy, nullable) NSString * videoCdn;
@end

@interface FLTAutomaticallyStartsPictureInPictureMessage : NSObject
/// `init` unavailable to enforce nonnull fields, see the `make` class method.
- (instancetype)init NS_UNAVAILABLE;
+ (instancetype)makeWithTextureId:(NSNumber *)textureId
    enableStartPictureInPictureAutomaticallyFromInline:(NSNumber *)enableStartPictureInPictureAutomaticallyFromInline;
@property(nonatomic, strong) NSNumber * textureId;
@property(nonatomic, strong) NSNumber * enableStartPictureInPictureAutomaticallyFromInline;
@end

@interface FLTSetPictureInPictureOverlayRectMessage : NSObject
/// `init` unavailable to enforce nonnull fields, see the `make` class method.
- (instancetype)init NS_UNAVAILABLE;
+ (instancetype)makeWithTextureId:(NSNumber *)textureId
    rect:(nullable FLTPictureInPictureOverlayRect *)rect;
@property(nonatomic, strong) NSNumber * textureId;
@property(nonatomic, strong, nullable) FLTPictureInPictureOverlayRect * rect;
@end

@interface FLTPictureInPictureOverlayRect : NSObject
/// `init` unavailable to enforce nonnull fields, see the `make` class method.
- (instancetype)init NS_UNAVAILABLE;
+ (instancetype)makeWithTop:(NSNumber *)top
    left:(NSNumber *)left
    width:(NSNumber *)width
    height:(NSNumber *)height;
@property(nonatomic, strong) NSNumber * top;
@property(nonatomic, strong) NSNumber * left;
@property(nonatomic, strong) NSNumber * width;
@property(nonatomic, strong) NSNumber * height;
@end

@interface FLTStartPictureInPictureMessage : NSObject
/// `init` unavailable to enforce nonnull fields, see the `make` class method.
- (instancetype)init NS_UNAVAILABLE;
+ (instancetype)makeWithTextureId:(NSNumber *)textureId;
@property(nonatomic, strong) NSNumber * textureId;
@end

@interface FLTStopPictureInPictureMessage : NSObject
/// `init` unavailable to enforce nonnull fields, see the `make` class method.
- (instancetype)init NS_UNAVAILABLE;
+ (instancetype)makeWithTextureId:(NSNumber *)textureId;
@property(nonatomic, strong) NSNumber * textureId;
@end

@interface FLTReplaceDataSourceMessage : NSObject
/// `init` unavailable to enforce nonnull fields, see the `make` class method.
- (instancetype)init NS_UNAVAILABLE;
+ (instancetype)makeWithTextureId:(NSNumber *)textureId
    asset:(nullable NSString *)asset
    uri:(nullable NSString *)uri
    packageName:(nullable NSString *)packageName
    formatHint:(nullable NSString *)formatHint
    httpHeaders:(NSDictionary<NSString *, NSString *> *)httpHeaders;
@property(nonatomic, strong) NSNumber * textureId;
@property(nonatomic, copy, nullable) NSString * asset;
@property(nonatomic, copy, nullable) NSString * uri;
@property(nonatomic, copy, nullable) NSString * packageName;
@property(nonatomic, copy, nullable) NSString * formatHint;
@property(nonatomic, strong) NSDictionary<NSString *, NSString *> * httpHeaders;
@end

/// The codec used by FLTAVFoundationVideoPlayerApi.
NSObject<FlutterMessageCodec> *FLTAVFoundationVideoPlayerApiGetCodec(void);

@protocol FLTAVFoundationVideoPlayerApi
- (void)initialize:(FlutterError *_Nullable *_Nonnull)error;
/// @return `nil` only when `error != nil`.
- (nullable FLTTextureMessage *)create:(FLTCreateMessage *)msg error:(FlutterError *_Nullable *_Nonnull)error;
- (void)dispose:(FLTTextureMessage *)msg error:(FlutterError *_Nullable *_Nonnull)error;
- (void)setLooping:(FLTLoopingMessage *)msg error:(FlutterError *_Nullable *_Nonnull)error;
- (void)setVolume:(FLTVolumeMessage *)msg error:(FlutterError *_Nullable *_Nonnull)error;
- (void)setPlaybackSpeed:(FLTPlaybackSpeedMessage *)msg error:(FlutterError *_Nullable *_Nonnull)error;
- (void)play:(FLTTextureMessage *)msg error:(FlutterError *_Nullable *_Nonnull)error;
/// @return `nil` only when `error != nil`.
- (nullable FLTPositionMessage *)position:(FLTTextureMessage *)msg error:(FlutterError *_Nullable *_Nonnull)error;
- (void)seekTo:(FLTPositionMessage *)msg completion:(void (^)(FlutterError *_Nullable))completion;
- (void)pause:(FLTTextureMessage *)msg error:(FlutterError *_Nullable *_Nonnull)error;
- (void)setMixWithOthers:(FLTMixWithOthersMessage *)msg error:(FlutterError *_Nullable *_Nonnull)error;
- (void)setupMux:(FLTMuxConfigMessage *)msg error:(FlutterError *_Nullable *_Nonnull)error;
/// @return `nil` only when `error != nil`.
- (nullable NSNumber *)isPictureInPictureSupported:(FlutterError *_Nullable *_Nonnull)error;
- (void)setPictureInPictureOverlayRect:(FLTSetPictureInPictureOverlayRectMessage *)msg error:(FlutterError *_Nullable *_Nonnull)error;
- (void)setAutomaticallyStartsPictureInPicture:(FLTAutomaticallyStartsPictureInPictureMessage *)msg error:(FlutterError *_Nullable *_Nonnull)error;
- (void)startPictureInPicture:(FLTStartPictureInPictureMessage *)msg error:(FlutterError *_Nullable *_Nonnull)error;
- (void)stopPictureInPicture:(FLTStopPictureInPictureMessage *)msg error:(FlutterError *_Nullable *_Nonnull)error;
- (void)replaceDataSource:(FLTReplaceDataSourceMessage *)msg error:(FlutterError *_Nullable *_Nonnull)error;
@end

extern void FLTAVFoundationVideoPlayerApiSetup(id<FlutterBinaryMessenger> binaryMessenger, NSObject<FLTAVFoundationVideoPlayerApi> *_Nullable api);

NS_ASSUME_NONNULL_END
