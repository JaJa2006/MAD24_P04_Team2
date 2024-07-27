package com.example.main_activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SongListAdapter extends RecyclerView.Adapter<SongListViewHolder>{
    boolean isVisible = false;
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
        // get the indicator value
        String indicator = (SongIndicatorList.get(holder.getAdapterPosition()).matches("good")) ? "good" :
                (SongIndicatorList.get(holder.getAdapterPosition()).matches("medium")) ? "medium" :"bad";
        // set the view with the music playlist data
        holder.Song.setText(SongName);
        holder.DeleteSong.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        // set the song indicator
        holder.SongIndicator.setBackgroundResource(
                (indicator.matches("good")) ? R.drawable.song_indicator_good :
                        (indicator.matches("medium")) ? R.drawable.song_indicator_medium :
                                R.drawable.song_indicator_bad);
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
                // create the alert
                AlertDialog alertDialog = new MaterialAlertDialogBuilder(v.getContext())
                        .setTitle("Delete Song?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // create instant of dbhandler and delete Song from there
                                MusicPlaylistDatabaseHandler dbHandler = new MusicPlaylistDatabaseHandler(v.getContext(), null, null, 1);
                                dbHandler.DeleteSongs(playlistID,holder.getAdapterPosition());
                                // refresh the page
                                Context context = holder.DeleteSong.getContext();
                                Intent refresh = new Intent(context,Manage_Playlist.class);
                                refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                context.startActivity(refresh);
                                ((Manage_Playlist)context).finish();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // set the cancel button to close the dialog
                                dialog.dismiss();
                            }
                        }).create();
                // show the alert
                alertDialog.show();
            }
        });
        // set indicator text
        holder.SongIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = (indicator.matches("good")) ? "This song is a good song for studying." :
                        (indicator.matches("medium")) ? "This song is average for studying." :
                                "This song is bad for studying.";
                // create the alert to show the info
                AlertDialog alertDialog = new MaterialAlertDialogBuilder(v.getContext())
                        .setTitle("Song Indicator")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                // show the alert
                alertDialog.show();
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
    // function to change the visibility of th delete button
    public void DeleteSongVisibility() {
        isVisible = !isVisible;
        notifyDataSetChanged();
    }
}
