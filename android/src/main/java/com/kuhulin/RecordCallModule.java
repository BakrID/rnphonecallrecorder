package com.kuhulin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.kuhulin.service.PhonecallReceiver;
import com.kuhulin.service.RecordService;

import java.util.ArrayList;
import java.util.Date;

public class RecordCallModule extends PhonecallReceiver {

    private static RecordService recordService = null;
    private static ReactApplicationContext reactContext = null;
    public static ArrayList<String> whitelist = new ArrayList<String>();
    public static ArrayList<String> blacklist = new ArrayList<String>();
    public static Boolean isRecord = false;

    public RecordCallModule() {
        super();
    }

    RecordCallModule(ReactApplicationContext context) {
        super();
        reactContext = context;
    }

    private void startRecord(String name, Date start) {
        recordService = new RecordService();
        recordService.setFileName(name + start.toString() + ".wav");
        recordService.setPath(reactContext.getFilesDir().getPath());
        recordService.startRecord();
    }

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start)
    {
        Log.i("CALL_RECORDER", "RECEIVED" + number);
        WritableMap params = Arguments.createMap();
        params.putString("number", number);
        params.putString("type", "INCOMING_RECEIVED" + whitelist.size() + " " + blacklist.size());
        emitDeviceEvent("onIncomingCallReceived", params);
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start)
    {
        Log.i("CALL_RECORDER", "ANSWERED" + whitelist.size() + " " + blacklist.size());
        if (isRecord == false) {
            WritableMap params = Arguments.createMap();
            params.putString("number", number);
            params.putString("type", "INCOMING_ANSWERED");
            params.putString("reason", "Record is disabled");
            emitDeviceEvent("onBlockRecordPhoneCall", params);
        } else {
            if (whitelist.isEmpty() && blacklist.isEmpty()) {
                startRecord("record-incoming-", start);
            } else if (whitelist.size() > 0 && blacklist.size() == 0) {
                if (whitelist.contains(number)) {
                    startRecord("record-incoming-", start);
                } else {
                    WritableMap params = Arguments.createMap();
                    params.putString("number", number);
                    params.putString("type", "INCOMING_ANSWERED");
                    params.putString("reason", "This phone is not exist in white list");
                    emitDeviceEvent("onBlockRecordPhoneCall", params);
                }
            } else if (blacklist.size() > 0 && whitelist.size() == 0) {
                if (blacklist.contains(number)) {
                    WritableMap params = Arguments.createMap();
                    params.putString("number", number);
                    params.putString("type", "INCOMING_ANSWERED");
                    params.putString("reason", "This phone in black list");
                    emitDeviceEvent("onBlockRecordPhoneCall", params);
                } else {
                    startRecord("record-incoming-", start);
                }
            } else {
                WritableMap params = Arguments.createMap();
                params.putString("number", number);
                params.putString("type", "INCOMING_ANSWERED");
                params.putString("reason", "Use whitelist or blacklist");
                emitDeviceEvent("onBlockRecordPhoneCall", params);
            }
        }
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
    {
        if (recordService != null) {
            String path = recordService.stopRecord();
            WritableMap params = Arguments.createMap();
            params.putString("filePath", path);
            params.putString("number", number);
            params.putString("start", start.toString());
            params.putString("end", end.toString());
            emitDeviceEvent("onIncomingCallRecorded", params);
        }
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start)
    {
        if (isRecord == false) {
            WritableMap params = Arguments.createMap();
            params.putString("number", number);
            params.putString("type", "OUTGOING_ANSWERED");
            params.putString("reason", "Record is disabled");
            emitDeviceEvent("onBlockRecordPhoneCall", params);
        } else {
            if (whitelist.isEmpty() && blacklist.isEmpty()) {
                startRecord("record-outgoing-", start);
            } else if (whitelist.size() > 0 && blacklist.size() == 0) {
                if (whitelist.contains(number)) {
                    startRecord("record-outgoing-", start);
                } else {
                    WritableMap params = Arguments.createMap();
                    params.putString("number", number);
                    params.putString("type", "OUTGOING_ANSWERED");
                    params.putString("reason", "This phone is not exist in white list");
                    emitDeviceEvent("onBlockRecordPhoneCall", params);
                }
            } else if (blacklist.size() > 0 && whitelist.size() == 0) {
                if (blacklist.contains(number)) {
                    WritableMap params = Arguments.createMap();
                    params.putString("number", number);
                    params.putString("type", "OUTGOING_ANSWERED");
                    params.putString("reason", "This phone in black list");
                    emitDeviceEvent("onBlockRecordPhoneCall", params);
                } else {
                    startRecord("record-outgoing-", start);
                }
            } else {
                WritableMap params = Arguments.createMap();
                params.putString("number", number);
                params.putString("type", "OUTGOING_ANSWERED");
                params.putString("reason", "Use whitelist or blacklist");
                emitDeviceEvent("onBlockRecordPhoneCall", params);
            }
        }
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end)
    {
        if (recordService != null) {
            String path = recordService.stopRecord();
            WritableMap params = Arguments.createMap();
            params.putString("filePath", path);
            params.putString("number", number);
            params.putString("start", start.toString());
            params.putString("end", end.toString());
            emitDeviceEvent("onOutgoingCallRecorded", params);
        }
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start)
    {
        WritableMap params = Arguments.createMap();
        params.putString("number", number);
        params.putString("date", start.toString());
        emitDeviceEvent("onMissedCall", params);
    }

    private static void emitDeviceEvent(String eventName, @Nullable WritableMap eventData) {
        // A method for emitting from the native side to JS
        // https://facebook.github.io/react-native/docs/native-modules-android.html#sending-events-to-javascript
        Log.i("CALL_START", reactContext.getPackageCodePath());
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, eventData);
    }

}
