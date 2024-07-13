package com.example.main_activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

public class OTP_Page extends AppCompatActivity {

    //define variable randomNumber
    private Integer randomNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // background animation
        ConstraintLayout constraintLayout = findViewById(R.id.main);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        // recieve intent to get username
        Intent receivingEnd = getIntent();
        String Name = receivingEnd.getStringExtra("Username");

        TextInputLayout tokenInputLayout = findViewById(R.id.TokenImputLayout);

        //ensures only 1 line of string
        EditText token = findViewById(R.id.tokeninput);
        final int maxLines = token.getMaxLines();
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //get current lines in edittext field
                int lines = token.getLineCount();
                if (lines > maxLines) {
                    // Remove the last character to prevent further input
                    token.getText().delete(token.getSelectionStart() - 1, token.getSelectionStart());
                }
                if (s.length() > 0) {
                    tokenInputLayout.setHelperText(null);
                } else {
                    tokenInputLayout.setHelperText("Required*");
                }
            }
        };
        //implement textwatcher
        token.addTextChangedListener(textWatcher);

        //assign authenticate xml button to auth variable
        TextView auth = findViewById(R.id.authenticate);
        TextView otp = findViewById(R.id.otpnew);
        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                //picks random number between 0 and 90000, excluding 90000
                //by adding 10000 the number will always be 5 digits
                randomNumber = random.nextInt(90000) + 10000;

                Toast.makeText(OTP_Page.this, "OTP: " + randomNumber, Toast.LENGTH_LONG).show();
            }
        });

        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the string typed into edittext field
                EditText tokenn = findViewById(R.id.tokeninput);
                String tokentyped = tokenn.getText().toString();

                if(randomNumber == null){
                    //animate vibration and show error
                    shakeEditText(token);
                    tokenInputLayout.setHelperText("Required*");
                }
                else{
                    try {
                        // Attempt to parse the string as an integer
                        int otpnum = Integer.parseInt(tokentyped);

                        // If parsing succeeds, it's a valid integer
                        if(otpnum == randomNumber){
                            Intent MainActivity = new Intent(OTP_Page.this,MainActivity.class);
                            MainActivity.putExtra("Username",Name);
                            startActivity(MainActivity);
                            finish();
                        }
                        else{
                            //animate vibration and show error
                            shakeEditText(token);
                            tokenInputLayout.setHelperText("Incorrect OTP*");
                        }

                    } catch (NumberFormatException e) {
                        //animate vibration and show error
                        shakeEditText(token);
                        tokenInputLayout.setHelperText("Required*");
                    }
                }
            }
        });

        //back button go back to login page
        ImageView backbu = findViewById(R.id.backb1);
        backbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    // Method to animate EditText when OTP field is empty
    private void shakeEditText(EditText editText) {
        Animation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        editText.startAnimation(shake);
    }
}