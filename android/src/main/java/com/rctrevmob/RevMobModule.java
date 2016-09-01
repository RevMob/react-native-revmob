package com.rctrevmob;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.widget.RelativeLayout;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;
import com.revmob.RevMob;
import com.revmob.RevMobAdsListener;
import com.revmob.ads.banner.RevMobBanner;
import com.revmob.ads.interstitial.RevMobFullscreen;
import com.revmob.ads.link.RevMobLink;

import javax.annotation.Nullable;

public class RevMobModule extends ReactContextBaseJavaModule{

    private RevMob revmob;
    private RevMobFullscreen fullscreen, video, rewardedVideo;
    private RevMobLink link;
    private RevMobBanner banner, customBanner;
    private RelativeLayout.LayoutParams customBannerParams, bannerParams;
    private RelativeLayout customBannerRelativeLayout, bannerRelativeLayout;
    private Activity bannerActivity, customBannerActivity;
    private final String LOG_TAG = "RevMobModule";
    private final String SESSION_NOT_STARTED_MSG = "Session has not been started. Call the startSession method.";

    public RevMobModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RevMobManager";
    }

    @ReactMethod
    public void startSession(final String appId, final Callback callback){
        final Activity activity = getCurrentActivity();
        Log.d("revmob", appId);
        if(activity != null) {
            revmob = RevMob.startWithListenerForWrapper(activity, appId, new RevMobAdsListener(){
                @Override
                public void onRevMobSessionStarted() {
                    Log.d("RevMobContext", "Session started.");
                    sendEvent(getReactApplicationContext(), "onRevmobSessionIsStarted", null);
                    callback.invoke();
                }

                @Override
                public void onRevMobSessionNotStarted(String error) {
                    WritableMap params = Arguments.createMap();
                    params.putString("error", error);
                    Log.d("RevMobContext", "Session failed to start.");
                    sendEvent(getReactApplicationContext(), "onRevmobSessionNotStarted", params);
                    callback.invoke(error);
                }
            });
        }
    }

    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext
                .getJSModule(RCTNativeAppEventEmitter.class)
                .emit(eventName, params);

    }

    @ReactMethod
    public void loadFullscreen() {
        final ReactContext reactContext = getReactApplicationContext();
        Activity activity = getCurrentActivity();
        WritableMap sessionNotStarted = Arguments.createMap();
        sessionNotStarted.putString("error", SESSION_NOT_STARTED_MSG);
        if (revmob != null && activity != null) {
            fullscreen = revmob.createFullscreen(activity, new RevMobAdsListener(){
                @Override
                public void onRevMobAdClicked() {
                    sendEvent(reactContext, "onRevmobUserDidClickOnFullscreen", null);
                }

                @Override
                public void onRevMobAdReceived() {
                    sendEvent(reactContext, "onRevmobFullscreenDidReceive", null);
                }

                @Override
                public void onRevMobAdNotReceived(String s) {
                    WritableMap params = Arguments.createMap();
                    params.putString("error", s);
                    sendEvent(reactContext, "onRevmobFullscreenDidFailWithError", params);
                }

                @Override
                public void onRevMobAdDisplayed() {
                    sendEvent(reactContext, "onRevmobFullscreenDidDisplay", null);
                }

                @Override
                public void onRevMobAdDismissed() {
                    sendEvent(reactContext, "onRevmobUserDidCloseFullscreen", null);
                }
            });
        } else {
            sendEvent(reactContext, "onRevmobFullscreenDidFailWithError", sessionNotStarted);
        }
    }

    @ReactMethod
    public void showPreLoadedFullscreen() {
        if (fullscreen != null) {
            fullscreen.show();
        }
    }

    @ReactMethod
    public void showFullscreen() {
        final ReactContext reactContext = getReactApplicationContext();
        Activity activity = getCurrentActivity();
        WritableMap sessionNotStarted = Arguments.createMap();
        sessionNotStarted.putString("error", SESSION_NOT_STARTED_MSG);
        if (revmob != null && activity != null) {
            revmob.showFullscreen(activity, new RevMobAdsListener(){
                @Override
                public void onRevMobAdClicked() {
                    sendEvent(reactContext, "onRevmobUserDidClickOnFullscreen", null);
                }

                @Override
                public void onRevMobAdReceived() {
                    sendEvent(reactContext, "onRevmobFullscreenDidReceive", null);
                }

                @Override
                public void onRevMobAdNotReceived(String s) {
                    WritableMap params = Arguments.createMap();
                    params.putString("error", s);
                    sendEvent(reactContext, "onRevmobFullscreenDidFailWithError", params);
                }

                @Override
                public void onRevMobAdDisplayed() {
                    sendEvent(reactContext, "onRevmobFullscreenDidDisplay", null);
                }

                @Override
                public void onRevMobAdDismissed() {
                    sendEvent(reactContext, "onRevmobUserDidCloseFullscreen", null);
                }
            });
        } else {
            sendEvent(reactContext, "onRevmobFullscreenDidFailWithError", sessionNotStarted);
        }
    }
    @ReactMethod
    public void showCustomBanner () {
        if (customBanner != null && customBannerActivity != null) {
            customBannerActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    customBanner.show();
                }
            });
        }
    }

    @ReactMethod
    public void loadCustomBanner(final int x, final int y, final int width, final int height) {
        final ReactContext reactContext = getReactApplicationContext();
        final Activity activity = getCurrentActivity();
        WritableMap sessionNotStarted = Arguments.createMap();
        sessionNotStarted.putString("error", SESSION_NOT_STARTED_MSG);
        if (activity != null && revmob != null) {
            // If custom banner is null, create a new one
            customBannerActivity = activity;
            if(customBanner == null) {
                customBanner = revmob.preLoadBanner(activity, null, new RevMobAdsListener(){
                    @Override
                    public void onRevMobAdClicked() {
                        sendEvent(reactContext, "onRevmobUserDidClickOnBanner", null);
                    }

                    @Override
                    public void onRevMobAdDisplayed() {
                        sendEvent(reactContext, "onRevmobBannerDidDisplay", null);
                    }

                    @Override
                    public void onRevMobAdNotReceived(String s) {
                        WritableMap params = Arguments.createMap();
                        params.putString("error", s);
                        sendEvent(reactContext, "onRevmobBannerDidFailWithError", params);
                    }

                    @Override
                    public void onRevMobAdReceived() {
                        sendEvent(reactContext, "onRevmobBannerDidReceive", null);
                    }
                });
                customBannerRelativeLayout = new RelativeLayout(getReactApplicationContext());
                customBannerParams = new RelativeLayout.LayoutParams(width, height);
                customBannerParams.leftMargin = x;
                customBannerParams.bottomMargin = y;
                customBannerRelativeLayout.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                customBannerRelativeLayout.addView(customBanner, customBannerParams);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.addContentView(customBannerRelativeLayout, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
                    }
                });
            }
        } else {
            sendEvent(reactContext, "onRevmobBannerDidFailWithError", sessionNotStarted);
        }
    }

    @ReactMethod
    public void hideCustomBanner() {
        if (customBanner != null && customBannerActivity != null) {
            customBannerActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    customBanner.hide();
                }
            });
        }
    }

    @ReactMethod
    public void releaseCustomBanner () {
        if (customBanner != null && customBannerActivity != null) {
            customBannerActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    customBannerRelativeLayout.removeAllViews();
                    customBanner.release();
                    customBanner = null;
                    customBannerRelativeLayout = null;
                }
            });
        }
    }

    @ReactMethod
    public void loadBanner () {
        final ReactContext reactContext = getReactApplicationContext();
        final Activity activity = getCurrentActivity();
        WritableMap sessionNotStarted = Arguments.createMap();
        final int displayWidth = getCurrentActivity().getWindowManager().getDefaultDisplay().getWidth();
        final int displayHeight = getCurrentActivity().getWindowManager().getDefaultDisplay().getHeight();
        sessionNotStarted.putString("error", SESSION_NOT_STARTED_MSG);
        if (activity != null && revmob != null) {
            bannerActivity = activity;
            if ( banner == null ) {
                bannerActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    banner = revmob.preLoadBanner(activity, new RevMobAdsListener(){
                        @Override
                        public void onRevMobAdClicked() {
                            sendEvent(reactContext, "onRevmobUserDidClickOnBanner", null);
                        }

                        @Override
                        public void onRevMobAdDisplayed() {
                            sendEvent(reactContext, "onRevmobBannerDidDisplay", null);
                        }

                        @Override
                        public void onRevMobAdNotReceived(String s) {
                            WritableMap params = Arguments.createMap();
                            params.putString("error", s);
                            sendEvent(reactContext, "onRevmobBannerDidFailWithError", params);
                        }

                        @Override
                        public void onRevMobAdReceived() {
                            sendEvent(reactContext, "onRevmobBannerDidReceive", null);
                        }
                    });
                    banner.determineDefaultDimensions();
                    float width = Math.min(displayWidth,displayHeight);
                    float height = width * RevMobBanner.DEFAULT_HEIGHT_IN_DIP/RevMobBanner.DEFAULT_WIDTH_IN_DIP;
                    bannerRelativeLayout = new RelativeLayout(getCurrentActivity().getApplicationContext());
                    bannerParams = new RelativeLayout.LayoutParams((int)width, (int)height);
                    bannerRelativeLayout.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                    bannerActivity.addContentView(bannerRelativeLayout, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT));
                    bannerRelativeLayout.addView(banner, bannerParams);
                }
                });
            }
        } else {
            sendEvent(reactContext, "onRevmobBannerDidFailWithError", sessionNotStarted);
        }
    }

    @ReactMethod
    public void showBanner() {
        final Activity activity = getCurrentActivity();
        if(banner != null && bannerActivity != null) {
            bannerActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    banner.hide();
                    banner.show();
                }
            });
        }
    }

    @ReactMethod
    public void hideBanner() {
        if(banner != null && bannerActivity != null) {
            bannerActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    banner.hide();
                }
            });
        }
    }

    @ReactMethod 
    public void releaseBanner() {
        if(banner != null && bannerActivity != null) {
            bannerActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    banner.release();
                    banner = null;
                }
            });
        }
    }

    @ReactMethod
    public void loadVideo() {
        final ReactContext reactContext = getReactApplicationContext();
        Activity activity = getCurrentActivity();
        WritableMap sessionNotStarted = Arguments.createMap();
        sessionNotStarted.putString("error", SESSION_NOT_STARTED_MSG);
        if (revmob != null && activity != null) {
            video = revmob.createVideo(activity, new RevMobAdsListener(){
                @Override
                public void onRevMobVideoLoaded() {
                    Log.d(LOG_TAG, "video onRevMobVideoLoaded");
                    sendEvent(reactContext, "onRevmobVideoDidLoad", null);
                }

                @Override
                public void onRevMobAdNotReceived(String s) {
                    Log.d(LOG_TAG, "video onRevMobAdNotReceived");
                    WritableMap params = Arguments.createMap();
                    params.putString("error", s);
                    sendEvent(reactContext, "onRevmobVideoDidFailWithError", params);
                }

                @Override
                public void onRevMobVideoNotCompletelyLoaded() {
                    Log.d(LOG_TAG, "video onRevMobVideoNotCompletelyLoaded");
                    sendEvent(reactContext, "onRevmobVideoNotCompletelyLoaded", null);
                }

                @Override
                public void onRevMobAdDismissed() {
                    Log.d(LOG_TAG, "video onRevMobAdDismissed");
                    sendEvent(reactContext, "onRevmobUserDidCloseVideo", null);
                }

                @Override
                public void onRevMobVideoStarted() {
                    Log.d(LOG_TAG, "video onRevMobVideoStarted");
                    sendEvent(reactContext, "onRevmobVideoDidStart", null);
                }

                @Override
                public void onRevMobVideoFinished() {
                    Log.d(LOG_TAG, "video onRevMobVideoFinished");
                    sendEvent(reactContext, "onRevmobVideoDidFinish", null);
                }

                @Override
                public void onRevMobAdClicked() {
                    Log.d(LOG_TAG, "video onRevMobAdClicked");
                    sendEvent(reactContext, "onRevmobUserDidClickOnVideo", null);
                }

            });
        } else {
            sendEvent(reactContext, "onRevmobVideoDidFailWithError", sessionNotStarted);
        }
    }

    @ReactMethod
    public void showVideo() {
        if (video != null) {
            video.showVideo();
        }
    }

    @ReactMethod
    public void loadRewardedVideo() {
        final ReactContext reactContext = getReactApplicationContext();
        Activity activity = getCurrentActivity();
        WritableMap sessionNotStarted = Arguments.createMap();
        sessionNotStarted.putString("error", SESSION_NOT_STARTED_MSG);
        if (revmob != null && activity != null) {
            rewardedVideo = revmob.createRewardedVideo(activity, new RevMobAdsListener(){
                @Override
                public void onRevMobRewardedVideoCompleted() {
                    sendEvent(reactContext, "onRevmobRewardedVideoDidComplete", null);
                }

                @Override
                public void onRevMobRewardedVideoLoaded() {
                    sendEvent(reactContext, "onRevmobRewardedVideoDidLoad", null);
                }

                @Override
                public void onRevMobRewardedVideoNotCompletelyLoaded() {
                    sendEvent(reactContext, "onRevmobRewardedVideoNotCompletelyLoaded", null);
                }

                @Override
                public void onRevMobRewardedVideoStarted() {
                    sendEvent(reactContext, "onRevmobRewardedVideoDidStart", null);
                }

                @Override
                public void onRevMobAdNotReceived(String s) {
                    Log.d(LOG_TAG, "video onRevMobAdNotReceived");
                    WritableMap params = Arguments.createMap();
                    params.putString("error", s);
                    sendEvent(reactContext, "onRevmobRewardedVideoDidFailWithError", params);
                }


            });
        } else {
            sendEvent(reactContext, "onRevmobRewardedVideoDidFailWithError", sessionNotStarted);
        }

    }

    @ReactMethod
    public void showRewardedVideo() {
        if (rewardedVideo != null) {
            rewardedVideo.showRewardedVideo();
        }
    }

    @ReactMethod
    public void loadAdLink() {
        final ReactContext reactContext = getReactApplicationContext();
        Activity activity = getCurrentActivity();
        WritableMap sessionNotStarted = Arguments.createMap();
        sessionNotStarted.putString("error", SESSION_NOT_STARTED_MSG);
        if (activity != null && revmob != null) {
            link = revmob.createLink(activity, new RevMobAdsListener(){
                @Override
                public void onRevMobAdClicked() {
                    sendEvent(reactContext, "onRevmobUserDidClickOnNative", null);
                }

                @Override
                public void onRevMobAdReceived() {
                    sendEvent(reactContext, "onRevmobNativeDidReceive", null);
                }

                @Override
                public void onRevMobAdNotReceived(String s) {
                    WritableMap params = Arguments.createMap();
                    params.putString("error", s);
                    sendEvent(reactContext, "onRevmobNativeDidFailWithError", params);
                }
            });
        } else {
            sendEvent(reactContext, "onRevmobNativeDidFailWithError", sessionNotStarted);
        }

    }

    @ReactMethod
    public void openLoadedAdLink() {
        if (link != null) {
            link.open();
        }
    }

    @ReactMethod
    public void openAdLink() {
        final ReactContext reactContext = getReactApplicationContext();
        Activity activity = getCurrentActivity();
        WritableMap sessionNotStarted = Arguments.createMap();
        sessionNotStarted.putString("error", SESSION_NOT_STARTED_MSG);
        if (activity != null && revmob != null) {
            revmob.openLink(activity, new RevMobAdsListener(){
                @Override
                public void onRevMobAdClicked() {
                    sendEvent(reactContext, "onRevmobUserDidClickOnNative", null);
                }

                @Override
                public void onRevMobAdReceived() {
                    sendEvent(reactContext, "onRevmobNativeDidReceive", null);
                }

                @Override
                public void onRevMobAdNotReceived(String s) {
                    WritableMap params = Arguments.createMap();
                    params.putString("error", s);
                    sendEvent(reactContext, "onRevmobNativeDidFailWithError", params);
                }
            });
        } else {
            sendEvent(reactContext, "onRevmobNativeDidFailWithError", sessionNotStarted);
        }
    }

    @ReactMethod
    public void printEnvironmentInformation() {
        Activity activity = getCurrentActivity();
        if (activity != null && revmob != null) revmob.printEnvironmentInformation(activity);
    }

    @ReactMethod
    public void setUserAgeRangeMin (int userAgeRangeMin) {
        if (revmob != null) {
            revmob.setUserAgeRangeMin(userAgeRangeMin);
        }
    }

}
