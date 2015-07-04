package com.gofish.study.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.gofish.study.utils.L;

public class MyService extends Service {
    public static final String TAG = "MyService";

    MyBinder myBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        L.d(TAG, "onCreate() executed");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d(TAG, "onDestroy() executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.d(TAG, "onStartCommand() executed, flags=" + flags + ", startId=" + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        L.d(TAG, "onBind() executed");
        return myBinder;
    }

    public class MyBinder extends Binder{
        public void startDownload(String url) {
            L.d(TAG, "startDownload() executed, cur=" + url);
        }
    }
}
