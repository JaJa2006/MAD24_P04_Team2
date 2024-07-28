package com.example.main_activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class DeckViewHolder extends RecyclerView.ViewHolder {
    TextView deckName;
    ImageView delete;

    public DeckViewHolder(View itemView) {
        super(itemView);
        deckName = itemView.findViewById(R.id.tvDeck);
        delete = itemView.findViewById(R.id.tvDelete);
    }
}
