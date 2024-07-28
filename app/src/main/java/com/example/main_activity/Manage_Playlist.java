package com.example.main_activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Manage_Playlist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_playlist);

        // Retrieve the data passed from the intent (if any)
        String playlistID = getIntent().getStringExtra("PlaylistID");
        String playlistName = getIntent().getStringExtra("PlaylistName");

        // Use the retrieved data as needed
        // For now, we'll just print them to the console
        System.out.println("Playlist ID: " + playlistID);
        System.out.println("Playlist Name: " + playlistName);
    }
}
