
# react-native-phone-call-recorder

## Getting started

`$ npm install react-native-phone-call-recorder --save`

### Mostly automatic installation

`$ react-native link react-native-phone-call-recorder`

### Manual installation


#### iOS (not implemented now)

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-phone-call-recorder` and add `RNPhoneCallRecorder.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNPhoneCallRecorder.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNPhoneCallRecorderPackage;` to the imports at the top of the file
  - Add `new RNPhoneCallRecorderPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-phone-call-recorder'
  	project(':react-native-phone-call-recorder').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-phone-call-recorder/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-phone-call-recorder')
  	```

## Usage
```javascript
import RNPhoneCallRecorder from 'react-native-phone-call-recorder';

// TODO: What to do with the module?
RNPhoneCallRecorder;
```
