package com.example.main_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private Switch switchNotifications;
    private EditText intervalDays;
    private Button btnSave;
    private ImageButton btnBack;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchNotifications = findViewById(R.id.switch_notifications);
        intervalDays = findViewById(R.id.interval_days);
        btnSave = findViewById(R.id.btn_save);
        btnBack = findViewById(R.id.btn_back);
        sharedPreferences = getSharedPreferences("NotificationPrefs", MODE_PRIVATE);

        loadPreferences();

        btnSave.setOnClickListener(v -> savePreferences());
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadPreferences() {
        boolean notificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", false);
        int interval = sharedPreferences.getInt("interval_days", 1);

        switchNotifications.setChecked(notificationsEnabled);
        intervalDays.setText(String.valueOf(interval));
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notifications_enabled", switchNotifications.isChecked());
        editor.putInt("interval_days", Integer.parseInt(intervalDays.getText().toString()));
        editor.apply();

        scheduleNotification();
    }

    private void scheduleNotification() {
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
    }
}
