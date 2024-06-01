package com.example.main_activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class NotificationService extends Service {
    private static final int NOTIFICATION_ID = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Get the user preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notificationsEnabled = preferences.getBoolean("notifications_enabled", false);
        String intervalString = preferences.getString("notifications_interval", "1");

        if (notificationsEnabled) {
            int interval = Integer.parseInt(intervalString);

            // Schedule the notification
            scheduleNotification(interval);
        }

        return START_STICKY;
    }

    private void scheduleNotification(int intervalDays) {
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long intervalMillis = intervalDays * AlarmManager.INTERVAL_DAY;
        long triggerAtMillis = System.currentTimeMillis() + intervalMillis;

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, pendingIntent);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

