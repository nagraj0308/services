package com.nagraj.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class BoundService extends Service {
    boolean allowRebind = true;//give desired value true or false
    private final IBinder binder = new MyBinder();


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG","onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("TAG","onBind");
        Toast.makeText(this,intent.getExtras().getString("MSG"),Toast.LENGTH_LONG).show();
        return binder;
    }

    public class MyBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("TAG","onUnBind");
        return allowRebind;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.e("TAG","onRebind");
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e("TAG","onDestroy");
        super.onDestroy();
    }
}
