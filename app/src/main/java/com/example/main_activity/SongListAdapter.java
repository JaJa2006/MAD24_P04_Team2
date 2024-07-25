package com.example.main_activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
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
    MediaPlayer myMPlayer;
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
        // get the database
        MusicPlaylistDatabaseHandler dbHandler = new MusicPlaylistDatabaseHandler(holder.itemView.getContext(), null, null, 1);
        MusicPlaylist musicPlaylist = dbHandler.getPlaylistFromID(playlistID);
        // get the song indicator
        ArrayList<String> SongIndicatorList = new ArrayList<String>();
        if (musicPlaylist.SongIndicator.matches("")) {
        } else {
            String [] Songindicator = musicPlaylist.SongIndicator.split("`");
            SongIndicatorList = new ArrayList<String>(Arrays.asList(Songindicator));
        }
        // set the music playlist data to the recycler view
        holder.Song.setText(SongName);
        // set the song indicator
        holder.SongIndicator.setBackgroundResource(
                (SongIndicatorList.get(holder.getAdapterPosition()).matches("good")) ? R.drawable.song_indicator_good :
                        (SongIndicatorList.get(holder.getAdapterPosition()).matches("medium")) ? R.drawable.song_indicator_medium :
                                R.drawable.song_indicator_bad);
        holder.SongIndicator.setText((SongIndicatorList.get(holder.getAdapterPosition()).matches("good")) ? "+" :
                (SongIndicatorList.get(holder.getAdapterPosition()).matches("medium")) ? "=" : "-");
        // handle the testing of music when you tap the song name
        holder.Song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get song uri data
                ArrayList<String> SongURIList = new ArrayList<String>();
                if (musicPlaylist.SongsURI.matches("")) {
                } else {
                    String [] SongURI = musicPlaylist.SongsURI.split("`");
                    SongURIList = new ArrayList<String>(Arrays.asList(SongURI));
                }
                // play the music
                myMPlayer = new MediaPlayer();
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
    public void clearMediaPlayer() {
        if(myMPlayer!=null){
            myMPlayer.stop();
            myMPlayer.release(); // clear the memory
            myMPlayer = null;
        }
    }
}
