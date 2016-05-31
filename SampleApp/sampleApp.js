/**
 * Sample RevMob Native App
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  requireNativeComponent,
  ScrollView,
  Platform
} from 'react-native';

import { RevMobManager } from 'react-native-revmob';
import Toast from 'react-native-root-toast';

import { NativeAppEventEmitter } from 'react-native';
import { NativeModules } from 'react-native';
  
class rnrevmob extends Component {
  constructor(props) {
    super(props);
    this.state = {isSessionStarted: false}
  }

  componentWillMount () {
    function toastShow (message) {
      const toastOptions = {
        duration: 3000,
        position: Toast.positions.BOTTOM,
        shadow: true,
        animation: true,
        hideOnPress: true,
        delay: 0
      };
      Toast.show(message, toastOptions)
    }
    const listenerMap = {
      // Session event listeners
      onRevmobSessionIsStarted: () => toastShow('Start session success.'),
      onRevmobSessionNotStarted: (e) => toastShow('Session not started - error: ' + e.error),

      // Native link event listeners
      onRevmobUserDidClickOnNative: ()=> toastShow('onRevmobUserDidClickOnNative'),
      onRevmobNativeDidReceive: ()=> toastShow('onRevmobNativeDidReceive'),
      onRevmobNativeDidFailWithError: (e)=> toastShow('onRevmobNativeDidFailWithError - error: ' + e.error),

      // Fullscreen ad unit event listeners
      onRevmobUserDidClickOnFullscreen: ()=> toastShow('onRevmobUserDidClickOnFullscreen'),
      onRevmobFullscreenDidReceive: ()=> toastShow('onRevmobFullscreenDidReceive'),
      onRevmobFullscreenDidFailWithError: (e)=> toastShow('onRevmobFullscreenDidFailWithError - error: ' + e.error),
      onRevmobFullscreenDidDisplay: ()=> toastShow('onRevmobFullscreenDidDisplay'),
      onRevmobUserDidCloseFullscreen: ()=> toastShow('onRevmobUserDidCloseFullscreen'),

      // Banner ad unit event listeners
      onRevmobUserDidClickOnBanner: ()=> toastShow('onRevmobUserDidClickOnBanner'),
      onRevmobBannerDidReceive: ()=> toastShow('onRevmobBannerDidReceive'),
      onRevmobBannerDidFailWithError: (e)=> toastShow('onRevmobBannerDidFailWithError - error: ' + e.error),
      onRevmobBannerDidDisplay: (e)=> toastShow('onRevmobBannerDidDisplay'),

      // Video ad unit event listeners
      onRevmobVideoDidLoad: ()=> toastShow('onRevmobVideoDidLoad'),
      onRevmobVideoDidFailWithError: ()=> toastShow('onRevmobVideoDidFailWithError'),
      onRevmobVideoNotCompletelyLoaded: ()=> toastShow('onRevmobVideoNotCompletelyLoaded'),
      onRevmobUserDidCloseVideo: ()=> toastShow('onRevmobUserDidCloseVideo'),
      onRevmobVideoDidStart: ()=> toastShow('onRevmobVideoDidStart'),
      onRevmobVideoDidFinish: ()=> toastShow('onRevmobVideoDidFinish'),
      onRevmobUserDidClickOnVideo: ()=> toastShow('onRevmobUserDidClickOnVideo'),

      // Rewarded Video ad unit event listeners
      onRevmobRewardedVideoDidLoad: ()=> toastShow('onRevmobRewardedVideoDidLoad'),
      onRevmobRewardedVideoDidFailWithError: () => toastShow('onRevmobRewardedVideoDidFailWithError'),
      onRevmobRewardedVideoNotCompletelyLoaded: ()=> toastShow('onRevmobRewardedVideoNotCompletelyLoaded'),
      onRevmobRewardedVideoDidStart: ()=> toastShow('onRevmobRewardedVideoDidStart'),
      onRevmobRewardedVideoDidComplete: ()=> toastShow('onRevmobRewardedVideoDidComplete'),
    };
    Object.getOwnPropertyNames(listenerMap).forEach( (event) => {
      NativeAppEventEmitter.addListener(
        event,
        listenerMap[event]
      );
    });
  }

  startSession = () => {
    Toast.show('Starting session...');
    RevMobManager.startSession( Platform.OS === 'ios' ? '5695efd659163ac94e52393e' : '5695f1c559163ac94e52394c', 
      (error) => {
        console.log(error)
        if(!error) {
          this.setState({isSessionStarted: true})
        } else {
          Toast.show('Start session failed.');
        }
    });
  }

  componentWillUnmount () {
    NativeAppEventEmitter.removeAllListeners()
  }

  render() {
    const { isSessionStarted } = this.state;
    return (
      <View style={styles.container}>

        <Text style={[styles.listener, {color: isSessionStarted ? 'green' : 'red'}]}>
          {isSessionStarted ? 'Session Started' : 'Session Not Started'}
        </Text>
        <ScrollView style={styles.scrollView}>
        
        <Text style={styles.welcome} onPress={this.startSession} >
          Start Session
        </Text>
        <Text style={styles.welcome} onPress={RevMobManager.loadFullscreen} >
          Load Fullscreen
        </Text>
        <Text style={styles.welcome} onPress={RevMobManager.showPreLoadedFullscreen} >
          Show Pre Loaded Fullscreen
        </Text>
        <Text style={styles.welcome} onPress={RevMobManager.showFullscreen} >
          Show Fullscreen
        </Text>
        <Text style={styles.welcome} onPress={RevMobManager.showCustomBanner.bind(null, 0, 200, 100, 200)} >
          Show Custom Banner
        </Text>
        <Text style={styles.welcome} onPress={RevMobManager.hideCustomBanner}>
          Hide Custom Banner
        </Text>
        <Text style={styles.welcome} onPress={RevMobManager.showBanner} >
          Show Banner
        </Text>
        <Text style={styles.welcome} onPress={RevMobManager.hideBanner} >
          Hide Banner
        </Text>
        <Text style={styles.welcome} onPress={RevMobManager.loadVideo} >
          Load Video
        </Text>
        <Text style={styles.welcome} onPress={RevMobManager.showVideo} >
          Show Video
        </Text>
        <Text style={styles.welcome} onPress={RevMobManager.loadRewardedVideo} >
          Load Rewarded Video
        </Text>
        <Text style={styles.welcome} onPress={RevMobManager.showRewardedVideo} >
          Show Rewarded Video
        </Text>
        <Text style={styles.welcome} onPress={RevMobManager.loadAdLink} >
          Load Link
        </Text>
        <Text style={styles.welcome} onPress={RevMobManager.openLoadedAdLink} >
          Open Loaded Link
        </Text>
        <Text style={styles.welcome} onPress={RevMobManager.openAdLink} >
          Open Ad Link
        </Text>
        <Text style={styles.welcome} onPress={RevMobManager.printEnvironmentInformation} >
          Print Environment Information
        </Text>
        </ScrollView>
        
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  listener: {
    fontSize: 20,
    textAlign: 'center',
    marginTop: 20,
    flex: 0.05
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
  scrollView: {
    backgroundColor: '#EBF2F5',
    flex: 0.95,
    borderRadius: 10
  }
});

module.exports = { rnrevmob };
