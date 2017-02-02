#ifndef RevMobManager_h
#define RevMobManager_h

#import <React/RCTBridgeModule.h>
#import <React/RCTViewManager.h>
#import <React/RCTLog.h>
#import <React/RCTEventDispatcher.h>
#import <RevMobAds/RevMobAdsDelegate.h>

@interface RevMobManager : NSObject<RCTBridgeModule, RevMobAdsDelegate>
@end

#endif /* RevMobManager_h */
