package com.layon.android.sendnotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "notification_test";
    private static final int NOTIFICATION_ID = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "NotificationBroadcast";
            String description = "This is a broadcast notification fot test";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendBaseNotification() {
        createNotificationChannel();
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.venturus)
                        .setContentTitle("Layonf Notification Test")
                        .setContentText(getString(R.string.notifi_text))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(getString(R.string.notifi_text)))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //Set flags:
        Notification notification = builder.build();
        //notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        //notification.flags |= Notification.FLAG_AUTO_CANCEL;
        //notification.flags |= Notification.FLAG_ONGOING_EVENT;
        //notification.flags |= Notification.FLAG_NO_CLEAR;
        //notification.flags |= Notification.FLAG_LOCAL_ONLY;


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void sendNotificationDelayed(View v, int delay) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendBaseNotification();
            }
        }, delay);
    }

    public void sendNotification(View v) {
        CheckBox checkBox_postDelayed = (CheckBox) findViewById(R.id.checkBox_postDelay);
        Spinner spinner_seconds = (Spinner) findViewById(R.id.spinner_seconds);

        //it doesn't work
        int delay = Integer.parseInt(spinner_seconds.getSelectedItem().toString());

        if (checkBox_postDelayed.isChecked()){
            sendNotificationDelayed(v, delay);
        } else {
            sendBaseNotification();
        }
    }
}

