package com.example.main_activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Create_Card_Page extends AppCompatActivity {
    ArrayList<Flashcard> flashcards = new ArrayList<>();
    DrawingView drawingView;

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

        // Receive intent from create deck page to get deck name
        Intent receivingEnd = getIntent();
        String Name = receivingEnd.getStringExtra("DeckName");

        // Get all the edit text and button from XML
        EditText EtCardName = findViewById(R.id.etCardName);
        EditText EtCardFront = findViewById(R.id.etCardFront);
        EditText EtCardBack = findViewById(R.id.etCardBack);
        TextView tvCreateCard = findViewById(R.id.tvCreateCard);
        TextView tvFinish = findViewById(R.id.tvFinish);
        drawingView = findViewById(R.id.drawingView);

        // Get the pen and eraser buttons
        ImageButton btnDrawing = findViewById(R.id.btnDrawing);
        ImageButton btnEraser = findViewById(R.id.btnEraser);

        // Set click listeners for the pen and eraser buttons
        btnDrawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setEraser(false);
            }
        });

        btnEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setEraser(true);
            }
        });

        // Create card onclick listener
        tvCreateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Combine all string to check if have delimiter
                String allString = EtCardName.getText().toString() + EtCardFront.getText().toString() + EtCardBack.getText().toString();
                if (EtCardName.getText().toString().matches("") || EtCardFront.getText().toString().matches("") || EtCardBack.getText().toString().matches("")) {
                    Toast.makeText(Create_Card_Page.this, "All fields need to be filled", Toast.LENGTH_SHORT).show();
                } else if (allString.contains(",") || allString.contains("/")) {
                    // Make sure users are not using the delimiter used
                    Toast.makeText(Create_Card_Page.this, "Fields cannot have , or /", Toast.LENGTH_SHORT).show();
                } else {
                    // Create flashcard
                    Bitmap drawing = drawingView.getBitmap();
                    Flashcard flashcard = new Flashcard(EtCardName.getText().toString(), EtCardFront.getText().toString(), EtCardBack.getText().toString(), drawing);
                    flashcards.add(flashcard);
                    // Clear the edit text field and drawing view
                    EtCardName.getText().clear();
                    EtCardFront.getText().clear();
                    EtCardBack.getText().clear();
                    drawingView.clear();
                    Toast.makeText(Create_Card_Page.this, "Card Created", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Finish creating card button
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make sure there are at least 2 cards
                if (flashcards.size() >= 2) {
                    // Add deck to the SQLite if got 2 or more cards
                    Deck deck = new Deck(Name, flashcards);
                    DeckDatabaseHandler dbHandler = new DeckDatabaseHandler(Create_Card_Page.this);
                    dbHandler.addDeck(deck);
                    finish();
                } else {
                    Toast.makeText(Create_Card_Page.this, "You must have at least 2 cards", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Back button
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
