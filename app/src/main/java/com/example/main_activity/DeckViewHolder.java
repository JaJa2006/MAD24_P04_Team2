package com.example.main_activity;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
// viewholder for the recycler view containing the decks
public class DeckViewHolder extends RecyclerView.ViewHolder{
    public TextView deckName;
    ImageView delete;
    public DeckViewHolder(View itemView) {
        super(itemView);
        deckName = itemView.findViewById(R.id.tvDeck);
        delete = itemView.findViewById(R.id.tvDelete);
    }
}
