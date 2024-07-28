package com.example.main_activity;

import java.util.ArrayList;

public class Deck {
    public String deckName;
    public ArrayList<Flashcard> Cardlist;
    public int deckId;

    public Deck(String deckName, ArrayList<Flashcard> cardlist) {
        this.deckName = deckName;
        Cardlist = cardlist;
    }

    public Deck() {
        Cardlist = new ArrayList<>();
    }
}
