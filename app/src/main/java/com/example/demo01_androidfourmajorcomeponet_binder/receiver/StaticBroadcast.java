package com.example.demo01_androidfourmajorcomeponet_binder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class StaticBroadcast extends BroadcastReceiver {
    private static final String TAG = "StaticBroadcast";

    public  StaticBroadcast() {

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Received:" + intent.getAction(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");
    }
}
