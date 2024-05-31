package com.example.main_activity;

import static androidx.core.content.IntentCompat.getSerializableExtra;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Review_Card_Page extends AppCompatActivity {

    int Card = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review_card_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent receivingEnd = getIntent();
        ArrayList<Flashcard> flashcards = (ArrayList<Flashcard>) receivingEnd.getSerializableExtra("Deck");

        TextView tvQuestion = findViewById(R.id.tvQuestion);
        TextView tvSeperator = findViewById(R.id.tvSeperator);
        TextView tvAnswerSection = findViewById(R.id.tvAnswerSection);
        TextView tvAnswer = findViewById(R.id.tvAnswer);
        ImageView ivNextCard = findViewById(R.id.ivNextCard);
        ImageView ivPreviousCard = findViewById(R.id.ivPreviousCard);
        Button btnShowAnswer = findViewById(R.id.btnShowAnswer);


        tvQuestion.setText(flashcards.indexOf(Card));
        tvSeperator.setVisibility(View.GONE);
        tvSeperator.setText(flashcards.indexOf(Card));
        tvAnswerSection.setVisibility(View.GONE);
        tvAnswerSection.setText(flashcards.indexOf(Card));
        tvAnswer.setVisibility(View.GONE);
        tvAnswer.setText(flashcards.indexOf(Card));

        ivNextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Card+1 < flashcards.size()) {
                    Card--;
                    tvQuestion.setText(flashcards.indexOf(Card));
                    tvSeperator.setVisibility(View.GONE);
                    tvSeperator.setText(flashcards.indexOf(Card));
                    tvAnswerSection.setVisibility(View.GONE);
                    tvAnswerSection.setText(flashcards.indexOf(Card));
                    tvAnswer.setVisibility(View.GONE);
                    tvAnswer.setText(flashcards.indexOf(Card));
                }
            }
        });
        ivPreviousCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Card-1 >= 0) {
                    Card--;
                    tvQuestion.setText(flashcards.indexOf(Card));
                    tvSeperator.setVisibility(View.GONE);
                    tvSeperator.setText(flashcards.indexOf(Card));
                    tvAnswerSection.setVisibility(View.GONE);
                    tvAnswerSection.setText(flashcards.indexOf(Card));
                    tvAnswer.setVisibility(View.GONE);
                    tvAnswer.setText(flashcards.indexOf(Card));
                }
            }
        });
        btnShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSeperator.setVisibility(View.VISIBLE);
                tvAnswerSection.setVisibility(View.VISIBLE);
                tvAnswer.setVisibility(View.VISIBLE);
            }
        });

    }
}