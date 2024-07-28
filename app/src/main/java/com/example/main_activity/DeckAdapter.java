package com.example.main_activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DeckAdapter extends RecyclerView.Adapter<DeckViewHolder> {
    ArrayList<Deck> data;
    Context context;

    private static final String CARD_DELIMITER = ";;;";
    private static final String FIELD_DELIMITER = ":::";

    public DeckAdapter(ArrayList<Deck> input, Context context) {
        this.data = input;
        this.context = context;
    }

    @Override
    public DeckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.deck_list, parent, false);
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
                for (Flashcard flashcard : deck.Cardlist) {
                    String encodedBitmap = bitmapToString(flashcard.drawing);
                    String card = flashcard.cardName + FIELD_DELIMITER + flashcard.front + FIELD_DELIMITER + flashcard.back + FIELD_DELIMITER + encodedBitmap;
                    list.add(card);
                }
                String cards = String.join(CARD_DELIMITER, list);
                Intent ReviewCardPage = new Intent(context, Review_Card_Page.class);
                ReviewCardPage.putExtra("Deck", cards);
                context.startActivity(ReviewCardPage);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeckDatabaseHandler dbHandler = new DeckDatabaseHandler(context);
                dbHandler.DeleteDeck(deck);
                Intent refresh = new Intent(context, Manage_Decks_Page.class);
                refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(refresh);
                ((Manage_Decks_Page) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
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
}
