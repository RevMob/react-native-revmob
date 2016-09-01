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
  Platform,
  TouchableHighlight,
  Switch
} from 'react-native';

import { RevMobManager } from 'react-native-revmob';
import Toast from 'react-native-root-toast';

import { NativeAppEventEmitter } from 'react-native';
import { NativeModules } from 'react-native';

const TouchableText = (props) => <TouchableHighlight underlayColor="#fff" activeOpacity={0.2} onPress={props.onTextPress}>
        <Text style={styles.welcome}  >
          {props.text}
        </Text>
        </TouchableHighlight>;

class rnrevmob extends Component {
  constructor(props) {
    super(props);
    this.state = {isSessionStarted: false, testModeOn: true}
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
    // Test
    const testMediaId = Platform.OS === 'ios' ? '5695efd659163ac94e52393e' : '5695f1c559163ac94e52394c';
    // Live
    const liveMediaId = Platform.OS === 'ios' ? 'YOUR_IOS_MEDIA_ID' : 'YOUR_ANDROID_MEDIA_ID';

    RevMobManager.startSession( this.state.testModeOn ? testMediaId : liveMediaId ,
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
    const { isSessionStarted, testModeOn } = this.state;
    return (
      <View style={styles.container}>
        
        <Text style={[styles.listener, {color: isSessionStarted ? 'green' : 'red'}]}>
          {isSessionStarted ? 'Session Started' : 'Session Not Started'}
        </Text>
        <View style={{flexDirection: 'row'}} >
          <Text>
          Test mode:
          </Text>
          <Switch value={testModeOn}
            onValueChange={(value) => this.setState({testModeOn: value})}
            disabled={isSessionStarted}
            />
        </View>

        <ScrollView style={styles.scrollView}>
          <TouchableText onTextPress={this.startSession} text="Start Session"/>

          <TouchableText onTextPress={ () => RevMobManager.loadFullscreen() } text="Load Fullscreen"/>
          <TouchableText onTextPress={ () => RevMobManager.showPreLoadedFullscreen() } text="Show Pre Loaded Fullscreen"/>
          <TouchableText onTextPress={ () => RevMobManager.showFullscreen() } text="Show Fullscreen"/>
          <TouchableText onTextPress={ () => RevMobManager.loadCustomBanner(0, 200, 100, 100) } text="Load Custom Banner"/>
          <TouchableText onTextPress={ () => RevMobManager.showCustomBanner() } text="Show Custom Banner"/>
          <TouchableText onTextPress={ () => RevMobManager.hideCustomBanner() } text="Hide Custom Banner"/>
          <TouchableText onTextPress={ () => RevMobManager.releaseCustomBanner() } text="Release Custom Banner"/>
          <TouchableText onTextPress={ () => RevMobManager.loadBanner() } text="Load Banner"/>
          <TouchableText onTextPress={ () => RevMobManager.showBanner() } text="Show Banner"/>
          <TouchableText onTextPress={ () => RevMobManager.hideBanner() } text="Hide Banner"/>
          <TouchableText onTextPress={ () => RevMobManager.releaseBanner() } text="Release Banner"/>
          <TouchableText onTextPress={ () => RevMobManager.loadVideo() } text="Load Video"/>
          <TouchableText onTextPress={ () => RevMobManager.showVideo() } text="Show Video"/>
          <TouchableText onTextPress={ () => RevMobManager.loadRewardedVideo() } text="Load Rewarded Video"/>
          <TouchableText onTextPress={ () => RevMobManager.showRewardedVideo() } text="Show Rewarded Video"/>
          <TouchableText onTextPress={ () => RevMobManager.loadAdLink() } text="Load Link"/>
          <TouchableText onTextPress={ () => RevMobManager.openLoadedAdLink() } text="Open Loaded Link"/>
          <TouchableText onTextPress={ () => RevMobManager.openAdLink() } text="Open Ad Link"/>
          <TouchableText onTextPress={ () => RevMobManager.printEnvironmentInformation() } text="Print Environment Information"/>
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
