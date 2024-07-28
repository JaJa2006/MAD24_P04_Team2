package com.example.main_activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class DeckDatabaseHandler extends SQLiteOpenHelper {
    private static final String TAG = "DeckDatabaseHandler";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "deckDB.db";
    public static final String TABLE_DECKS = "Deck";
    public static final String COLUMN_DECK_NAME = "name";
    public static final String COLUMN_DECK_CARDS = "cards";
    public static final String COLUMN_DECK_ID = "id";

    private static final String CARD_DELIMITER = ";;;";
    private static final String FIELD_DELIMITER = ":::";

    public DeckDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        ArrayList<String> list = new ArrayList<>();
        for (Flashcard flashcard : deck.Cardlist) {
            String encodedBitmap = bitmapToString(flashcard.drawing);
            String card = flashcard.cardName + FIELD_DELIMITER + flashcard.front + FIELD_DELIMITER + flashcard.back + FIELD_DELIMITER + encodedBitmap;
            list.add(card);
        }
        String cards = String.join(CARD_DELIMITER, list);
        values.put(COLUMN_DECK_CARDS, cards);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_DECKS, null, values);
        Log.d(TAG, "Deck added: " + deck.deckName + " with cards: " + cards);
    }

    public ArrayList<Deck> getDeck() {
        ArrayList<Deck> Decks = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_DECKS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Deck deck = new Deck();
            deck.deckName = cursor.getString(0);
            String cardStrings = cursor.getString(1);
            Log.d(TAG, "Retrieved deck: " + deck.deckName + " with card strings: " + cardStrings);
            ArrayList<Flashcard> flashcards = new ArrayList<>();
            String[] splitStrings = cardStrings.split(CARD_DELIMITER);
            ArrayList<String> list = new ArrayList<>(Arrays.asList(splitStrings));
            for (String s : list) {
                try {
                    String[] cardvalues = s.split(FIELD_DELIMITER);
                    if (cardvalues.length < 4) {
                        Log.e(TAG, "Invalid card format: " + Arrays.toString(cardvalues));
                        continue;
                    }
                    String cardName = cardvalues[0];
                    String front = cardvalues[1];
                    String back = cardvalues[2];
                    Bitmap drawing = stringToBitmap(cardvalues[3]);
                    Log.d(TAG, "Decoded bitmap for card: " + cardName);
                    Flashcard card = new Flashcard(cardName, front, back, drawing);
                    flashcards.add(card);
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing card data: " + e.getMessage(), e);
                }
            }
            deck.Cardlist = flashcards;
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
                    new String[]{String.valueOf(deleteDeck.deckId)});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    private String bitmapToString(Bitmap bitmap) {
        if (bitmap == null) {
            return "";
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            Log.e(TAG, "Error decoding bitmap: " + e.getMessage());
            return null;
        }
    }
}
