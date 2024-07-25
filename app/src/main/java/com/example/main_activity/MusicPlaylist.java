package com.example.main_activity;

import java.util.ArrayList;

public class MusicPlaylist {
    // class for the music playlist
    public int PlaylistID;
    public String PlaylistName;
    public String SongsURI;
    public String SongNames;
    public String SongIndicator;
    public String Selected;
    public MusicPlaylist() {
    }
    public MusicPlaylist(String PlaylistName,String SongsURI, String SongNames, String SongIndicator, String Selected) {
        this.PlaylistName = PlaylistName;
        this.SongsURI = SongsURI;
        this.SongNames = SongNames;
        this.SongIndicator = SongIndicator;
        this.Selected = Selected;
    }
}
