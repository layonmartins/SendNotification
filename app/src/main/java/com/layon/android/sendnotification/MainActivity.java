package com.layon.android.sendnotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

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
                        .setContentTitle("layonf Notification Test")
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


    // refactoring...
    public void sendNotification(View v) {

        // get Notification configs
        CheckBox checkBox_postDelayed = (CheckBox) findViewById(R.id.checkBox_postDelay);
        CheckBox checkBox_StartActivity = (CheckBox) findViewById(R.id.checkBox_StartActivity);
        CheckBox checkBox_fullScreen = (CheckBox) findViewById(R.id.checkBox_fullScreen);
        Spinner spinner_seconds = (Spinner) findViewById(R.id.spinner_seconds);

        // create a channel
        createNotificationChannel();

        // create a Notification
        final NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.venturus)
                .setContentTitle("layonf notification test")
                .setContentText(getString(R.string.notifi_text))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.notifi_text)));


        //set the properties on notification:

        //priority

        //start result activity

        //fullscreen

        //postdelay
        int delay = 0;
        if(checkBox_postDelayed.isChecked()){
            delay = Integer.parseInt(spinner_seconds.getSelectedItem().toString());
            StringBuilder str = new StringBuilder();
            str.append(getString(R.string.toast));
            str.append(" " + Integer.toString(delay) + " seconds");
            Toast toast = Toast.makeText(this, str.toString(), Toast.LENGTH_LONG);
            toast.show();
        }


        //post the notification
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //send notification
                NotificationManagerCompat notificationManagerCompat =
                        NotificationManagerCompat.from(getApplicationContext());
                notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
            }
        }, delay * 1000);
    }

    //maybe needed to delete this method
    public void sendStartActivityNotification() {

        //create channel
        createNotificationChannel();

        //create notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.venturus)
                        .setContentTitle("layonf PendingIntent Notification Test")
                        .setContentText(getString(R.string.notifi_text))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(getString(R.string.notifi_text)))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //create the pendingIntent:
        //create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, ResultActivity.class);
        //create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        //get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //notify
        builder.setContentIntent(resultPendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    //maybe needed to delete this method
    public void sendFullScreenIntentNotification() {
        Intent fullScreenIntent = new Intent(this, ResultActivity.class);
        //fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.venturus)
                .setContentTitle("layonf fullScreen notification test")
                .setContentText(getString(R.string.notifi_text))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                //Use a full-screen
                .setFullScreenIntent(fullScreenPendingIntent, true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

        startActivity(fullScreenIntent);

    }
}

