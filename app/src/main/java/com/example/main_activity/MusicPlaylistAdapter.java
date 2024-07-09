package com.example.main_activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicPlaylistAdapter extends RecyclerView.Adapter<MusicPlaylistViewHolder>{
    ArrayList<MusicPlaylist> data;
    // playlist adapter to get the data
    public MusicPlaylistAdapter(ArrayList<MusicPlaylist> input, Context context) {
        this.data = input;
    }
    // music playlist view holder to hold the layout
    public MusicPlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.music_playlist_list, parent, false);
        return new MusicPlaylistViewHolder(item);
    }
    // setting all the buttons of the layout
    public void onBindViewHolder(MusicPlaylistViewHolder holder, int position) {
        // get the playlist data
        MusicPlaylist musicPlaylist = data.get(position);
        // set the music playlist data to the recycler view
        holder.PlaylistName.setText(musicPlaylist.PlaylistName);
        // handle deleting of playlist text
        holder.DeletePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create instant of dbhandler and delete playlist from there
                MusicPlaylistDatabaseHandler dbHandler = new MusicPlaylistDatabaseHandler(v.getContext(), null, null, 1);
                dbHandler.DeletePlaylist(musicPlaylist);
                // refresh the page
                Context context = holder.DeletePlaylist.getContext();
                Intent refresh = new Intent(context,StudySessionPage.class);
                refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(refresh);
                ((StudySessionPage)context).finish();
            }
        });
        // allow users to click on the playlist to manage it
        holder.PlaylistName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send the string into the manage playlist page
                Context context = holder.PlaylistName.getContext();
                Intent ManagePlaylist = new Intent(context, Manage_Playlist.class);
                ManagePlaylist.putExtra("PlaylistID",musicPlaylist.PlaylistID);
                context.startActivity(ManagePlaylist);
            }
        });
        // allow user to select which playlist to play during the timer session
        holder.PlaylistSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // create instant of dbhandler and change selected to 1
                    MusicPlaylistDatabaseHandler dbHandler = new MusicPlaylistDatabaseHandler(buttonView.getContext(), null, null, 1);
                    dbHandler.ChangeSelected(musicPlaylist.PlaylistID,"1");
                } else {
                    // create instant of dbhandler and change selected to 0
                    MusicPlaylistDatabaseHandler dbHandler = new MusicPlaylistDatabaseHandler(buttonView.getContext(), null, null, 1);
                    dbHandler.ChangeSelected(musicPlaylist.PlaylistID,"0");
                }
            }
        });
    }
    public int getItemCount() {
        return data.size();
    }
}
