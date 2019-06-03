
package com.kuhulin;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.util.ArrayList;

public class RNPhoneCallRecorderModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  RecordCallModule recordCallModule = null;

  public RNPhoneCallRecorderModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    this.recordCallModule = new RecordCallModule(reactContext);
  }

  @Override
  public String getName() {
    return "RNPhoneCallRecorder";
  }

  @ReactMethod
  public void addPhonesWhiteList(ReadableArray phoneWhileList, Promise promise) {
    if (this.recordCallModule == null) {
      promise.reject("Error: Set phone whitelist", "recordCallModule is null");
    } else {
      for (int i = 0; i < phoneWhileList.size(); i++) {
        ReadableType type = phoneWhileList.getType(i);
        if (type.name() != "String") {
          promise.reject("Phone type", "Phone with index " + i + " has " + type.name() + " type!");
        } else {
          this.recordCallModule.whitelist.add(phoneWhileList.getString(i));
        }
      }
      promise.resolve(Arguments.createMap());
    }
  }

  @ReactMethod
  public void clearWhitelist() {
    this.recordCallModule.whitelist = new ArrayList<String>();
  }

  @ReactMethod
  public void getWhiteList(Promise promise) {
    WritableArray wl = Arguments.createArray();
    for (int i = 0; i < this.recordCallModule.whitelist.size(); i++) {
      wl.pushString(this.recordCallModule.whitelist.get(i));
    }
    WritableMap map = Arguments.createMap();
    map.putArray("whitelist", wl);
    promise.resolve(map);
  }

  @ReactMethod
  public void deletePhoneWhiteList(String number, Promise promise) {
    if (this.recordCallModule.whitelist.contains(number)) {
      this.recordCallModule.whitelist.remove(number);
      promise.resolve(this.recordCallModule.whitelist);
    } else {
      promise.reject("error", "whitelist not contain number " + number);
    }
  }

  @ReactMethod
  public void addPhonesBlackList(ReadableArray phoneBlackList, Promise promise) {
    if (this.recordCallModule == null) {
      promise.reject("Error: Set phone whitelist", "recordCallModule is null");
    } else {
      for (int i = 0; i < phoneBlackList.size(); i++) {
        ReadableType type = phoneBlackList.getType(i);
        if (type.name() != "String") {
          promise.reject("Phone type", "Phone with index " + i + " has " + type.name() + " type!");
        } else {
          this.recordCallModule.blacklist.add(phoneBlackList.getString(i));
        }
      }
      promise.resolve(Arguments.createMap());
    }
  }

  @ReactMethod
  public void clearBlackList() {
    this.recordCallModule.blacklist = new ArrayList<String>();
  }

  @ReactMethod
  public void deletePhoneBlackList(String number, Promise promise) {
    if (this.recordCallModule.blacklist.contains(number)) {
      this.recordCallModule.blacklist.remove(number);
      promise.resolve(this.recordCallModule.blacklist);
    } else {
      promise.reject("error", "whitelist not contain number " + number);
    }
  }

  @ReactMethod
  public void getBlackList(Promise promise) {
    WritableArray wl = Arguments.createArray();
    for (int i = 0; i < this.recordCallModule.blacklist.size(); i++) {
      wl.pushString(this.recordCallModule.blacklist.get(i));
    }
    WritableMap map = Arguments.createMap();
    map.putArray("blacklist", wl);
    promise.resolve(map);
  }

  @ReactMethod
  public void switchRecordStatus(Boolean status) {
    this.recordCallModule.isRecord = status;
  }
}
