package com.example.main_activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class MusicPlaylistDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PlaylistDB.db";
    public static final String TABLE_PLAYLIST = "Playlist";
    public static final String COLUMN_PLAYLIST_NAME = "PlaylistName";
    public static final String COLUMN_SONGS = "SongsURI";
    public static final String COLUMN_SONG_NAMES = "SongNames";
    public static final String COLUMN_PLAYLIST_ID = "id";

    public MusicPlaylistDatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    // crete database table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PLAYLIST +
                "(" + COLUMN_PLAYLIST_NAME + " TEXT," + COLUMN_SONGS + " TEXT," + COLUMN_SONG_NAMES + " TEXT,"
                + COLUMN_PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }
    // update database if have new one
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST);
        onCreate(db);
    }
    // add memo function to add the memo into the database
    public void addPlaylist(MusicPlaylist musicPlaylist) {
        ContentValues values = new ContentValues();
        // put data in data base
        values.put(COLUMN_PLAYLIST_NAME, musicPlaylist.PlaylistName);
        values.put(COLUMN_SONGS, musicPlaylist.SongsURI);
        values.put(COLUMN_SONG_NAMES, musicPlaylist.SongNames);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PLAYLIST, null, values);
    }
    public ArrayList<MusicPlaylist> getPlaylist() {
        // get the playlist from the database table
        ArrayList<MusicPlaylist> Playlists = new ArrayList<MusicPlaylist>();
        String query = "SELECT * FROM " + TABLE_PLAYLIST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            MusicPlaylist playlist = new MusicPlaylist();
            // get playlist name
            playlist.PlaylistName = cursor.getString(0);
            // get song uri
            playlist.SongsURI = cursor.getString(1);
            // get song names
            playlist.SongNames = cursor.getString(2);
            // get playlist id
            playlist.PlaylistID = Integer.parseInt(cursor.getString(3));
            Playlists.add(playlist);
            cursor.moveToNext();
        }
        cursor.close();
        return Playlists;
    }
    // get playlist from id
    public MusicPlaylist getPlaylistFromID(int playlistID) {
        // find the playlist from with the playlist id
        String query = "SELECT * FROM " + TABLE_PLAYLIST + " WHERE "
                + COLUMN_PLAYLIST_ID + " = \""
                + playlistID + "\"";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        // go to the deck position and delete the memo
        MusicPlaylist Playlist = new MusicPlaylist();
        if (cursor.moveToFirst()) {
            Playlist.PlaylistName = cursor.getString(0);
            // get song uri
            Playlist.SongsURI = cursor.getString(1);
            // get song names
            Playlist.SongNames = cursor.getString(2);
            // get playlist id
            Playlist.PlaylistID = Integer.parseInt(cursor.getString(3));
            cursor.close();
        }
        db.close();
        return Playlist;
    }
    // delete playlist
    public boolean DeletePlaylist(MusicPlaylist playlist) {
        boolean result = false;
        // find the deck from with the deck id
        String query = "SELECT * FROM " + TABLE_PLAYLIST + " WHERE "
                + COLUMN_PLAYLIST_ID + " = \""
                + playlist.PlaylistID + "\"";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        // go to the deck position and delete the memo
        MusicPlaylist deletePlaylist = new MusicPlaylist();
        if (cursor.moveToFirst()) {
            deletePlaylist.PlaylistID = Integer.parseInt(cursor.getString(3));
            db.delete(TABLE_PLAYLIST, COLUMN_PLAYLIST_ID + " = ?",
                    new String[] { String.valueOf(deletePlaylist.PlaylistID) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
    // delete songs
    public void DeleteSongs(int playlistID, int songPosition) {
        // find the deck from with the deck id
        String query = "SELECT * FROM " + TABLE_PLAYLIST + " WHERE "
                + COLUMN_PLAYLIST_ID + " = \""
                + playlistID + "\"";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        // go to the deck position and delete the memo
        MusicPlaylist deletePlaylist = new MusicPlaylist();
        if (cursor.moveToFirst()) {
            deletePlaylist.PlaylistName = cursor.getString(0);
            // get song uri
            deletePlaylist.SongsURI = cursor.getString(1);
            // get song names
            deletePlaylist.SongNames = cursor.getString(2);
            // get playlist id
            deletePlaylist.PlaylistID = Integer.parseInt(cursor.getString(3));
            cursor.close();
        }
        if (deletePlaylist.SongNames.contains("`")){
            // if there are multiple songs in the list
            // getting the songs from string to arraylist format
            String [] Songnames = deletePlaylist.SongNames.split("`");
            ArrayList<String> SongnamesList = new ArrayList<String>(Arrays.asList(Songnames));
            String [] SongURI = deletePlaylist.SongNames.split("`");
            ArrayList<String> SongURIList = new ArrayList<String>(Arrays.asList(SongURI));
            // removing the song
            SongnamesList.remove(songPosition);
            SongURIList.remove(songPosition);
            // joining the strings together
            deletePlaylist.SongNames = String.join("`",SongnamesList);
            deletePlaylist.SongsURI = String.join("`",SongURIList);
        } else {
            // if there is only one song in the list, delete it
            deletePlaylist.SongNames = "";
            deletePlaylist.SongsURI = "";
        }
        // update the table
        ContentValues values = new ContentValues();
        values.put(COLUMN_SONGS,deletePlaylist.SongsURI);
        values.put(COLUMN_SONG_NAMES,deletePlaylist.SongNames);

        db.update(TABLE_PLAYLIST, values, COLUMN_PLAYLIST_ID + " = " + deletePlaylist.PlaylistID, null );

        db.close();
    }
    public void AddSong(int playlistID, String SongsURI, String SongNames) {
        SQLiteDatabase db = this.getWritableDatabase();
        // update the table
        ContentValues values = new ContentValues();
        values.put(COLUMN_SONGS,SongsURI);
        values.put(COLUMN_SONG_NAMES,SongNames);

        db.update(TABLE_PLAYLIST, values, COLUMN_PLAYLIST_ID + " = " + playlistID, null );

        db.close();
    }
}
