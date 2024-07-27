package com.example.main_activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Review_Card_Page extends AppCompatActivity {

    private TextView tvQuestion;
    private ImageView ivDrawing;
    private TextView tvAnswerSection;
    private TextView tvAnswer;
    private TextView tvShowAnswer;
    private ImageView ivPreviousCard;
    private ImageView ivNextCard;
    private int currentCardIndex = 0;
    private ArrayList<Flashcard> flashcards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_card_page);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String deckString = intent.getStringExtra("Deck");
        flashcards = parseDeckString(deckString);

        tvQuestion = findViewById(R.id.tvQuestion);
        ivDrawing = findViewById(R.id.ivDrawing);
        tvAnswerSection = findViewById(R.id.tvAnswerSection);
        tvAnswer = findViewById(R.id.tvAnswer);
        tvShowAnswer = findViewById(R.id.tvShowAnswer);
        ivPreviousCard = findViewById(R.id.ivPreviousCard);
        ivNextCard = findViewById(R.id.ivNextCard);

        displayCard(currentCardIndex);

        tvShowAnswer.setOnClickListener(v -> {
            tvAnswerSection.setVisibility(View.VISIBLE);
            tvAnswer.setVisibility(View.VISIBLE);
            tvShowAnswer.setVisibility(View.GONE);
        });

        ivPreviousCard.setOnClickListener(v -> {
            if (currentCardIndex > 0) {
                currentCardIndex--;
                displayCard(currentCardIndex);
            }
        });

        ivNextCard.setOnClickListener(v -> {
            if (currentCardIndex < flashcards.size() - 1) {
                currentCardIndex++;
                displayCard(currentCardIndex);
            }
        });

        findViewById(R.id.ivReviewBack).setOnClickListener(v -> finish());
    }

    private void displayCard(int index) {
        Flashcard card = flashcards.get(index);
        tvQuestion.setText(card.front);
        tvAnswer.setText(card.back);
        tvAnswerSection.setVisibility(View.GONE);
        tvAnswer.setVisibility(View.GONE);
        tvShowAnswer.setVisibility(View.VISIBLE);
        if (card.drawing != null) {
            ivDrawing.setImageBitmap(card.drawing);
        } else {
            ivDrawing.setImageBitmap(null);
        }
    }

    private ArrayList<Flashcard> parseDeckString(String deckString) {
        ArrayList<Flashcard> flashcards = new ArrayList<>();
        String[] cards = deckString.split("/");
        for (String card : cards) {
            String[] parts = card.split(",", 4);
            String cardName = parts[0];
            String front = parts[1];
            String back = parts[2];
            Bitmap drawing = decodeBase64ToBitmap(parts[3]);
            flashcards.add(new Flashcard(cardName, front, back, drawing));
        }
        return flashcards;
    }

    private Bitmap decodeBase64ToBitmap(String base64Str) {
        if (base64Str.isEmpty()) return null;
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
