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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "notification_test";
    private static final int NOTIFICATION_ID = 1234;

    // create Notification configs variables
    CheckBox checkBox_postDelayed;
    CheckBox checkBox_StartActivity;
    CheckBox checkBox_fullScreen;
    Spinner spinner_seconds;
    TextView textview_seconds;
    CheckBox checkBox_ongoing;
    CheckBox checkBox_flagnoclear;
    CheckBox checkBox_autocancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get Notification configs references
        checkBox_postDelayed = (CheckBox) findViewById(R.id.checkBox_postDelay);
        checkBox_StartActivity = (CheckBox) findViewById(R.id.checkBox_StartActivity);
        checkBox_fullScreen = (CheckBox) findViewById(R.id.checkBox_fullScreen);
        spinner_seconds = (Spinner) findViewById(R.id.spinner_seconds);
        spinner_seconds.setSelection(2);
        textview_seconds = (TextView) findViewById(R.id.textview_seconds);
        checkBox_ongoing = (CheckBox) findViewById(R.id.checkBox_ongoing);
        checkBox_flagnoclear = (CheckBox) findViewById(R.id.checkBox_flagnoclear);
        checkBox_autocancel = (CheckBox) findViewById(R.id.checkBox_autocancel);
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

    //TODO disable landscape mode

    //TODO maybe needed to delete this method
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

    //show the seconds spinner
    public void onCheckBoxSecondsClicked(View v){

        if(checkBox_postDelayed.isChecked()){
            spinner_seconds.setVisibility(View.VISIBLE);
            textview_seconds.setVisibility(View.VISIBLE);

        } else {
            spinner_seconds.setVisibility(View.GONE);
            textview_seconds.setVisibility(View.GONE);
        }

    }

    //TODO refactoring...
    public void sendNotification(View v) {

        // create a channel
        createNotificationChannel();

        // create a Notification
        final NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.venturus)
                .setContentTitle("layonf notification test")
                .setContentText(getString(R.string.notifi_text))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.notifi_text)));

        // create notification the set flags
        final Notification notification = builder.build();

        //set the properties on notification:

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


        //TODO priority to do

        //TODO start result activity to do

        //TODO fullscreen doesn't work

        // set ongoing
        if (checkBox_ongoing.isChecked()){
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
        }

        // set Flag_NO_CLEAR
        if (checkBox_flagnoclear.isChecked()){
            notification.flags |= Notification.FLAG_NO_CLEAR;
        }

        //TODO the FLAG_AUTO_CANCEL doesn't work
        if (checkBox_autocancel.isChecked()){
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
        }



        // post the notification
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //send notification
                NotificationManagerCompat notificationManagerCompat =
                        NotificationManagerCompat.from(getApplicationContext());
                notificationManagerCompat.notify(NOTIFICATION_ID, notification);
            }
        }, delay * 1000);
    }

    //TODO remove notification method


    //TODO maybe needed to delete this method
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

    //TODO maybe needed to delete this method
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

