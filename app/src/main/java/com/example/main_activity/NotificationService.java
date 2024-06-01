package com.example.main_activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class NotificationService extends Service {
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sharedPreferences = getSharedPreferences("NotificationPrefs", MODE_PRIVATE);

        boolean notificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", false);
        int intervalDays = sharedPreferences.getInt("interval_days", 1);

        if (notificationsEnabled) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent(this, NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, 0);

            long intervalMillis = intervalDays * AlarmManager.INTERVAL_DAY;
            long triggerAtMillis = System.currentTimeMillis() + intervalMillis;

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, pendingIntent);
        }

        return START_STICKY;
    }
}

