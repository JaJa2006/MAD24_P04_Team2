package com.example.main_activity;

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
        if (deck.deckId == 1) {
            holder.deckName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent createDeck = new Intent(v.getContext(),Create_Deck_Page.class);
                    v.getContext().startActivity(createDeck);
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
    public int getItemCount() {
        return data.size();
    }

}
