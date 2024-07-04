package com.example.main_activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

// viewholder for the recycler view containing the music playlist
public class MusicPlaylistViewHolder extends RecyclerView.ViewHolder{
    TextView PlaylistName;
    ImageView DeletePlaylist;
    public MusicPlaylistViewHolder(View itemView) {
        super(itemView);
        PlaylistName = itemView.findViewById(R.id.tvPlaylistName);
        DeletePlaylist = itemView.findViewById(R.id.ivDeletePlaylist);
    }
}
