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
    ImageButton btnDrawing, btnEraser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card_page);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent receivingEnd = getIntent();
        String name = receivingEnd.getStringExtra("DeckName");

        EditText etCardName = findViewById(R.id.etCardName);
        EditText etCardFront = findViewById(R.id.etCardFront);
        EditText etCardBack = findViewById(R.id.etCardBack);
        TextView tvCreateCard = findViewById(R.id.tvCreateCard);
        TextView tvFinish = findViewById(R.id.tvFinish);
        drawingView = findViewById(R.id.drawingView);
        btnDrawing = findViewById(R.id.btnDrawing);
        btnEraser = findViewById(R.id.btnEraser);

        btnDrawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setDrawingMode();
            }
        });

        btnEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setEraserMode();
            }
        });

        tvCreateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String allString = etCardName.getText().toString() + etCardFront.getText().toString() + etCardBack.getText().toString();
                if (etCardName.getText().toString().matches("") || etCardFront.getText().toString().matches("") || etCardBack.getText().toString().matches("")) {
                    Toast.makeText(Create_Card_Page.this, "All fields need to be filled", Toast.LENGTH_SHORT).show();
                } else if (allString.contains(",") || allString.contains("/")) {
                    Toast.makeText(Create_Card_Page.this, "Fields cannot have , or /", Toast.LENGTH_SHORT).show();
                } else {
                    Bitmap drawing = drawingView.getBitmap();
                    Flashcard flashcard = new Flashcard(etCardName.getText().toString(), etCardFront.getText().toString(), etCardBack.getText().toString(), drawing);
                    flashcards.add(flashcard);
                    etCardName.getText().clear();
                    etCardFront.getText().clear();
                    etCardBack.getText().clear();
                    drawingView.clear();
                    Toast.makeText(Create_Card_Page.this, "Card Created", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flashcards.size() >= 2) {
                    Deck deck = new Deck(name, flashcards);
                    DeckDatabaseHandler dbHandler = new DeckDatabaseHandler(Create_Card_Page.this, null, null, 1);
                    dbHandler.addDeck(deck);
                    finish();
                } else {
                    Toast.makeText(Create_Card_Page.this, "You must have at least 2 cards", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
