package com.example.main_activity;

import java.util.ArrayList;

public class Deck {
    public String deckName;
    public ArrayList<Flashcard> Cardlist;
    public int deckId;
    public Deck() {
    }
    public Deck(String deckName, ArrayList<Flashcard> Cardlist) {
        this.deckName = deckName;
        this.Cardlist = Cardlist;
    }
    public Deck(String deckName, ArrayList<Flashcard> Cardlist, int deckId) {
        this.deckName = deckName;
        this.Cardlist = Cardlist;
        this.deckId = deckId;
    }
}
