package com.example.main_activity;

import java.util.ArrayList;

public class MusicPlaylist {
    public int PlaylistID;
    public String PlaylistName;
    public String SongsURI;
    public String SongNames;
    public String Selected;
    public MusicPlaylist() {
    }
    public MusicPlaylist(String PlaylistName,String SongsURI, String SongNames, String Selected) {
        this.PlaylistName = PlaylistName;
        this.SongsURI = SongsURI;
        this.SongNames = SongNames;
        this.Selected = Selected;
    }
}
