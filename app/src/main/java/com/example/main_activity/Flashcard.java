package com.example.main_activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
// flashcard class
public class Flashcard {
    public String cardName;
    public String front;
    public String back;
    public int cardId;
    public Flashcard(String cardName, String front, String back, int cardId) {
        this.cardName = cardName;
        this.front = front;
        this.back = back;
        this.cardId = cardId;
    }
    public Flashcard(String cardName, String front, String back) {
        this.cardName = cardName;
        this.front = front;
        this.back = back;
    }
}

