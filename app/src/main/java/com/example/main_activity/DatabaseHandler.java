package com.example.main_activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table with username as primary key
        String SQL_CREATE_TABLE = "CREATE TABLE Users (" +
                "username TEXT PRIMARY KEY," +
                "email TEXT," +
                "password TEXT)";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table
        db.execSQL("DROP TABLE IF EXISTS Users");

        // Recreate the table with the new schema
        onCreate(db);
    }
    public User checkuser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            //the ? is like for i in range in python so it changes value
            //this searches through the database to see if username is inside
            String query = "SELECT * FROM Users WHERE username=?";
            cursor = db.rawQuery(query, new String[]{username});

            // If cursor has rows, retrieve user data and return a User object
            //check if cursor contains any data
            //movetofirst checks if it contains data
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex("username");
                int emailIndex = cursor.getColumnIndex("email");
                int passwordIndex = cursor.getColumnIndex("password");

                //make sure that the fields can be found
                //since .getcolumnindex will return -1 if not found
                if (nameIndex != -1 && emailIndex != -1 && passwordIndex != -1) {
                    String name = cursor.getString(nameIndex);
                    String email = cursor.getString(emailIndex);
                    String password = cursor.getString(passwordIndex);
                    return new User(name, password, email);
                } else {
                    // Handle the case where column indices are not valid
                    return null;
                }
            } else {
                // Username does not exist, return null
                return null;
            }

        } finally {
            //close cursor if it contains data
            if (cursor != null) {
                cursor.close();
            }
            //close the database
            db.close();
        }
    }
    public void addUser(User user) {
        //makes it able to write to database
        SQLiteDatabase db = this.getWritableDatabase();

        //creation of a new row to add to the database
        ContentValues values = new ContentValues();
        values.put("username", user.getName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());

        // Insert the new row
        db.insert("Users", null, values);

        // Close the database
        db.close();
    }
}
