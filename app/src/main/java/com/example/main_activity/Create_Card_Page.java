package com.example.main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
public class Create_Card_Page extends AppCompatActivity {
    ArrayList<Flashcard> flashcards = new ArrayList<Flashcard>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_card_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent receivingEnd = getIntent();
        String Name = receivingEnd.getStringExtra("DeckName");

        EditText EtCardName = findViewById(R.id.etCardName);
        EditText EtCardFront = findViewById(R.id.etCardFront);
        EditText EtCardBack = findViewById(R.id.etCardBack);

        Button BtnCreateCard = findViewById(R.id.btnCreateCard);
        BtnCreateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flashcard flashcard = new Flashcard(EtCardName.toString(),EtCardFront.toString(),EtCardBack.toString(),flashcards.size());
                flashcards.add(flashcard);
            }
        });

        Button BtnFinish = findViewById(R.id.btnFinish);
        BtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create deck and send it to fire base
            }
        });

    }
}