package com.example.demo01_androidfourmajorcomeponet_binder.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.demo01_androidfourmajorcomeponet_binder.IMyAidlInterface;

public class AidlService extends Service {
    private static final String TAG = "AidlService";

    // 创建一个 AIDL 服务的 Stub 实现
    private final IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {
        @Override
        public int add(int num1, int num2) throws RemoteException {
            Log.d(TAG, "add() called with: num1 = [" + num1 + "], num2 = [" + num2 + "]");
            return num1 + num2;
        }

        @Override
        public String getMessage(String name) throws RemoteException {
            Log.d(TAG, "getMessage() called with: name = [" + name + "]");
            return "Hello, " + name;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() called with: intent = [" + intent + "]");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind() called with: intent = [" + intent + "]");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}

