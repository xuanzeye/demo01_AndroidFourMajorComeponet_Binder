package com.example.demo01_androidfourmajorcomeponet_binder.receiver;

import static androidx.core.content.ContextCompat.registerReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

public class DynamicBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "DynamicBroadcast";

    public DynamicBroadcastReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)){
            Toast.makeText(context,"动态注册的广播接收器接收到了广播",Toast.LENGTH_SHORT).show();
            Log.d(TAG,"动态注册的广播接收器接收到了广播");
        }
    }


}
