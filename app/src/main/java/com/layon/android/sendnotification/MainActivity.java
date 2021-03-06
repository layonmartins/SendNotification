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
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.BitmapFactory;

public class MainActivity extends AppCompatActivity {

    private static int CHANNEL_ID = 1;
    private static int NOTIFICATION_ID = 1;
    private static final String GROUP_KEY_WORK_EMAIL = "com.android.layon.GROUP";

    private static String contentTitleNotification;

    // create Notification configs variables
    CheckBox checkBox_postDelayed;
    CheckBox checkBox_StartActivity;
    CheckBox checkBox_fullScreen;
    Spinner spinner_seconds;
    Spinner spinner_importance;
    TextView textview_seconds;
    CheckBox checkBox_ongoing;
    CheckBox checkBox_flagnoclear;
    CheckBox checkBox_autocancel;
    CheckBox checkBox_importance;
    CheckBox checkBox_group;
    CheckBox checkBox_custom;
    CheckBox checkBox_category;
    Spinner spinner_category;
    CheckBox checkBox_colorized;
    Spinner spinner_style;
    CheckBox checkBox_style;
    CheckBox checkBox_channelId;
    CheckBox checkBox_notificationId;
    View channelIdControls;
    View notificationIdControls;
    TextView txt_channelId;
    TextView txt_notificationId;
    CheckBox checkBox_vibrate;
    TextView textView_vibrate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO is to large list, create list like a RecycleView.

        // get Notification configs references
        checkBox_postDelayed = (CheckBox) findViewById(R.id.checkBox_postDelay);
        checkBox_StartActivity = (CheckBox) findViewById(R.id.checkBox_StartActivity);
        checkBox_fullScreen = (CheckBox) findViewById(R.id.checkBox_fullScreen);
        spinner_seconds = (Spinner) findViewById(R.id.spinner_seconds);
        spinner_importance = (Spinner) findViewById(R.id.spinner_importance);
        textview_seconds = (TextView) findViewById(R.id.textview_seconds);
        checkBox_ongoing = (CheckBox) findViewById(R.id.checkBox_ongoing);
        checkBox_flagnoclear = (CheckBox) findViewById(R.id.checkBox_flagnoclear);
        checkBox_autocancel = (CheckBox) findViewById(R.id.checkBox_autocancel);
        checkBox_importance = (CheckBox) findViewById(R.id.checkBox_importance);
        checkBox_group = (CheckBox) findViewById(R.id.checkBox_group);
        checkBox_custom = (CheckBox) findViewById(R.id.checkBox_custom);
        checkBox_category = (CheckBox) findViewById(R.id.checkBox_category);
        spinner_category = (Spinner) findViewById(R.id.spinner_category);
        checkBox_colorized = (CheckBox) findViewById(R.id.checkBox_colorized);
        checkBox_style = (CheckBox) findViewById(R.id.checkBox_style);
        spinner_style = (Spinner) findViewById(R.id.spinner_style);
        checkBox_channelId = (CheckBox) findViewById(R.id.checkBox_channelId);
        checkBox_notificationId = (CheckBox) findViewById(R.id.checkBox_notificationId);
        channelIdControls = (LinearLayout) findViewById(R.id.channelIdControls);
        notificationIdControls = (LinearLayout) findViewById(R.id.notificationIdControls);
        txt_channelId = (TextView) findViewById(R.id.txt_channelId);
        txt_notificationId = (TextView) findViewById(R.id.txt_notificationId);
        checkBox_vibrate = (CheckBox) findViewById(R.id.checkBox_vibrate);
        textView_vibrate = (TextView) findViewById(R.id.textView_vibrate);

        // TODO continue here the style customization

        // set spinner_importance adapter
        ArrayAdapter<CharSequence> adapterImportance = ArrayAdapter.createFromResource(this,
                R.array.importance_array, android.R.layout.simple_dropdown_item_1line);
        adapterImportance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_importance.setAdapter(adapterImportance);
        spinner_importance.setSelection(1);

        // set spinner_seconds adapter
        ArrayAdapter<CharSequence> adapterSeconds = ArrayAdapter.createFromResource(this,
                R.array.seconds_array, android.R.layout.simple_dropdown_item_1line);
        adapterImportance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_seconds.setAdapter(adapterSeconds);
        spinner_seconds.setSelection(2);

        // set spinner_category adapter
        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_dropdown_item_1line);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adapterCategory);
        spinner_category.setSelection(1);

        // set spinner_style adapter
        ArrayAdapter<CharSequence> adapterStyle = ArrayAdapter.createFromResource(this,
                R.array.style_array, android.R.layout.simple_dropdown_item_1line);
        adapterStyle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_style.setAdapter(adapterStyle);
        spinner_style.setSelection(1);

        //set default ids
        txt_channelId.setText(Integer.toString(CHANNEL_ID));
        txt_notificationId.setText(Integer.toString(NOTIFICATION_ID));

    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "SendNotificationChannel";
            String description = "This is a channel of the SendNotification app";

            // get importance
            // TODO it doesn't work, check later
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (checkBox_importance.isChecked()) {
                importance = getImportance(spinner_importance.getSelectedItem().toString());
                Log.d("layonf importance: ", Integer.toString(importance));
            }

            NotificationChannel channel = new NotificationChannel(Integer.toString(CHANNEL_ID), name, importance);
            channel.setDescription(description);
            if (checkBox_vibrate.isChecked()) {
                channel.enableVibration(true);
            } else {
                channel.enableVibration(false);
            }

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.deleteNotificationChannel(Integer.toString(CHANNEL_ID));
            notificationManager.createNotificationChannel(channel);
        }
    }

    public int getImportance(CharSequence importance) {
        switch (importance.charAt(0)) {
            case 'H':
                return NotificationManager.IMPORTANCE_HIGH;
            case 'D':
                return NotificationManager.IMPORTANCE_DEFAULT;
            case 'L':
                return NotificationManager.IMPORTANCE_LOW;
            case 'M':
                return NotificationManager.IMPORTANCE_MIN;
            default:
                return NotificationManager.IMPORTANCE_DEFAULT;
        }
    }

    // get the String Category of the notification
    public String getCategory(CharSequence importance) {
        switch (importance.toString()) {
            case "call":
                return Notification.CATEGORY_CALL;
            case "msg":
                return Notification.CATEGORY_MESSAGE;
            case "email":
                return Notification.CATEGORY_EMAIL;
            case "event":
                return Notification.CATEGORY_EVENT;
            case "alarm":
                return Notification.CATEGORY_ALARM;
            case "sys":
                return Notification.CATEGORY_SYSTEM;
            case "service":
                return Notification.CATEGORY_SERVICE;
            case "reminder":
                return Notification.CATEGORY_REMINDER;
            default:
                return "notification";
        }
    }

    // get the style of the notification return the correct class.
    public NotificationCompat.Style getStyle(CharSequence style) {
        switch (style.toString()) {
            case "BigTextStyle":
                return new NotificationCompat.BigTextStyle().bigText(getString(R.string.notifi_text));
            case "BigPictureStyle":
                return new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.bigcat));
            case "DecoratedCustomViewStyle":
                return new NotificationCompat.DecoratedCustomViewStyle(); // It doesn't work without set the custom Content View
            case "InboxStyle":
                contentTitleNotification = "3 New message from fulano de tal";
                return new NotificationCompat.InboxStyle()
                        .addLine("Fulano de tal - Hi whats up man?")
                        .addLine("Eu - I am ok and you?")
                        .addLine("Fulano de tal - Sextouuuuuu!");
            // TODO finished the other styles of notifications...
        }
        return null;
    }

    //TODO disable landscape mode

    //show the importance spinner
    public void onCheckBoxImportanceClicked(View v) {
        if (checkBox_importance.isChecked()) {
            spinner_importance.setVisibility(View.VISIBLE);
        } else {
            spinner_importance.setVisibility(View.GONE);
        }

    }

    //show the seconds spinner
    public void onCheckBoxSecondsClicked(View v) {
        if (checkBox_postDelayed.isChecked()) {
            spinner_seconds.setVisibility(View.VISIBLE);
            textview_seconds.setVisibility(View.VISIBLE);

        } else {
            spinner_seconds.setVisibility(View.GONE);
            textview_seconds.setVisibility(View.GONE);
        }
    }

    //show the category spinner
    public void onCheckBoxCategoryClicked(View v) {
        if (checkBox_category.isChecked()) {
            spinner_category.setVisibility(View.VISIBLE);
        } else {
            spinner_category.setVisibility(View.GONE);
        }

    }

    //show the style spinner
    public void onCheckBoxStyleClicked(View v) {
        if (checkBox_style.isChecked()) {
            spinner_style.setVisibility(View.VISIBLE);
        } else {
            spinner_style.setVisibility(View.GONE);
        }
    }

    public void onCheckBoxchannelIdClicked(View v) {
        if (checkBox_channelId.isChecked()) {
            channelIdControls.setVisibility(View.VISIBLE);
        } else {
            channelIdControls.setVisibility(View.GONE);
        }
    }

    public void onCheckBoxnotificationIdClicked(View v) {
        if (checkBox_notificationId.isChecked()) {
            notificationIdControls.setVisibility(View.VISIBLE);
        } else {
            notificationIdControls.setVisibility(View.GONE);
        }
    }

    public void onCheckBoxVibrateClicked(View v) {
        textView_vibrate.setVisibility(View.VISIBLE);
        textView_vibrate.setSelected(true);
        Handler handler = new Handler();
        Runnable closeTextViewVibrate = new Runnable() {
            public void run() {
                textView_vibrate.setVisibility(View.GONE);
            }
        };
        handler.postDelayed(closeTextViewVibrate, 30000);
    }


    public void channelIdMinus(View v) {
        CHANNEL_ID--;
        txt_channelId.setText(Integer.toString(CHANNEL_ID));
    }

    public void channelIdPlus(View v) {
        CHANNEL_ID++;
        txt_channelId.setText(Integer.toString(CHANNEL_ID));
    }

    public void notificationIdMinus(View v) {
        NOTIFICATION_ID--;
        txt_notificationId.setText(Integer.toString(NOTIFICATION_ID));
    }

    public void notificationIdPlus(View v) {
        NOTIFICATION_ID++;
        txt_notificationId.setText(Integer.toString(NOTIFICATION_ID));
    }


    //TODO refactoring...
    public void sendNotification(View v) {

        // set default title
        contentTitleNotification = "default notification title";

        // create a channel
        createNotificationChannel();

        // create a Notification
        final NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, Integer.toString(CHANNEL_ID))
                        .setSmallIcon(R.drawable.venturus)
                        .setContentText(getString(R.string.notifi_text));


        // TODO create a check box with a Spinner to chose the notification style
        // Doing

        // set category
        if (checkBox_category.isChecked()) {
            builder.setCategory(getCategory(spinner_category.getSelectedItem().toString()));
        }

        // set style
        if (checkBox_style.isChecked()) {
            builder.setStyle(getStyle(spinner_style.getSelectedItem().toString()));
        } else {
            builder.setStyle(getStyle("BigTextStyle")); //default
        }

        // set colorized
        if (checkBox_colorized.isChecked()) {
            Log.d("layonf", "colorized");
            builder.setColorized(true);
            builder.setColor(Color.BLUE);
        }

        if (checkBox_group.isChecked()) {
            //builder.setGroup(GROUP_KEY_WORK_EMAIL);
            //NOTIFICATION_ID++;

            sendGroupNotification();
            return;
        }

        if (checkBox_custom.isChecked()) {
            //builder.setGroup(GROUP_KEY_WORK_EMAIL);
            //NOTIFICATION_ID++;
            Log.d("layonf", "sendCustomNotification");
            sendCustomNotification();
            return;
        }

        // create notification the set flags
        final Notification notification = builder.build();

        //set the properties on notification:

        //postdelay
        int delay = 0;
        if (checkBox_postDelayed.isChecked()) {
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
        if (checkBox_ongoing.isChecked()) {
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
        }

        // set Flag_NO_CLEAR
        if (checkBox_flagnoclear.isChecked()) {
            notification.flags |= Notification.FLAG_NO_CLEAR;
        }

        //TODO the FLAG_AUTO_CANCEL doesn't work
        if (checkBox_autocancel.isChecked()) {
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
        }

        //Update the title of the notification with Ids
        CharSequence charSequence = "Notification_Id: " + NOTIFICATION_ID + " - Channel_Id: " + CHANNEL_ID;
        notification.extras.putCharSequence(Notification.EXTRA_TITLE, charSequence);

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


    //TODO refactoring this method to insert the logic in sendNotification()
    public void sendGroupNotification() {
        createNotificationChannel();

        Notification newMessageNotification1 =
                new NotificationCompat.Builder(MainActivity.this, Integer.toString(CHANNEL_ID))
                        .setSmallIcon(R.drawable.venturus)
                        .setContentTitle("Jucelma")
                        .setContentText("You will not believe...")
                        .setGroup(GROUP_KEY_WORK_EMAIL)
                        .build();

        Notification newMessageNotification2 =
                new NotificationCompat.Builder(MainActivity.this, Integer.toString(CHANNEL_ID))
                        .setSmallIcon(R.drawable.venturus)
                        .setContentTitle("Jucelma")
                        .setContentText("My neighbor has changed...")
                        .setGroup(GROUP_KEY_WORK_EMAIL)
                        .build();

        Notification newMessageNotification3 =
                new NotificationCompat.Builder(MainActivity.this, Integer.toString(CHANNEL_ID))
                        .setSmallIcon(R.drawable.venturus)
                        .setContentTitle("Creuza")
                        .setContentText("Where did he live?")
                        .setGroup(GROUP_KEY_WORK_EMAIL)
                        .build();

        Notification newMessageNotification4 =
                new NotificationCompat.Builder(MainActivity.this, Integer.toString(CHANNEL_ID))
                        .setSmallIcon(R.drawable.venturus)
                        .setContentTitle("Jucelma")
                        .setContentText("He is your neighbor now :)")
                        .setGroup(GROUP_KEY_WORK_EMAIL)
                        .build();


        Notification summaryNotification =
                new NotificationCompat.Builder(MainActivity.this, Integer.toString(CHANNEL_ID))
                        .setContentTitle("Four new messages")
                        // set content text to support devices running API level < 24
                        // .setContentText("Two new messages")
                        .setSmallIcon(R.drawable.venturus)
                        // build summary info into InboxStyle template
                        .setStyle(new NotificationCompat.InboxStyle()
                                .addLine("Jucelma")
                                .addLine("Jucelma")
                                .addLine("Creuza")
                                .addLine("Creuza")
                                .setBigContentTitle("4 new messages")
                                .setSummaryText("jucelma@example.com"))
                        // specify which group this notification belongs to
                        .setGroup(GROUP_KEY_WORK_EMAIL)
                        // set this notification as the summary for the group
                        .setGroupSummary(true)
                        .build();

        NotificationManagerCompat notificationMangerCompat = NotificationManagerCompat.from(this);
        notificationMangerCompat.notify(12, newMessageNotification1);
        notificationMangerCompat.notify(122, newMessageNotification2);
        notificationMangerCompat.notify(123, newMessageNotification3);
        notificationMangerCompat.notify(124, newMessageNotification4);
        notificationMangerCompat.notify(111, summaryNotification);
    }


    //TODO refactoring this method to insert the logic in sendNotification()
    public void sendCustomNotification() {

        // create a channel
        createNotificationChannel();

        // Get the layout to use in the custom notification
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_custom);
        RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification_custom_expanded);

        // Apply the layouts to the notification
        Notification customNotification = new NotificationCompat.Builder(this, Integer.toString(CHANNEL_ID))
                .setSmallIcon(R.drawable.venturus)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle()) //remove line if you don't want that system insert options like (smallIcon, title, buttomExpanded)
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(75678, customNotification);

    }

    //TODO refactoring this method to insert the logic in sendNotification()
    public void sendStartActivityNotification() {

        //create channel
        createNotificationChannel();

        //create notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, Integer.toString(CHANNEL_ID))
                        .setSmallIcon(R.drawable.venturus)
                        .setContentTitle("layonf PendingIntent Notification Test")
                        //.setContentText(getString(R.string.notifi_text))
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

    //TODO refactoring this method to insert the logic in sendNotification()
    public void sendFullScreenIntentNotification() {
        Intent fullScreenIntent = new Intent(this, ResultActivity.class);
        //fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, Integer.toString(CHANNEL_ID))
                        .setSmallIcon(R.drawable.venturus)
                        .setContentTitle("layonf fullScreen notification test")
                        //.setContentText(getString(R.string.notifi_text))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_CALL)
                        //Use a full-screen
                        .setFullScreenIntent(fullScreenPendingIntent, true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

        startActivity(fullScreenIntent);

    }

    //TODO create a game notification, enabled DND and create a channel that bypass dnd, so send many notification that user has
    //to touch to interact e win the game.
}

