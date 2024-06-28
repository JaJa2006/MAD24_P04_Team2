package com.example.main_activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MemoDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "memoDB.db";
    public static final String TABLE_MEMOS = "memo";
    public static final String COLUMN_MEMO_CONTENT = "memoContent";
    public static final String COLUMN_ISIMAGE = "isImage";
    public static final String COLUMN_MEMO_ID = "id";

    public MemoDatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    // crete database table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_MEMOS +
                "(" + COLUMN_MEMO_CONTENT + " TEXT," + COLUMN_ISIMAGE + " TEXT,"
                + COLUMN_MEMO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }
    // update database if have new one
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMOS);
        onCreate(db);
    }
    // add memo function to add the memo into the database
    public void addMemo(Memo memo) {
        ContentValues values = new ContentValues();
        // put deck name in data base
        values.put(COLUMN_MEMO_CONTENT, memo.MemoContent);
        values.put(COLUMN_ISIMAGE, memo.isImage);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_MEMOS, null, values);
    }
    public ArrayList<Memo> getMemo() {
        // get the deck from the database table
        ArrayList<Memo> Memos = new ArrayList<Memo>();
        String query = "SELECT * FROM " + TABLE_MEMOS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Memo memo = new Memo();
            // get memo content
            memo.MemoContent = cursor.getString(0);
            // get is image
            memo.isImage = cursor.getString(1);
            // get memo id
            memo.MemoID = Integer.parseInt(cursor.getString(2));
            Memos.add(memo);
            cursor.moveToNext();
        }
        cursor.close();
        return Memos;
    }
    // delete memo
    public boolean DeleteMemo(Memo memo) {
        boolean result = false;
        // find the deck from with the deck id
        String query = "SELECT * FROM " + TABLE_MEMOS + " WHERE "
                + COLUMN_MEMO_ID + " = \""
                + memo.MemoID + "\"";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        // go to the deck position and delete the memo
        Memo deleteMemo = new Memo();
        if (cursor.moveToFirst()) {
            deleteMemo.MemoID = Integer.parseInt(cursor.getString(2));
            db.delete(TABLE_MEMOS, COLUMN_MEMO_ID + " = ?",
                    new String[] { String.valueOf(deleteMemo.MemoID) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}
