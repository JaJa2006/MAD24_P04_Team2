package com.example.main_activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {
    private Switch switchNotifications;
    private EditText editTextInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchNotifications = findViewById(R.id.switch_notifications);
        editTextInterval = findViewById(R.id.editText_interval);

        // Load the saved settings
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        switchNotifications.setChecked(preferences.getBoolean("notifications_enabled", false));
        editTextInterval.setText(preferences.getString("notifications_interval", ""));

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("notifications_enabled", isChecked);
            editor.apply();
        });

        editTextInterval.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("notifications_interval", s.toString());
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}



