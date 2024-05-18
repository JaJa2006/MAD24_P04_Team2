package com.example.main_activity;

import java.util.ArrayList;

public class Flashcard {
    public String cardName;
    public String front;
    public String back;
    public int cardiId;
    public Flashcard(String cardName, String front, String back, int cardiId) {
        this.cardName = cardName;
        this.front = front;
        this.back = back;
        this.cardiId = cardiId;
    }
}
