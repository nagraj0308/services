package com.nagraj.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.nagraj.services.MainActivity.MSG_KEY_MAIN_ACTIVITY;

public class ForegroundService extends Service {
    private static final int ONGOING_NOTIFICATION = 1;
    public static final String CHANNEL_ID = "foreground_service_channel_id";
    public final static String MSG_KEY_FOREGROUND_SERVICE = "msg_key_foreground_service";
    final Handler handler = new Handler();
    Context context;
    private long remainingTime;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        remainingTime=System.currentTimeMillis()+10*1000;
        context=this;
        Log.e("TAG", "onStartCommand");
        createNotification();
        startLooper();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentIntent(pendingIntent)
                .setContentText(intent.getStringExtra("MSG"))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.vibrate))
                .setVibrate(new long[]{0, 500, 1000})
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .build();
        startForeground(ONGOING_NOTIFICATION, notification);
        return START_STICKY;


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("TAG", "onBind");


        return null;
    }

    @Override
    public void onDestroy() {
        Log.e("TAG", "onDestroy");
        stopLooper();
        super.onDestroy();
    }

    public void createNotification() {
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.vibrate);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    "Foreground Service", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setDescription("Description");
            notificationChannel.enableLights(true);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            notificationChannel.setSound(soundUri, audioAttributes);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    void startLooper() {
        r.run();
        Log.e("TAG", "startLooper");
    }

    void stopLooper() {
        handler.removeCallbacks(r);
        Log.e("TAG", "stopLooper");
    }

    final Runnable r = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(MSG_KEY_FOREGROUND_SERVICE);
            intent.putExtra(MSG_KEY_MAIN_ACTIVITY, String.valueOf((remainingTime-System.currentTimeMillis())/1000));
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            handler.postDelayed(this, 1000);
        }
    };


}
