package com.example.demo01_androidfourmajorcomeponet_binder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

public class DynamicBroadcast extends BroadcastReceiver {
    private static final String TAG = "DynamicBroadcast";

    public DynamicBroadcast(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)){
            Toast.makeText(context,"动态注册的广播接收器接收到了广播",Toast.LENGTH_SHORT);
            Log.d(TAG,"动态注册的广播接收器接收到了广播");
        }
    }
}
