package com.example.main_activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
public class Create_Card_Page extends AppCompatActivity {
    ArrayList<Flashcard> flashcards = new ArrayList<Flashcard>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_card_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // recieve intent from create deck page to get deck name
        Intent receivingEnd = getIntent();
        String Name = receivingEnd.getStringExtra("DeckName");
        // get all the edit text and button from XML
        EditText EtCardName = findViewById(R.id.etCardName);
        EditText EtCardFront = findViewById(R.id.etCardFront);
        EditText EtCardBack = findViewById(R.id.etCardBack);
        TextInputLayout CardnameImputLayout = findViewById(R.id.CardnameImputLayout);
        TextInputLayout CardFrontLayout = findViewById(R.id.CardFrontLayout);
        TextInputLayout CardBackImputLayout = findViewById(R.id.CardBackImputLayout);
        TextView tvCreateCard = findViewById(R.id.tvCreateCard);
        TextView tvFinish = findViewById(R.id.tvFinish);

        tvFinish.setBackgroundResource(R.drawable.button_outline_unactive);
        tvFinish.setTextColor(Color.parseColor("#FF939495"));
        // set up text watcher
        textwatcher(CardnameImputLayout,EtCardName);
        textwatcher(CardFrontLayout,EtCardFront);
        textwatcher(CardBackImputLayout,EtCardBack);
        // create card onclick listener
        tvCreateCard.setOnClickListener(new View.OnClickListener() {
            // on click make sure all field are filled and add card if filed
            @Override
            public void onClick(View v) {
                //combine all string to check if have delimiter
                String allStrirng = EtCardName.getText().toString() + EtCardFront.getText().toString() + EtCardBack.getText().toString();
                if (EtCardName.getText().toString().matches("") || EtCardFront.getText().toString().matches("") ||EtCardBack.getText().toString().matches("")){
                    // if fields are empty
                    if (EtCardName.getText().toString().matches("")) {
                        shakeEditText(EtCardName);
                        CardnameImputLayout.setHelperText("Required*");
                    }
                    if (EtCardFront.getText().toString().matches("")) {
                        shakeEditText(EtCardFront);
                        CardFrontLayout.setHelperText("Required*");
                    }
                    if (EtCardBack.getText().toString().matches("")) {
                        shakeEditText(EtCardBack);
                        CardBackImputLayout.setHelperText("Required*");
                    }
                    Toast.makeText(Create_Card_Page.this, "All fields need to be filled", Toast.LENGTH_SHORT).show();
                }else if (allStrirng.contains(",") || allStrirng.contains("/")) {
                    // Make sure users are not using the delimiter used
                    Toast.makeText(Create_Card_Page.this, "Fields cannot have , or /", Toast.LENGTH_SHORT).show();
                }else {
                    // Create flashcard
                    Flashcard flashcard = new Flashcard(EtCardName.getText().toString(),EtCardFront.getText().toString(),EtCardBack.getText().toString());
                    flashcards.add(flashcard);
                    // clear the edit text field
                    EtCardName.getText().clear();
                    EtCardFront.getText().clear();
                    EtCardBack.getText().clear();
                    if (flashcards.size()==2) {
                        tvFinish.setBackgroundResource(R.drawable.button_outline);
                        tvFinish.setTextColor(Color.parseColor("#025B81"));
                    }
                    Toast.makeText(Create_Card_Page.this, "Card Created", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // finish creating card button
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make sure there are at least 2 cards
                if (flashcards.size() >= 2) {
                    // add deck to the SQLite if got 2 or more cards
                    Deck deck = new Deck(Name,flashcards);
                    DeckDatabaseHandler dbHandler = new DeckDatabaseHandler(Create_Card_Page.this, null, null, 1);
                    dbHandler.addDeck(deck);
                    finish();
                } else {
                    Toast.makeText(Create_Card_Page.this, "You must have at least 2 cards", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void shakeEditText(EditText editText) {
        Animation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        editText.startAnimation(shake);
    }
    private void textwatcher(TextInputLayout textInputLayout, EditText editText) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    textInputLayout.setHelperText("");
                } else {
                    textInputLayout.setHelperText("Required*");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }

        };
        editText.addTextChangedListener(textWatcher);
    }
}