package com.example.main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Manage_Decks_Page extends AppCompatActivity {

    private RecyclerView rvDeckButton;
    private DeckAdapter deckAdapter;
    private ArrayList<Deck> deckList;
    private DeckDatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_decks_page);

        dbHandler = new DeckDatabaseHandler(this);
        deckList = dbHandler.getDeck();

        rvDeckButton = findViewById(R.id.rvDeckButton);
        deckAdapter = new DeckAdapter(deckList, this);
        rvDeckButton.setAdapter(deckAdapter);
        rvDeckButton.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.tvAddCard).setOnClickListener(v -> {
            // Handle adding new cards
            Intent createDeck = new Intent(Manage_Decks_Page.this, Create_Deck_Page.class);
            startActivity(createDeck);
        });

        findViewById(R.id.ivManageBack).setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        deckList = dbHandler.getDeck();
        deckAdapter.notifyDataSetChanged();
        RecyclerView recyclerView = findViewById(R.id.rvDeckButton);
        DeckAdapter mAdapter = new DeckAdapter(deckList, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
