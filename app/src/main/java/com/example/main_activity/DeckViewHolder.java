package com.example.main_activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class DeckViewHolder extends RecyclerView.ViewHolder{
    TextView deckName;
    public DeckViewHolder(View itemView) {
        super(itemView);
        deckName = itemView.findViewById(R.id.tvDeck);
    }
}
