package com.example.main_activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final int RC_NOTIFICATION = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // recieve intent to get username
        Intent receivingEnd = getIntent();
        String Name = receivingEnd.getStringExtra("Username");

        // set up welcome
        TextView tvName = findViewById(R.id.tvName);
        tvName.setText(Name);


        // buttons
        TextView tvFlashCard = findViewById(R.id.tvFlashCards);
        TextView tvTimer = findViewById(R.id.tvTimer);
        TextView tvMemo = findViewById(R.id.tvMemo);
        TextView tvSettingsIcon = findViewById(R.id.tvSettings);
        TextView tvSignOut = findViewById(R.id.tvSignOut);
        // on click listener for flashcard button to bring to new activity
        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvFlashCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create intent
                Intent ManageDeckPage = new Intent(MainActivity.this,Manage_Decks_Page.class);
                // start manage deck page
                startActivity(ManageDeckPage);
            }
        });
        tvTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent StudySessionActivity = new Intent(MainActivity.this,StudySessionPage.class);
                startActivity(StudySessionActivity);
            }
        });
        tvMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EnterSettingsPage = new Intent(MainActivity.this, Memo_Page.class);
                startActivity(EnterSettingsPage);
            }
        });
        tvSettingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EnterSettingsPage = new Intent(MainActivity.this, Settings_Page.class);
                startActivity(EnterSettingsPage);
            }
        });
        // check if permission for notification is enabled
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Ask permission for notification
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, RC_NOTIFICATION);
            }
        }
    }
    // to handle notification permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_NOTIFICATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"ALLOWED",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"DENIED",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
