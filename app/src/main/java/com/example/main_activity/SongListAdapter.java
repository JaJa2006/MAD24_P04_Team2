package com.example.main_activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SongListAdapter extends RecyclerView.Adapter<SongListViewHolder>{
    ArrayList<String> data;
    int playlistID;
    // playlist adapter to get the data
    public SongListAdapter(MusicPlaylist input, Context context) {
        // set the id
        playlistID = input.PlaylistID;
        // get the number of songs
        ArrayList<String> SongnamesList = new ArrayList<String>();
        if (input.SongNames.matches("")) {
        } else {
            String [] Songnames = input.SongNames.split("`");
            SongnamesList = new ArrayList<String>(Arrays.asList(Songnames));
        }
        this.data = SongnamesList;
    }
    // music playlist view holder to hold the layout
    public SongListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.song_list, parent, false);
        return new SongListViewHolder(item);
    }
    // setting all the buttons of the layout
    public void onBindViewHolder(SongListViewHolder holder, int position) {
        // get the SongName data
        String SongName = data.get(position);
        // set the music playlist data to the recycler view
        holder.Song.setText(SongName);
        // handle the testing of music when you tap the song name
        holder.Song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get song uri data
                MusicPlaylistDatabaseHandler dbHandler = new MusicPlaylistDatabaseHandler(v.getContext(), null, null, 1);
                MusicPlaylist musicPlaylist = dbHandler.getPlaylistFromID(playlistID);
                ArrayList<String> SongURIList = new ArrayList<String>();
                if (musicPlaylist.SongsURI.matches("")) {
                } else {
                    String [] Songnames = musicPlaylist.SongsURI.split("`");
                    SongURIList = new ArrayList<String>(Arrays.asList(Songnames));
                }
                // play the music
                MediaPlayer myMPlayer = new MediaPlayer();
                myMPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    myMPlayer.setDataSource(v.getContext(), Uri.parse(SongURIList.get(holder.getAdapterPosition())));
                    myMPlayer.prepare();
                } catch (IOException e) {
                    Toast.makeText(v.getContext(),"No Audio", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }
                myMPlayer.start();
            }
        });
        // handle deleting of playlist text
        holder.DeleteSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create instant of dbhandler and delete playlist from there
                MusicPlaylistDatabaseHandler dbHandler = new MusicPlaylistDatabaseHandler(v.getContext(), null, null, 1);
                dbHandler.DeleteSongs(playlistID,holder.getAdapterPosition());
                // refresh the page
                Context context = holder.DeleteSong.getContext();
                Intent refresh = new Intent(context,Manage_Playlist.class);
                refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(refresh);
                ((Manage_Playlist)context).finish();
            }
        });
    }
    public int getItemCount() {
        // get the number of songs
        return data.size();
    }
}
