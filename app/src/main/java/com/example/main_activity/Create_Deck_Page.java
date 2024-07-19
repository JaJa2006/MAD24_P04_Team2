package com.example.main_activity;

import android.content.Intent;
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

public class Create_Deck_Page extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_deck_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // get all button and edit text from XML
        TextView tvCreateDeck = findViewById(R.id.tvCreateDeck);
        EditText EtDeckName = findViewById(R.id.etDeckName);
        TextInputLayout DecknameImputLayout = findViewById(R.id.DecknameImputLayout);
        // set up text watcher
        textwatcher(DecknameImputLayout,EtDeckName);
        // on click for creating deck
        tvCreateDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for all fields to be filled
                if (EtDeckName.getText().toString().matches("")){
                    // if field are empty
                    if (EtDeckName.getText().toString().matches("")) {
                        shakeEditText(EtDeckName);
                        DecknameImputLayout.setHelperText("Required*");
                    }
                    Toast.makeText(Create_Deck_Page.this, "All fields need to be filled", Toast.LENGTH_SHORT).show();
                }else {
                    // create the deck and go to the create card page
                    Toast.makeText(Create_Deck_Page.this, "Deck created", Toast.LENGTH_SHORT).show();
                    Intent CreateCardPage = new Intent(Create_Deck_Page.this,Create_Card_Page.class);
                    CreateCardPage.putExtra("DeckName",EtDeckName.getText().toString());
                    startActivity(CreateCardPage);
                    finish();
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