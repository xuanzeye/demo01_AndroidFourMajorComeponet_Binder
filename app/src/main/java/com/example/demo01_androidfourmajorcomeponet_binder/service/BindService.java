package com.example.demo01_androidfourmajorcomeponet_binder.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;


public class BindService extends Service {

    private static final String TAG = "BindService";
    private final IBinder mBinder = new LocalBinder();

    public BindService() {

    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() called");
        return mBinder;
    }

    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind() called");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    public String getBindText() {
        return "Xiaomi";
    }

    public class LocalBinder extends Binder {
        public BindService getService() {
            return BindService.this;
        }
    }


}