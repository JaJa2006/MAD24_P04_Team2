package com.example.main_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class Manage_Playlist extends AppCompatActivity {

    ActivityResultLauncher<Intent> resultLauncher;
    TextView addAudio;
    String StringURI;
    Uri audioUri;
    TextInputEditText SongName;

    int PlaylistID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_playlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // recieve intent to get song name and song uri
        Intent receivingEnd = getIntent();
        PlaylistID = receivingEnd.getIntExtra("PlaylistID",-1);
        if (PlaylistID != -1) {
            SharedPreferences preferences = getSharedPreferences("ManagePlaylist",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("PlaylistID", PlaylistID);
            editor.apply();
        } else {
            SharedPreferences loadPreferences = getSharedPreferences("ManagePlaylist",MODE_PRIVATE);
            PlaylistID = loadPreferences.getInt("PlaylistID",-1);
        }
        // get the memo from the data base
        MusicPlaylistDatabaseHandler dbHandler = new MusicPlaylistDatabaseHandler(Manage_Playlist.this, null, null, 1);
        MusicPlaylist playlist = dbHandler.getPlaylistFromID(PlaylistID);
        // get the recyclerview from the XML
        RecyclerView recyclerView = findViewById(R.id.rvManagePlaylist);
        // fill the layout with the information from the data base
        SongListAdapter mAdapter = new SongListAdapter(playlist,this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        // get the elements from xml
        addAudio = findViewById(R.id.tvAddMusic);

        // call the register result function
        registerResult();

        // set on click listener for the add audio button
        addAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogLayout = LayoutInflater.from(Manage_Playlist.this).inflate(R.layout.add_music_dialog, null);
                SongName = dialogLayout.findViewById(R.id.etMusicName);
                AlertDialog alertDialog = new MaterialAlertDialogBuilder(Manage_Playlist.this)
                        .setTitle("Song Name")
                        .setView(dialogLayout)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (SongName.getText().toString().matches("")) {
                                    Toast.makeText(v.getContext(), "Please enter song name", Toast.LENGTH_SHORT).show();
                                } else {
                                    // if song name is not empty
                                    pickAudio();
                                    dialog.dismiss();
                                }

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                alertDialog.show();
            }
        });
    }
    // function to pick the Audio from files
    private void pickAudio(){
        Intent intent_upload = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent_upload);
    }
    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try{
                            // if have result, get image data as an uri
                            audioUri = result.getData().getData();
                            StringURI = audioUri.toString();
                            if (audioUri.getPath().startsWith("/document/audio:")) {
                                // It's an audio file
                                // create database handler to add song
                                MusicPlaylistDatabaseHandler dbHandler = new MusicPlaylistDatabaseHandler(Manage_Playlist.this, null, null, 1);
                                MusicPlaylist songlist =  dbHandler.getPlaylistFromID(PlaylistID);
                                if (songlist.SongNames.matches("")) {
                                    songlist.SongNames = SongName.getText().toString();
                                    songlist.SongsURI = StringURI;
                                } else {
                                    songlist.SongNames += "`"+SongName.getText().toString();
                                    songlist.SongsURI += "`"+StringURI;
                                }
                                Toast.makeText(Manage_Playlist.this,StringURI, Toast.LENGTH_SHORT).show();
                                // make the uri persistent and will last
                                getContentResolver().takePersistableUriPermission(audioUri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                dbHandler.AddSong(PlaylistID,songlist.SongsURI,songlist.SongNames);
                                // refresh the page
                                Intent refresh = new Intent(Manage_Playlist.this,Manage_Playlist.class);
                                refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(refresh);
                                finish();
                            } else {
                                // Not an audio file
                                Toast.makeText(Manage_Playlist.this,"Please select an audio", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(Manage_Playlist.this,"No Audio Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}