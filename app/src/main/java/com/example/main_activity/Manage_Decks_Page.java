package com.example.main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
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
        });

        findViewById(R.id.ivManageBack).setOnClickListener(v -> finish());
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_decks_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Setting up the add deck for the button in the class
        TextView AddCardButton = findViewById(R.id.tvAddCard);
        ImageView ivBack = findViewById(R.id.ivManageBack);
        // add card button to go to the create deck page to start the deck making
        AddCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create intent to go to create deck page
                Intent createDeck = new Intent(v.getContext(), Create_Deck_Page.class);
                v.getContext().startActivity(createDeck);
            }
        });

        // back button to go back to the main activity page
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    // update the page with the data base when the page is reloaded
    @Override
    protected void onResume() {
        super.onResume();
        // get the deck from the data base
        DeckDatabaseHandler dbHandler = new DeckDatabaseHandler(Manage_Decks_Page.this, null, null, 1);
        ArrayList<Deck> decks = dbHandler.getDeck();
        // get the recyclerview from the XML
        RecyclerView recyclerView = findViewById(R.id.rvDeckButton);
        // fill the layout with the information from the data base
        DeckAdapter mAdapter = new DeckAdapter(decks,this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}