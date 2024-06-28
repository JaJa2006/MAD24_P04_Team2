package com.example.main_activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class SettingsAlarm {
    // function to schedule the alarm
    public static void scheduleAlarm(Context context, long intervalMillis) {
        // get alarm manager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // create an intent
        Intent intent = new Intent(context, ReminderNotificationReceiver.class);
        // create the intent for broadcast
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        // get the time for the notification to show up
        long triggerAtMillis = System.currentTimeMillis() + intervalMillis;
        // use the alarm manager to send the repeating alarm notification at the specified time and an interval for the time between notification
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, pendingIntent);
    }
    // function to cancel the intent
    public static void cancelAlarm(Context context) {
        // get the alarm manager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // create an intent
        Intent intent = new Intent(context, ReminderNotificationReceiver.class);
        // create the intent for broadcast
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        // cancel the intent
        alarmManager.cancel(pendingIntent);
    }
}
