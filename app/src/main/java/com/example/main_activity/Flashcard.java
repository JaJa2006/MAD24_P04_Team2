package com.example.main_activity;

import android.graphics.Bitmap;

public class Flashcard {
    public String cardName;
    public String front;
    public String back;
    public Bitmap drawing;
    public int cardId;

    public Flashcard(String cardName, String front, String back, Bitmap drawing) {
        this.cardName = cardName;
        this.front = front;
        this.back = back;
        this.drawing = drawing;
    }

    public Flashcard(String cardName, String front, String back, int cardId) {
        this.cardName = cardName;
        this.front = front;
        this.back = back;
        this.cardId = cardId;
    }
}
