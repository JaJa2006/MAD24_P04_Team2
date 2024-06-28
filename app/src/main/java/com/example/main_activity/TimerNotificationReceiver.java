package com.example.main_activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TimerNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TiNotificationReceiver", "Notification received!");
        // Build and show notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Timer")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Timer")
                .setContentText("Your study session has ended!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // check if user enable notification
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("TiNotificationReceiver", "No Notification!");
        }else{
            // notify the user
            notificationManager.notify(201, builder.build());
        }
    }
}
