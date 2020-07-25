package com.nagraj.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class BackgroundService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG","onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TAG","onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("TAG","onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.e("TAG","onDestroy");
        super.onDestroy();
    }

}
