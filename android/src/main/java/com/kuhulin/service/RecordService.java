package com.kuhulin.service;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

public class RecordService {

    private MediaRecorder mediaRecorder;
    private String fileName;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void startRecord() {
        String file = path + "/" + fileName;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setOutputFile(file);
        mediaRecorder.setAudioEncodingBitRate(64000);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setAudioSamplingRate(16000);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Log.e("ReactNativePhoneCallRecorder", "started: " + file);
        } catch (IOException e) {
            Log.e("ReactNativePhoneCallRecorder", "Microphone is already in use by another app. IOException: " + e.toString());
        } catch (IllegalStateException e) {
            Log.e("ReactNativePhoneCallRecorder", "Microphone is already in use by another app. IllegalStateException: " + e.toString());
        } catch (Exception e) {
            Log.e("ReactNativePhoneCallRecorder", "Something went wrong in start recording. Exception: " + e.toString());
        }
    }

    public String stopRecord() {
        try {
            mediaRecorder.stop();
        } catch (Exception e) {
            mediaRecorder.reset();
            mediaRecorder.release();
            Log.e("stopping_failed", "Stop failed:" + e.toString());
        }

        mediaRecorder.reset();
        mediaRecorder.release();
        return path + "/" + fileName;
    }

}
