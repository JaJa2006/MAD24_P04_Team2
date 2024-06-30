package com.example.main_activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

public class Settings_Page extends AppCompatActivity {

    Long TimeSet;
    int hour;
    int minute;
    String period;

    Boolean isTimeSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView tvApply = findViewById(R.id.tv_Apply);
        TextView tvRemove = findViewById(R.id.tv_Remove);
        ImageView ivBack = findViewById(R.id.ivSettingsBack);
        TextView tvNotificationTime = findViewById(R.id.tvNotificationTime);

        // back button to go back to the main activity page
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // notification time setter button
        tvNotificationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // to get the time now
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                // check if pm or am
                AtomicBoolean isPM = new AtomicBoolean(currentHour >= 12);
                if (isPM.get()) {
                    currentHour -= 12;
                }
                // set up time picker
                TimePickerDialog timePickerDialog = new TimePickerDialog(Settings_Page.this,
                        (TimePicker view, int hourOfDay, int minuteOfHour) -> {
                            isPM.set(hourOfDay >= 12);
                            hour = hourOfDay % 12; // Convert 24-hour format to 12-hour format
                            minute = minuteOfHour;
                            period = isPM.get() ? "PM" : "AM";
                            isTimeSet = true;
                            // put all the data into shared preference
                            SharedPreferences preferences = getSharedPreferences("SettingsPrefs",MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("hour", hour);
                            editor.putInt("minute", minute);
                            editor.putBoolean("isPM", isPM.get());
                            editor.putBoolean("TimeSet",true);
                            editor.apply();
                            // set the text
                            tvNotificationTime.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, period));
                            TimeSet = (long) hourOfDay * 60 * 60 * 1000 + minuteOfHour * 60 * 1000;
                            // get the time now
                            Calendar TimeNow = Calendar.getInstance();
                            int hour24hrs = TimeNow.get(Calendar.HOUR_OF_DAY);
                            int minutes = TimeNow.get(Calendar.MINUTE);
                            long TimeNowMili = (long) hour24hrs * 60 * 60 * 1000 + minutes * 60 * 1000;
                            // check to see if the time is before now or after now
                            if (TimeSet - TimeNowMili > 0) {
                                TimeSet = System.currentTimeMillis() + TimeSet - TimeNowMili;
                            } else {
                                TimeSet = System.currentTimeMillis() + TimeSet + 86400000 - TimeNowMili;
                            }
                        }, currentHour, currentMinute, false);
                // show the time picker
                timePickerDialog.show();
            }
        });

        // Load the saved settings
        SharedPreferences loadPreferences = getSharedPreferences("SettingsPrefs",MODE_PRIVATE);
        boolean isPM = loadPreferences.getBoolean("isPM",false);
        isTimeSet = loadPreferences.getBoolean("TimeSet",false);
        period = isPM ? "PM" : "AM";
        hour = loadPreferences.getInt("hour",0);
        minute = loadPreferences.getInt("minute",0);
        if (loadPreferences.getBoolean("notifications_enabled",false)) {
            tvApply.setEnabled(false);
            tvRemove.setEnabled(true);
            tvNotificationTime.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, period));
        } else {
            tvApply.setEnabled(true);
            tvRemove.setEnabled(false);
            tvNotificationTime.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, period));
        }

        tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimeSet) {
                    // is time is set
                    // get the shared preferences
                    SharedPreferences preferences = getSharedPreferences("SettingsPrefs",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("notifications_enabled", true);
                    editor.apply();
                    // Initialize Notification Channel
                    createNotificationChannel();
                    // Schedule Alarm
                    long intervalMillis = TimeSet;
                    SettingsAlarm.scheduleAlarm(v.getContext(), intervalMillis);
                    Toast.makeText(Settings_Page.this, "Alarm Applied", Toast.LENGTH_SHORT).show();
                    tvApply.setEnabled(false);
                    tvRemove.setEnabled(true);
                }else {
                    // if time is not set
                    Toast.makeText(Settings_Page.this, "Please set the time", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // button to cancel the alarm
        tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // function to cancel the alarm
                SettingsAlarm.cancelAlarm(v.getContext());
                // update the shared preference to say that notification is not enabled
                SharedPreferences preferences = getSharedPreferences("SettingsPrefs",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("notifications_enabled", false);
                editor.apply();
                Toast.makeText(Settings_Page.this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
                tvApply.setEnabled(true);
                tvRemove.setEnabled(false);
            }
        });
    }

    // function to create the notification channel to allow for sending of notification
    private void createNotificationChannel() {
        // check if the build version is more than the version oreo which is 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create the channel
            CharSequence name = "ReminderChannel";
            String description = "Channel for reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Reminder",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}