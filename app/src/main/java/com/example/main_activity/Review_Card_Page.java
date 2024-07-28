package com.example.main_activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class Review_Card_Page extends AppCompatActivity {
    private static final String TAG = "Review_Card_Page";
    int Card = 0;
    private ArrayList<Flashcard> flashcards;
    private TextView tvQuestion, tvSeperator, tvAnswerSection, tvAnswer, tvShowAnswer;
    private CenteredImageView ivDrawing;
    private ImageView ivNextCard, ivPreviousCard, ivBack;




    private static final String CARD_DELIMITER = ";;;";
    private static final String FIELD_DELIMITER = ":::";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_card_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent receivingEnd = getIntent();
        String cardStrings = receivingEnd.getStringExtra("Deck");
        Log.d(TAG, "Received cardStrings: " + cardStrings);

        flashcards = new ArrayList<>();
        if (cardStrings != null && !cardStrings.isEmpty()) {
            String[] splitStrings = cardStrings.split(CARD_DELIMITER);
            ArrayList<String> list = new ArrayList<>(Arrays.asList(splitStrings));
            for (String s : list) {
                try {
                    String[] cardvalues = s.split(FIELD_DELIMITER);
                    if (cardvalues.length < 4) {
                        Log.e(TAG, "Invalid card format: " + Arrays.toString(cardvalues));
                        continue;
                    }
                    String cardName = cardvalues[0];
                    String front = cardvalues[1];
                    String back = cardvalues[2];
                    Bitmap drawing = stringToBitmap(cardvalues[3]);
                    Log.d(TAG, "Decoded bitmap for card: " + cardName);
                    Flashcard card = new Flashcard(cardName, front, back, drawing);
                    flashcards.add(card);
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing card data: " + e.getMessage(), e);
                    showErrorAndExit("Error parsing card data: " + e.getMessage());
                    return;
                }
            }
        } else {
            showErrorAndExit("No card data received.");
            return;
        }

        tvQuestion = findViewById(R.id.tvQuestion);
        tvSeperator = findViewById(R.id.tvSeperator);
        tvAnswerSection = findViewById(R.id.tvAnswerSection);
        tvAnswer = findViewById(R.id.tvAnswer);
        ivDrawing = findViewById(R.id.ivDrawing);
        ivNextCard = findViewById(R.id.ivNextCard);
        ivPreviousCard = findViewById(R.id.ivPreviousCard);
        tvShowAnswer = findViewById(R.id.tvShowAnswer);
        ivBack = findViewById(R.id.ivReviewBack);
        ivDrawing = findViewById(R.id.ivDrawing);

        try {
            showCard(Card);
        } catch (Exception e) {
            Log.e(TAG, "Error displaying card: " + e.getMessage(), e);
            showErrorAndExit("Error displaying card: " + e.getMessage());
        }

        ivNextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Card + 1 < flashcards.size()) {
                    Card++;
                    try {
                        showCard(Card);
                    } catch (Exception e) {
                        Log.e(TAG, "Error displaying card: " + e.getMessage(), e);
                        showErrorAndExit("Error displaying card: " + e.getMessage());
                    }
                }
            }
        });

        ivPreviousCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Card - 1 >= 0) {
                    Card--;
                    try {
                        showCard(Card);
                    } catch (Exception e) {
                        Log.e(TAG, "Error displaying card: " + e.getMessage(), e);
                        showErrorAndExit("Error displaying card: " + e.getMessage());
                    }
                }
            }
        });

        tvShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSeperator.setVisibility(View.VISIBLE);
                tvAnswerSection.setVisibility(View.VISIBLE);
                tvAnswer.setVisibility(View.VISIBLE);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showCard(int index) {
        if (flashcards.size() > 0) {
            Flashcard flashcard = flashcards.get(index);
            Log.d(TAG, "Showing card: " + flashcard.front);
            tvQuestion.setText(flashcard.front);
            tvAnswer.setText(flashcard.back);

            if (flashcard.drawing != null) {
                ivDrawing.setImageBitmapCentered(flashcard.drawing);
                Log.d(TAG, "Displayed drawing for card: " + flashcard.cardName);
            } else {
                ivDrawing.setImageBitmap(null);
                Log.e(TAG, "No drawing to display for card: " + flashcard.cardName);
            }

            tvSeperator.setVisibility(View.GONE);
            tvAnswerSection.setVisibility(View.GONE);
            tvAnswer.setVisibility(View.GONE);
        } else {
            Log.e(TAG, "No flashcards to display");
        }
    }


    private void showErrorAndExit(String errorMessage) {
        Toast.makeText(Review_Card_Page.this, errorMessage, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Review_Card_Page.this, MainActivity.class); // Assuming MainActivity is the main page
        startActivity(intent);
        finish();
    }

    private Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            Log.e(TAG, "Error decoding bitmap: " + e.getMessage());
            return null;
        }
    }
}
