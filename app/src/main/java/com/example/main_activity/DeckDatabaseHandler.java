package com.example.main_activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class DeckDatabaseHandler extends SQLiteOpenHelper {
    // create database columns
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "deckDB.db";
    public static final String TABLE_DECKS = "Deck";
    public static final String COLUMN_DECK_NAME = "name";
    public static final String COLUMN_DECK_CARDS = "cards";
    public static final String COLUMN_DECK_ID = "id";
    public DeckDatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    // crete database table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_DECKS +
                "(" + COLUMN_DECK_NAME + " TEXT," + COLUMN_DECK_CARDS + " TEXT,"
                + COLUMN_DECK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }
    // update database if have new one
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DECKS);
        onCreate(db);
    }
    // add deck function to add the deck into the database
    public void addDeck(Deck deck) {
        ContentValues values = new ContentValues();
        // put deck name in data base
        values.put(COLUMN_DECK_NAME, deck.deckName);
        // convert the card list into a list of strings
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0;i<deck.Cardlist.size();i++){
            String card = deck.Cardlist.get(i).cardName+','+deck.Cardlist.get(i).front+','+deck.Cardlist.get(i).back+','+deck.Cardlist.get(i).cardId;
            list.add(card);
        }
        // convert the list of string into one string
        String cards = String.join("/",list);
        // put the string into the database
        values.put(COLUMN_DECK_CARDS, cards);
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_DECKS, null, values);
    }
    // get the deck from the data base
    public ArrayList<Deck> getDeck() {
        // get the deck from the database table
        ArrayList<Deck> Decks = new ArrayList<Deck>();
        String query = "SELECT * FROM " + TABLE_DECKS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Deck deck = new Deck();
            // get deck name
            deck.deckName = cursor.getString(0);
            // conver the string into a list of string
            String cardStrings = cursor.getString(1);
            ArrayList<Flashcard> flashcards = new ArrayList<Flashcard>();
            String[] splitStrings = cardStrings.split("/");
            ArrayList<String> list = new ArrayList<String>(Arrays.asList(splitStrings));
            for (int i = 0;i<list.size();i++){
                // for each string in the list of string, convert it to a flashcard
                String[] cardvalues = list.get(i).split(",");
                ArrayList<String> listCardValues = new ArrayList<String>(Arrays.asList(cardvalues));
                Flashcard card = new Flashcard(listCardValues.get(0),listCardValues.get(1),listCardValues.get(2),Integer.parseInt(listCardValues.get(3)));
                flashcards.add(card);
            }
            deck.Cardlist = flashcards;
            // get deck id
            deck.deckId = Integer.parseInt(cursor.getString(2));
            Decks.add(deck);
            cursor.moveToNext();
        }
        cursor.close();
        return Decks;
    }
    // delete deck
    public boolean DeleteDeck(Deck deck) {
        boolean result = false;
        // find the deck from with the deck id
        String query = "SELECT * FROM " + TABLE_DECKS + " WHERE "
                + COLUMN_DECK_ID + " = \""
                + deck.deckId + "\"";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        // go to the deck position and delete the deck
        Deck deleteDeck = new Deck();
        if (cursor.moveToFirst()) {
            deleteDeck.deckId = Integer.parseInt(cursor.getString(2));
            db.delete(TABLE_DECKS, COLUMN_DECK_ID + " = ?",
                    new String[] { String.valueOf(deleteDeck.deckId) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
