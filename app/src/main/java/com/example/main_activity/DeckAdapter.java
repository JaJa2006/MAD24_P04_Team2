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

public class DeckAdapter extends RecyclerView.Adapter<DeckViewHolder>{
    ArrayList<Deck> data;
    // Deck adapter to get the data
    public DeckAdapter(ArrayList<Deck> input, Context context) {
        this.data = input;
    }
    // deck view holder to hold the layout
    public DeckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.deck_list, parent, false);
        return new DeckViewHolder(item);
    }
    // setting all the buttons of the layout
    public void onBindViewHolder(DeckViewHolder holder, int position) {
        // get the deck data
        Deck deck = data.get(position);
        // set the deck name
        holder.deckName.setText(deck.deckName);
        // to allow for reviewing cards
        holder.deckName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the get the card list from the deck and convert them into a list of strings
                ArrayList<String> list = new ArrayList<String>();
                for (int i = 0;i<deck.Cardlist.size();i++){
                    String card = deck.Cardlist.get(i).cardName+','+deck.Cardlist.get(i).front+','+deck.Cardlist.get(i).back+','+deck.Cardlist.get(i).cardId;
                    list.add(card);
                }
                // Convert the list of strings into one string
                String cards = String.join("/",list);
                // send the string into the reveiw card page
                Context context = holder.deckName.getContext();
                Intent ReviewCardPage = new Intent(context, Review_Card_Page.class);
                ReviewCardPage.putExtra("Deck",cards);
                context.startActivity(ReviewCardPage);
            }
        });
        // handle deleating of decks
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create instant of dbhandler and delete deck from there
                DeckDatabaseHandler dbHandler = new DeckDatabaseHandler(v.getContext(), null, null, 1);
                dbHandler.DeleteDeck(deck);
                // refresh the page
                Context context = holder.delete.getContext();
                Intent refresh = new Intent(context, Manage_Decks_Page.class);
                context.startActivity(refresh);
                ((Manage_Decks_Page)context).finish();
            }
        });
    }
    public int getItemCount() {
        return data.size();
    }

}
