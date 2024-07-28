package com.example.main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Create_Deck_Page extends AppCompatActivity {

    private static final String TAG = "Create_Deck_Page";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deck_page);

        try {
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
                    try {
                        String deckName = etDeckName.getText().toString();
                        if (deckName.isEmpty()) {
                            Toast.makeText(Create_Deck_Page.this, "Deck name is required", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Deck name is empty");
                        } else {
                            Log.d(TAG, "Deck name entered: " + deckName);
                            Intent createCardIntent = new Intent(Create_Deck_Page.this, Create_Card_Page.class);
                            createCardIntent.putExtra("DeckName", deckName);
                            startActivity(createCardIntent);
                            finish();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error in Create Deck button click", e);
                        Toast.makeText(Create_Deck_Page.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Create_Deck_Page", e);
            Toast.makeText(this, "Error initializing Create Deck page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
