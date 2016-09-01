#import "RevMobManager.h"
#import <RevMobAds/RevMobAds.h>
#import "RCTBridge.h"
#import "RCTEventDispatcher.h"

@implementation RevMobManager{
  RevMobFullscreen *video, *rewardedVideo, *fullscreen;
  RevMobBanner *banner;
  RevMobBanner *customBanner;
  RevMobAdLink *link;
}

RCT_EXPORT_MODULE();

// Uses main thread
- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

// Send events to Javascript
@synthesize bridge = _bridge;

RCT_EXPORT_METHOD(printEnvironmentInformation)
{
  [[RevMobAds session] printEnvironmentInformation];
}

RCT_EXPORT_METHOD(startSession:(NSString *)appId callback:(RCTResponseSenderBlock) callback)
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [RevMobAds startSessionWithAppID:appId
                withSuccessHandler:^{
                  if(callback) callback(@[[NSNull null]]);
                  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobSessionIsStarted" body: emptyDict];
                }
                    andFailHandler:^(NSError *error){
                      if(callback) callback(@[[error localizedDescription]]);
                      [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobSessionNotStarted" body: emptyDict];
                    }];
}

RCT_EXPORT_METHOD(setUserAgeRangeMin:(NSUInteger) userAgeRangeMin)
{
  [RevMobAds session].userAgeRangeMin = userAgeRangeMin;
}

RCT_EXPORT_METHOD(showFullscreen)
{
  RevMobFullscreen *fs = [[RevMobAds session] fullscreen];
  fs.delegate = self;
  [fs showAd];
}

RCT_EXPORT_METHOD(loadFullscreen)
{
  fullscreen = [[RevMobAds session] fullscreen];
  fullscreen.delegate = self;
  [fullscreen loadAd];
}

RCT_EXPORT_METHOD(showPreLoadedFullscreen)
{
  if (fullscreen) [fullscreen showAd];
}

RCT_EXPORT_METHOD(loadBanner)
{
  if (banner == nil){
    banner = [[RevMobAds session] banner];
    banner.delegate = self;
    [banner loadAd];
  }
}

RCT_EXPORT_METHOD(showBanner)
{
  if (banner){
    [banner showAd];
  }
}

RCT_EXPORT_METHOD(hideBanner)
{
  if (banner) {
    [banner hideAd];
  }
}

RCT_EXPORT_METHOD(releaseBanner)
{
  if(banner) {
    [banner releaseAd];
    banner = nil;
  }
}

RCT_EXPORT_METHOD(loadCustomBanner:(float) x y:(float) y
                  width:(float) width height:(float) height)
{
  if (customBanner == nil) {
    customBanner = [[RevMobAds session] banner];
    customBanner.delegate = self;
    [customBanner setFrame:CGRectMake(x, y, width, height)];
    [customBanner loadAd];
  }
}

RCT_EXPORT_METHOD(showCustomBanner)
{
  if (customBanner) {
    [customBanner showAd];
  }
}

RCT_EXPORT_METHOD(hideCustomBanner)
{
  if (customBanner) [customBanner hideAd];
}

RCT_EXPORT_METHOD(releaseCustomBanner)
{
  if (customBanner) {
    [customBanner releaseAd];
    customBanner = nil;
  }
}

RCT_EXPORT_METHOD(loadVideo)
{
  video = [[RevMobAds session] fullscreen];
  video.delegate = self;
  [video loadVideo];
}

RCT_EXPORT_METHOD(showVideo)
{
  if (video) [video showVideo];
}

RCT_EXPORT_METHOD(loadRewardedVideo)
{
  rewardedVideo = [[RevMobAds session] fullscreen];
  rewardedVideo.delegate = self;
  [rewardedVideo loadRewardedVideo];
}

RCT_EXPORT_METHOD(showRewardedVideo)
{
  if (rewardedVideo) [rewardedVideo showRewardedVideo];
}

RCT_EXPORT_METHOD(loadAdLink)
{
  link = [[RevMobAds session] adLink];
  [link loadWithSuccessHandler:^(RevMobAdLink *link) {
    [self revmobNativeDidReceive:NULL];
  } andLoadFailHandler:^(RevMobAdLink *link, NSError *error) {
    [self revmobNativeDidFailWithError:error onPlacement:NULL];
  }];
}

RCT_EXPORT_METHOD(openLoadedAdLink)
{
  if (link) [link openLink];
}

RCT_EXPORT_METHOD(openAdLink)
{
  [[RevMobAds session] openAdLinkWithDelegate:self];
}

#pragma mark - RevMobAdsDelegate methods

/////Native ads Listeners/////

-(void) revmobUserDidClickOnNative:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobUserDidClickOnNative" body:emptyDict];
}

-(void) revmobNativeDidReceive:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobNativeDidReceive" body:emptyDict];
}

-(void) revmobNativeDidFailWithError:(NSError *)error onPlacement:(NSString *)placementId
{
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobNativeDidFailWithError"
                                               body:@{@"error": [error localizedDescription],
                                                      @"placementId": placementId ? placementId : [NSNull null]
                                                      }];
}

/////Fullscreen Listeners/////

-(void) revmobUserDidClickOnFullscreen:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobUserDidClickOnFullscreen" body:emptyDict];
}

-(void) revmobFullscreenDidReceive:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobFullscreenDidReceive" body:emptyDict];
}

-(void) revmobFullscreenDidFailWithError:(NSError *)error onPlacement:(NSString *)placementId
{
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobFullscreenDidFailWithError" body:@{@"error": [error localizedDescription],
                                                                                                 @"placementId": placementId ? placementId : [NSNull null]
                                                                                                 }];
}

-(void) revmobFullscreenDidDisplay:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobFullscreenDidDisplay" body:emptyDict];
}

-(void) revmobUserDidCloseFullscreen:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobUserDidCloseFullscreen" body:emptyDict];
}

///Banner Listeners///

-(void) revmobUserDidClickOnBanner:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobUserDidClickOnBanner" body:emptyDict];
}

-(void) revmobBannerDidReceive:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobBannerDidReceive" body:emptyDict];
}

-(void) revmobBannerDidFailWithError:(NSError *)error onPlacement:(NSString *)placementId
{
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobBannerDidFailWithError" body:@{@"error": [error localizedDescription],
                                                                                             @"placementId": placementId ? placementId : [NSNull null]
                                                                                             }];
}

-(void) revmobBannerDidDisplay:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobBannerDidDisplay" body:emptyDict];
}

/////Video Listeners/////
-(void)revmobVideoDidLoad:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobVideoDidLoad" body:emptyDict];
}

-(void) revmobVideoDidFailWithError:(NSError *)error onPlacement:(NSString *)placementId
{
    [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobVideoDidFailWithError" body:@{@"error": [error localizedDescription],
                                                                                             @"placementId": placementId ? placementId : [NSNull null]
                                                                                             }];
}

-(void)revmobVideoNotCompletelyLoaded:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobVideoNotCompletelyLoaded" body:emptyDict];
}

-(void)revmobVideoDidStart:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobVideoDidStart" body:emptyDict];
}

-(void)revmobVideoDidFinish:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobVideoDidFinish" body:emptyDict];
}

-(void) revmobUserDidCloseVideo:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobUserDidCloseVideo" body:emptyDict];
}

-(void) revmobUserDidClickOnVideo:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobUserDidClickOnVideo" body:emptyDict];
}


/////Rewarded Video Listeners/////
-(void)revmobRewardedVideoDidLoad:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobRewardedVideoDidLoad" body:emptyDict];
}

-(void)revmobRewardedVideoDidFailWithError:(NSError *)error onPlacement:(NSString *)placementId {
    [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobRewardedVideoDidFailWithError" body:@{@"error": [error localizedDescription],
                                                                                             @"placementId": placementId ? placementId : [NSNull null]
                                                                                             }];
}

-(void)revmobRewardedVideoNotCompletelyLoaded:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobRewardedVideoNotCompletelyLoaded" body:emptyDict];
}

-(void)revmobRewardedVideoDidStart:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobRewardedVideoDidStart" body:emptyDict];
}

-(void)revmobRewardedVideoDidComplete:(NSString *)placementId
{
  NSDictionary * emptyDict =[NSDictionary dictionary];
  [self.bridge.eventDispatcher sendAppEventWithName:@"onRevmobRewardedVideoDidComplete" body:emptyDict];
}

#pragma mark - Others

@end
