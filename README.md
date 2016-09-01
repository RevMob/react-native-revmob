# RevMob React-Native package
### Overview

It takes 3 simple steps to start monetizing:

1. Add your app to our [system](https://www.revmobmobileadnetwork.com/medias) and get your Media Id
2. Add the package to your project
3. Implement Start Session and Ad Unit code

You can find more details about RevMob's package integration at [Official RevMob documentation](http://www.revmob.com/sdk/react_native)

###Requirements
*react-native 0.25.0 or newer*
#### To add the plugin to your existing React Native project:
#### Using [rnpm](https://github.com/rnpm/rnpm):
1. Using your terminal, go to your project directory and enter the command: `npm install --save react-native-revmob`
2. `react-native link`
3. Add the file `RevMobAds.framework` that is included inside the ios folder (`yourProject/node_modules/react-native-revmob/ios`) to your project.
4. *(Optional)* Add RevMob's Fullscreen Activity to your `AndroidManifest.xml`:
```xml
<activity android:name="com.revmob.FullscreenActivity"
            android:theme="@android:style/Theme.Translucent"
            android:configChanges="keyboardHidden|orientation">
</activity>
```

#### Manually
##### iOS
1. `npm i react-native-revmob -S`
2. Add the file `RevMobAds.framework` that is included inside the ios folder (`yourProject/node_modules/react-native-revmob/ios`) to your project.
3. Add `react-native-revmob` static library to your Xcode project: [Official React Native guide](http://facebook.github.io/react-native/docs/linking-libraries-ios.html#manual-linking) (Step 3 not needed).

##### Android
1. `npm i react-native-admob -S`
2. Add the following lines to the given files:

**android/settings.gradle**

```
include ':react-native-revmob'
project(':react-native-revmob').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-revmob/android')
```

**android/app/build.gradle**

```
dependencies {
   ...
   compile project(':react-native-revmob')
}
```

**MainActivity.java**

```java
import com.rctrevmob.RevMobPackage; // Add this import
...

	protected List<ReactPackage> getPackages() {
        return Arrays.<ReactPackage>asList(
            new MainReactPackage(),
            new RevMobPackage() // Add this line
        );
    }

```

---
### Sample App
For more information, see the example project: [Sample App](/SampleApp)


### Basic Usage

```javascript
import { RevMobManager } from 'react-native-revmob';

// Methods:
RevMobManager.startSession('YOUR-MEDIA-ID', callback);
RevMobManager.loadFullscreen();
RevMobManager.showPreLoadedFullscreen();
RevMobManager.showBanner();
RevMobManager.hideBanner();
RevMobManager.loadVideo();
RevMobManager.showVideo();
RevMobManager.loadRewardedVideo();
RevMobManager.showRewardedVideo();
RevMobManager.loadAdLink();
RevMobManager.openLoadedAdLink();

```

For more information, please refer to the [official documentation](http://www.revmob.com/sdk/react_native)

###Forum
-------
Be sure to also join the developer community on
[our forum](http://forum.revmobmobileadnetwork.com/).

###License
-------
[License](/LICENSE)
