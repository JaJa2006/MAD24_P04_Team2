package com.example.main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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
        // button for flashcard
        Button BtnFlashCard = findViewById(R.id.btnFlashCards);
        Button BtnTimer = findViewById(R.id.btnTimer);
        // on click listener for flashcard button to bring to new activity
        BtnFlashCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create intent
                Intent ManageDeckPage = new Intent(MainActivity.this,Manage_Decks_Page.class);
                // start manage deck page
                startActivity(ManageDeckPage);
            }
        });
        BtnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent StudySessionActivity = new Intent(MainActivity.this,StudySessionActivity.class);
                startActivity(StudySessionActivity);
            }
        });
        Button settingsIcon = findViewById(R.id.settingsIcon);
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EnterSettingsPage = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(EnterSettingsPage);
            }
        });
    }
}
