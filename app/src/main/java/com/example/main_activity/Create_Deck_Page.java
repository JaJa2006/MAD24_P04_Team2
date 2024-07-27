package com.example.main_activity;

import android.content.Intent;
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

public class Create_Deck_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deck_page);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText etDeckName = findViewById(R.id.etDeckName);
        TextView tvCreateDeck = findViewById(R.id.tvCreateDeck);

        tvCreateDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deckName = etDeckName.getText().toString();
                if (deckName.isEmpty()) {
                    Toast.makeText(Create_Deck_Page.this, "Deck name is required", Toast.LENGTH_SHORT).show();
                } else {
                    Intent createCardIntent = new Intent(Create_Deck_Page.this, Create_Card_Page.class);
                    createCardIntent.putExtra("DeckName", deckName);
                    startActivity(createCardIntent);
                    finish();
                }
            }
        });


    }
}
