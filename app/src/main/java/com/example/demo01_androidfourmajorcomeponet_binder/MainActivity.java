package com.example.demo01_androidfourmajorcomeponet_binder;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demo01_androidfourmajorcomeponet_binder.service.BindService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG_ActivityLifeCycle= "ActivityLifeCycle";
    private static final String TAG_ServiceBindActivity = "ServiceBindActivity";
    private static final String TAG_DynamicBroadcastReceiver = "DynamicBroadcastReceiver";
    private static final String TAG_StaticBroadcastReceiver = "StaticBroadcastReceiver";

    private BindService mBindService;
    private Boolean mBound;
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

    //aidl
    private IMyAidlInterface aidlInterface;
    private Context mContext;
    private ServiceConnection mConnectionAidl = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //Log.e(TAG_ActivityLifeCycle, "onServiceConnected() called with: name = [" + name + "], service = [" + service + "]");
            Log.e(TAG_ServiceBindActivity, "aidl");
            aidlInterface = IMyAidlInterface.Stub.asInterface(service);
            try {
                String result = String.valueOf(aidlInterface.add(1, 2));
                Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
                Log.e(TAG_ServiceBindActivity, "onServiceConnected() called with: result = [" + result + "]");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            aidlInterface = null;
        }
    };

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

        //绑定服务按钮
        Button bindServiceButton = findViewById(R.id.btn_Service_bindService);
        bindServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BindService.class);
                bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
            }
        });

        //解绑服务
        Button unbindServiceButton = findViewById(R.id.btn_Service_unbindService);
        unbindServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) {
                    unbindService(mConnection);
                    mBound = false;
                } else {
                    Log.d(TAG_ActivityLifeCycle, "no service to unbind.");
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

        //绑定aidl服务按钮
        Button bindAidlServiceButton = findViewById(R.id.btn_BindService_aidl);
        bindAidlServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent("com.example.demo01_androidfourmajorcomeponent_binder");
                serviceIntent.setPackage("com.example.demo01_androidfourmajorcomeponet_binder");
                bindService(serviceIntent, mConnectionAidl, Service.BIND_AUTO_CREATE);
            }
        });


    }
}