#ifndef RevMobManager_h
#define RevMobManager_h

#import "RCTBridgeModule.h"
#import "RCTViewManager.h"
#import "RCTLog.h"
#import "RCTEventDispatcher.h"
#import <RevMobAds/RevMobAdsDelegate.h>

@interface RevMobManager : NSObject<RCTBridgeModule, RevMobAdsDelegate>
@end

#endif /* RevMobManager_h */
