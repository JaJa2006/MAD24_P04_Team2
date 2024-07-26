package com.example.main_activity;

import static android.content.Intent.getIntent;
import static androidx.core.content.ContextCompat.startActivity;
import static androidx.core.content.IntentCompat.getSerializableExtra;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import java.util.ArrayList;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
public class DeckAdapter extends RecyclerView.Adapter<DeckViewHolder>{
    ArrayList<Deck> data;
    //Feature 3 - R
    private Context context;
    private DeckDatabaseHandler dbHandler;

    // Deck adapter to get the data
    public DeckAdapter(ArrayList<Deck> input, Context context) {
        this.data = input;
        this.dbHandler = new DeckDatabaseHandler(context);
        this.context = context;

    }

    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.deck_list, parent, false);
        return new DeckViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        Deck deck = data.get(position);
        holder.deckName.setText(deck.deckName);

        holder.deckName.setOnClickListener(v -> {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < deck.Cardlist.size(); i++) {
                String card = deck.Cardlist.get(i).cardName + ',' +
                        deck.Cardlist.get(i).front + ',' +
                        deck.Cardlist.get(i).back + ',' +
                        deck.Cardlist.get(i).cardId;
                list.add(card);
            }
            String cards = String.join("/", list);
            Intent reviewCardPage = new Intent(context, Review_Card_Page.class);
            reviewCardPage.putExtra("Deck", cards);
            context.startActivity(reviewCardPage);



        // Setup swipe-to-delete
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Deck deletedDeck = data.get(position);
                data.remove(position);
                notifyItemRemoved(position);
                dbHandler.DeleteDeck(deletedDeck);

                Snackbar snackbar = Snackbar.make(((Manage_Decks_Page) context).findViewById(android.R.id.content), "Deck deleted", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", v -> {
                    data.add(position, deletedDeck);
                    notifyItemInserted(position);
                    dbHandler.addDeck(deletedDeck);
                });
                snackbar.show();
            }
        };

        // Attach ItemTouchHelper to RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(((Manage_Decks_Page) context).findViewById(R.id.rvDeckButton));
    });


    // deck view holder to hold the layout


    }
    public int getItemCount() {
        return data.size();
    }

}