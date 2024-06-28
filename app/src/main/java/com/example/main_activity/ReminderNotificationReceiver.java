package com.example.main_activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.pm.PackageManager;

public class ReminderNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ReNotificationReceiver", "Notification received!");
        // Build and show notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Reminder")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Reminder")
                .setContentText("You have not reviewed your cards in a while, come back!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // check if user enable notification
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("ReNotificationReceiver", "No Notification!");
        }else{
            // notify the user
            notificationManager.notify(200, builder.build());
        }
    }
}
