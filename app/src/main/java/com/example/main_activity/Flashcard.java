package com.example.main_activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("CardName", cardName);
            obj.put("Front", front);
            obj.put("Back", back);
            obj.put("Id", cardId);
        } catch (JSONException e) {
        }
        return obj;
}
}
