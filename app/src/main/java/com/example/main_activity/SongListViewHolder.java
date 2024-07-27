package com.example.main_activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

// viewholder for the recycler view containing the songs in the playlist
public class SongListViewHolder extends RecyclerView.ViewHolder{
    TextView Song;
    TextView SongIndicator;
    ImageView DeleteSong;
    public SongListViewHolder(View itemView) {
        super(itemView);
        Song = itemView.findViewById(R.id.tvSong);
        SongIndicator = itemView.findViewById(R.id.tvSongIndicator);
        DeleteSong = itemView.findViewById(R.id.ivDeleteSong);
    }
}
