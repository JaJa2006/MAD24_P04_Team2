package com.example.main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        // recieve intent from create deck page to get deck name
        Intent receivingEnd = getIntent();
        String Name = receivingEnd.getStringExtra("DeckName");
        // get all the edit text and button from XML
        EditText EtCardName = findViewById(R.id.etCardName);
        EditText EtCardFront = findViewById(R.id.etCardFront);
        EditText EtCardBack = findViewById(R.id.etCardBack);
        Button BtnCreateCard = findViewById(R.id.btnCreateCard);
        Button BtnFinish = findViewById(R.id.btnFinish);
        // create card onclick listener
        BtnCreateCard.setOnClickListener(new View.OnClickListener() {
            // on click make sure all field are filled and add card if filed
            @Override
            public void onClick(View v) {
                if (EtCardName.getText().toString().matches("") || EtCardFront.getText().toString().matches("") ||EtCardBack.getText().toString().matches("")){
                    Toast.makeText(Create_Card_Page.this, "All fields need to be filled", Toast.LENGTH_SHORT).show();
                }else {
                    // Create flashcard
                    Flashcard flashcard = new Flashcard(EtCardName.getText().toString(),EtCardFront.getText().toString(),EtCardBack.getText().toString());
                    flashcards.add(flashcard);
                    // clear the edit text field
                    EtCardName.getText().clear();
                    EtCardFront.getText().clear();
                    EtCardBack.getText().clear();
                    Toast.makeText(Create_Card_Page.this, "Card Created", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // finish creating card button
        BtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make sure ther are at least 2 cards
                if (flashcards.size() >= 2) {
                    // add deck to the SQLite if got 2 or more cards
                    Deck deck = new Deck(Name,flashcards);
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