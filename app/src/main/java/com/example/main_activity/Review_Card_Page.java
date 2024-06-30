package com.example.main_activity;

import static androidx.core.content.IntentCompat.getSerializableExtra;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class Review_Card_Page extends AppCompatActivity {
    // position in the deck
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
        // receive the deck as a string from the manage deck page
        Intent receivingEnd = getIntent();
        String cardStrings = receivingEnd.getStringExtra("Deck");

        // conver the string into a list of strings
        ArrayList<Flashcard> flashcards = new ArrayList<Flashcard>();
        String[] splitStrings = cardStrings.split("/");
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(splitStrings));
        for (int i = 0;i<list.size();i++){
            // for each string in the list of string, convert it to a flashcard
            String[] cardvalues = list.get(i).split(",");
            ArrayList<String> listCardValues = new ArrayList<String>(Arrays.asList(cardvalues));
            Flashcard card = new Flashcard(listCardValues.get(0),listCardValues.get(1),listCardValues.get(2),Integer.parseInt(listCardValues.get(3)));
            flashcards.add(card);
        }

        // get all the fields from the XML
        TextView tvQuestion = findViewById(R.id.tvQuestion);
        TextView tvSeperator = findViewById(R.id.tvSeperator);
        TextView tvAnswerSection = findViewById(R.id.tvAnswerSection);
        TextView tvAnswer = findViewById(R.id.tvAnswer);
        ImageView ivNextCard = findViewById(R.id.ivNextCard);
        ImageView ivPreviousCard = findViewById(R.id.ivPreviousCard);
        TextView tvShowAnswer = findViewById(R.id.tvShowAnswer);
        ImageView ivBack = findViewById(R.id.ivReviewBack);


        // set the fields and hide some of the fields
        tvQuestion.setText(flashcards.get(Card).front);
        tvSeperator.setVisibility(View.GONE);
        tvAnswerSection.setVisibility(View.GONE);
        tvAnswer.setVisibility(View.GONE);
        tvAnswer.setText(flashcards.get(Card).back);

        // on click for next card
        ivNextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for next card
                if (Card+1 < flashcards.size()) {
                    // increment the position
                    Card++;
                    // set all the filed and hide the required fields
                    tvQuestion.setText(flashcards.get(Card).front);
                    tvSeperator.setVisibility(View.GONE);
                    tvAnswerSection.setVisibility(View.GONE);
                    tvAnswer.setVisibility(View.GONE);
                    tvAnswer.setText(flashcards.get(Card).back);
                }
            }
        });
        ivPreviousCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for previous card
                if (Card-1 >= 0) {
                    // decrement the position
                    Card--;
                    // set all the filed and hide the required fields
                    tvQuestion.setText(flashcards.get(Card).front);
                    tvSeperator.setVisibility(View.GONE);
                    tvAnswerSection.setVisibility(View.GONE);
                    tvAnswer.setVisibility(View.GONE);
                    tvAnswer.setText(flashcards.get(Card).back);
                }
            }
        });
        // show the answer button
        tvShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show the required fields
                tvSeperator.setVisibility(View.VISIBLE);
                tvAnswerSection.setVisibility(View.VISIBLE);
                tvAnswer.setVisibility(View.VISIBLE);
            }
        });
        // back button
        ivBack.setOnClickListener(new View.OnClickListener() {
            //finish the activity to go back to the manage deck page
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}