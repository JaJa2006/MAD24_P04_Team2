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


public class DeckDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "deckDB.db";
    public static final String TABLE_DECKS = "Deck";
    public static final String COLUMN_DECK_NAME = "name";
    public static final String COLUMN_DECK_CARDS = "cards";
    public static final String COLUMN_DECK_ID = "id";
    public DeckDatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_DECKS +
                "(" + COLUMN_DECK_NAME + " TEXT," + COLUMN_DECK_CARDS + " TEXT,"
                + COLUMN_DECK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DECKS);
        onCreate(db);
    }
    public void addDeck(Deck deck) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DECK_NAME, deck.deckName);
        JSONArray jsonArray = new JSONArray(deck.Cardlist);
        for (int i=0; i < deck.Cardlist.size(); i++) {
            jsonArray.put(deck.Cardlist.get(i).getJSONObject());
        }
        values.put(COLUMN_DECK_CARDS, jsonArray.toString());
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_DECKS, null, values);
    }
    public ArrayList<Deck> getDeck() {
        ArrayList<Deck> Decks = new ArrayList<Deck>();
        String query = "SELECT * FROM " + TABLE_DECKS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Deck deck = new Deck();
            deck.deckName = cursor.getString(0);
            String cards = cursor.getString(1);
            ArrayList<Flashcard> Cards = new ArrayList<Flashcard>();
            try {
                JSONArray jsonArray = new JSONArray(cards);
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject card = jsonArray.getJSONObject(i);
                    Flashcard flashcard = new Flashcard(card.getString("CardName"),card.getString("Front"),card.getString("Back"),card.getInt("Id"));
                    Cards.add(flashcard);
                }
            } catch (JSONException e) {
            }
            deck.deckId = Integer.parseInt(cursor.getString(2));
            Decks.add(deck);
            cursor.moveToNext();
        }
        cursor.close();
        return Decks;
    }

    public boolean DeleteDeck(Deck deck) {
        boolean result = false;

        String query = "SELECT * FROM " + TABLE_DECKS + " WHERE "
                + COLUMN_DECK_ID + " = \""
                + deck.deckId + "\"";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

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
