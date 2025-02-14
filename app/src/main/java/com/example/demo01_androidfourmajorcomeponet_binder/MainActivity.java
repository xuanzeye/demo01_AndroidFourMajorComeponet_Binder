package com.example.demo01_androidfourmajorcomeponet_binder;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demo01_androidfourmajorcomeponet_binder.receiver.DynamicBroadcast;
import com.example.demo01_androidfourmajorcomeponet_binder.receiver.StaticBroadcast;
import com.example.demo01_androidfourmajorcomeponet_binder.service.BindService;
import com.example.demo01_androidfourmajorcomeponet_binder.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {
    private static final String TAG_ActivityLifeCycle = "ActivityLifeCycle";
    private static final String TAG_ServiceBindActivity = "ServiceBindActivity";

    private BindService mBindService;
    private Boolean mBound = false;
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BindService.LocalBinder localBinder = (BindService.LocalBinder) service;
            mBindService = localBinder.getService();
            mBound = true;
            Log.d(TAG_ServiceBindActivity, "onServiceConnected() called with: name = [" + name + "], service = [" + service + "]");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
            Log.d(TAG_ServiceBindActivity, "onServiceDisconnected() called with: name = [" + name + "]");
        }
    };

    private IMyAidlInterface mAidlService;
    private Boolean mAidlBound = false;
    private final ServiceConnection mConnectionAidl = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mAidlService = IMyAidlInterface.Stub.asInterface(service);
            mAidlBound = true;
            Log.d(TAG_ServiceBindActivity, "onServiceConnected() called with: name = [" + name + "], service = [" + service + "]");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAidlBound = false;
            Log.d(TAG_ServiceBindActivity, "onServiceDisconnected() called with: name = [" + name + "]");
        }
    };

    private DynamicBroadcast mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Log.d(TAG_ActivityLifeCycle, "onCreate() called");
        setContentView(R.layout.activity_main);

        // 拨号按钮
        Button dialButton = findViewById(R.id.btn_BroadcastReceiver_dial);
        dialButton.setOnClickListener(v -> {
            Intent dailIntent = new Intent(Intent.ACTION_DIAL);
            dailIntent.setData(Uri.parse("tel:10086"));
            startActivity(dailIntent);
        });

        // 查看联系人按钮
        Button contactButton = findViewById(R.id.btn_BroadcastReceiver_contacts);
        contactButton.setOnClickListener(v -> {
            Intent contactsIntent = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
            startActivity(contactsIntent);
        });

        // 发送短信按钮
        Button smsButton = findViewById(R.id.btn_BroadcastReceiver_sms);
        smsButton.setOnClickListener(v -> {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("sms:10086"));
            smsIntent.putExtra("sms_body", "Hello, I am a sms");
            startActivity(smsIntent);
        });

        // 绑定服务按钮
        Button bindServiceButton = findViewById(R.id.btn_Service_bindService);
        bindServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BindService.class);
                boolean isBound = bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
                Log.d(TAG_ServiceBindActivity, "bindService() called with: isBound = [" + isBound + "]");
            }
        });

        // 解绑服务
        Button unbindServiceButton = findViewById(R.id.btn_Service_unbindService);
        unbindServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) {
                    unbindService(mConnection);
                    mBound = false;
                } else {
                    Log.d(TAG_ServiceBindActivity, "no service to unbind.");
                }
            }
        });

        // 从服务中获取数据按钮
        Button getDataFromServiceButton = findViewById(R.id.btn_BindService_getDataFromService);
        getDataFromServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBound) {
                    return;
                }
                Log.d(TAG_ServiceBindActivity, "getDataFromService() called");
                String bindText = mBindService.getBindText();
                Toast.makeText(MainActivity.this, bindText, Toast.LENGTH_SHORT).show();
                Log.d(TAG_ServiceBindActivity, "getDataFromService() called with: data = [" + bindText + "]");
            }
        });

        // 绑定aidl服务按钮
        Button bindAidlServiceButton = findViewById(R.id.btn_BindService_aidl);
        bindAidlServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent("com.example.demo01_androidfourmajorcomeponet_binder.aidlService");
                serviceIntent.setPackage("com.example.demo01_androidfourmajorcomeponet_binder");
                boolean isBound = bindService(serviceIntent, mConnectionAidl, Service.BIND_AUTO_CREATE);
                Log.d(TAG_ServiceBindActivity, "bindService() called with: isBound = [" + isBound + "]");
            }
        });

        // 解绑aidl服务按钮
        Button unbindAidlServiceButton = findViewById(R.id.btn_BindService_unbindAidl);
        unbindAidlServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAidlBound) {
                    unbindService(mConnectionAidl);
                    mAidlBound = false;
                } else {
                    Log.d(TAG_ServiceBindActivity, "no aidl service to unbind.");
                }
            }
        });

        // 调用aidl服务方法按钮
        Button callAidlMethodButton = findViewById(R.id.btn_BindService_callAidlMethod);
        callAidlMethodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mAidlBound) {
                    Toast.makeText(MainActivity.this, "AIDL service is not bound", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    int result = mAidlService.add(5, 10);
                    String message = mAidlService.getMessage("World");
                    Toast.makeText(MainActivity.this, "Result: " + result + ", Message: " + message, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error calling AIDL service", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        mReceiver = new DynamicBroadcast();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(mReceiver, intentFilter);
//
//        Button dynamicBroadcastButton = findViewById(R.id.DynamicBroadcast_button);
//        dynamicBroadcastButton.setOnClickListener(v -> {
//            Log.d(TAG_ServiceBindActivity, "onClick() called with: v = [" + v + "]");
//        });
//
//        Button startServiceButton = findViewById(R.id.StaticBroadcast_button);
//        startServiceButton.setOnClickListener(v -> {
//            Intent broadcastIntent = new Intent("com.example.demo01_androidfourmajorcomeponet_binder.receiver.StaticBroadcast");
//            sendBroadcast(broadcastIntent);
//            Log.d(TAG_ServiceBindActivity, "onClick() called with: v = [" + v + "]");
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        if (mAidlBound) {
            unbindService(mConnectionAidl);
            mAidlBound = false;
        }
        unregisterReceiver(mReceiver);
    }

}

