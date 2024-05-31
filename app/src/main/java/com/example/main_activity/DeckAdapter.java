package com.example.main_activity;

import static android.content.Intent.getIntent;
import static androidx.core.content.ContextCompat.startActivity;
import static androidx.core.content.IntentCompat.getSerializableExtra;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeckAdapter extends RecyclerView.Adapter<DeckViewHolder>{
    ArrayList<Deck> data;
    public DeckAdapter(ArrayList<Deck> input, Context context) {
        this.data = input;
    }

    public DeckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.deck_list, parent, false);
        return new DeckViewHolder(item);
    }
    public void onBindViewHolder(DeckViewHolder holder, int position) {
        Deck deck = data.get(position);
        holder.deckName.setText(deck.deckName);
        holder.deckName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.deckName.getContext();
                Intent ReviewCardPage = new Intent(context, Review_Card_Page.class);
                ReviewCardPage.putExtra("Deck",deck.Cardlist);
                context.startActivity(ReviewCardPage);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public int getItemCount() {
        return data.size();
    }

}
