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

public class Create_Deck_Page extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_deck_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // get all button and edit text from XML
        Button BtnCreateDeck = findViewById(R.id.btnCreateDeck);
        EditText EtDeckName = findViewById(R.id.etDeckName);
        // on click for creating deck
        BtnCreateDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for all fields to be filled
                if (EtDeckName.getText().toString().matches("")){
                    Toast.makeText(Create_Deck_Page.this, "All fields need to be filled", Toast.LENGTH_SHORT).show();
                }else {
                    // create the deck and go to the create card page
                    Toast.makeText(Create_Deck_Page.this, "Deck created", Toast.LENGTH_SHORT).show();
                    Intent CreateCardPage = new Intent(Create_Deck_Page.this,Create_Card_Page.class);
                    CreateCardPage.putExtra("DeckName",EtDeckName.getText().toString());
                    startActivity(CreateCardPage);
                    finish();
                }
            }
        });
    }
}