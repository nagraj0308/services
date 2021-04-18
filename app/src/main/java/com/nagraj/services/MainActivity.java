package com.nagraj.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.EditText;
import android.widget.Switch;

import static com.nagraj.services.ForegroundService.MSG_KEY_FOREGROUND_SERVICE;

public class MainActivity extends AppCompatActivity {
    public final static String MSG_KEY_MAIN_ACTIVITY = "msg_key_main_activity";
    Intent intentBackgroundService, intentForegroundService, intentBoundedService;
    Switch swBackgroundService, swForegroundService, swBoundedService;
    EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etMessage = findViewById(R.id.et_message);
        swBackgroundService = findViewById(R.id.sw_background_service);
        swForegroundService = findViewById(R.id.sw_foreground_service);
        swBoundedService = findViewById(R.id.sw_bounded_service);

        intentBackgroundService = new Intent(this, BackgroundService.class);
        intentForegroundService = new Intent(this, ForegroundService.class);
        intentBoundedService = new Intent(this, BoundService.class);

        swBackgroundService.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                startService(intentBackgroundService);
            } else {
                stopService(intentBackgroundService);
            }
        });

        swForegroundService.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                startService(intentForegroundService);
            } else {
                stopService(intentForegroundService);
            }
        });

        swBoundedService.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                intentBoundedService.putExtra("MSG", etMessage.getText().toString());
                bindService(intentBoundedService, serviceConnection, Context.BIND_AUTO_CREATE);
            } else {
                unbindService(serviceConnection);
            }
        });
    }

    protected ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.e("LOG_TAG", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("LOG_TAG", "onServiceDisconnected");
        }
    };

    @Override
    protected void onDestroy() {
        stopService(intentForegroundService);
        super.onDestroy();
        stopService(intentBackgroundService);
        unbindService(serviceConnection);
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(MSG_KEY_MAIN_ACTIVITY);
            if(Double.parseDouble(msg)<=0) stopService(intentForegroundService);
            Log.d("TAG", "onReceive: " + msg);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, new IntentFilter(MSG_KEY_FOREGROUND_SERVICE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(broadcastReceiver);

    }
}