package com.example.main_activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeckAdapter extends RecyclerView.Adapter<DeckViewHolder> {
    private ArrayList<Deck> data;
    private Context context;

    public DeckAdapter(ArrayList<Deck> input, Context context) {
        this.data = input;
        this.context = context;
    }

    @Override
    public DeckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.deck_list, parent, false);
        return new DeckViewHolder(item);
    }

    @Override
    public void onBindViewHolder(DeckViewHolder holder, int position) {
        Deck deck = data.get(position);
        holder.deckName.setText(deck.deckName);

        holder.deckName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();
                for (Flashcard card : deck.Cardlist) {
                    String cardString = card.cardName + ',' + card.front + ',' + card.back + ',' + card.cardId;
                    list.add(cardString);
                }

                String cards = String.join("/", list);

                Intent ReviewCardPage = new Intent(context, Review_Card_Page.class);
                ReviewCardPage.putExtra("Deck", cards);
                context.startActivity(ReviewCardPage);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeckDatabaseHandler dbHandler = new DeckDatabaseHandler(context, null, null, 1);
                dbHandler.DeleteDeck(deck);
                data.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, data.size());
                Toast.makeText(context, "Deck deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
