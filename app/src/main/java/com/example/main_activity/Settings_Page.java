package com.example.main_activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Settings_Page extends AppCompatActivity {

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
        EditText editTextInterval = findViewById(R.id.editText_interval);
        Button btnApply = findViewById(R.id.btn_Apply);
        Button btnRemove = findViewById(R.id.btn_Remove);
        ImageView ivBack = findViewById(R.id.ivSettingsBack);

        // back button to go back to the main activity page
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Load the saved settings
        SharedPreferences loadPreferences = getSharedPreferences("SettingsPrefs",MODE_PRIVATE);
        editTextInterval.setText(loadPreferences.getString("notifications_interval", ""));
        if (loadPreferences.getBoolean("notifications_enabled",false)) {
            btnApply.setEnabled(false);
            btnRemove.setEnabled(true);
        } else {
            btnApply.setEnabled(true);
            btnRemove.setEnabled(false);
        }

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextInterval.getText().toString().matches("")) {
                    // if interval has no value
                    Toast.makeText(Settings_Page.this, "Please enter a number for the interval", Toast.LENGTH_SHORT).show();
                }else if (Integer.parseInt(editTextInterval.getText().toString()) > 0) {
                    // if interval has a value more than 0
                    // get the shared preferences
                    SharedPreferences preferences = getSharedPreferences("SettingsPrefs",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("notifications_enabled", true);
                    editor.apply();
                    // Initialize Notification Channel
                    createNotificationChannel();
                    // Schedule Alarm
                    long intervalMillis = (long) Integer.parseInt(editTextInterval.getText().toString()) * 60 * 1000;
                    SettingsAlarm.scheduleAlarm(v.getContext(), intervalMillis);
                    Toast.makeText(Settings_Page.this, "Alarm Applied", Toast.LENGTH_SHORT).show();
                    btnApply.setEnabled(false);
                    btnRemove.setEnabled(true);
                } else {
                    // if interval is 0
                    Toast.makeText(Settings_Page.this, "Interval should be more than 0", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // button to cancel the alarm
        btnRemove.setOnClickListener(new View.OnClickListener() {
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
                btnApply.setEnabled(true);
                btnRemove.setEnabled(false);
            }
        });

        // function to see if the edit text is changed
        editTextInterval.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // on change, update the shared preferences to show the newly changed value
                SharedPreferences preferences = getSharedPreferences("SettingsPrefs",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("notifications_interval", s.toString());
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable s) {}
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