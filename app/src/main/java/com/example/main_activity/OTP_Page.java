package com.example.main_activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
            }
        };
        //implement textwatcher
        token.addTextChangedListener(textWatcher);

        //assign authenticate xml button to auth variable
        Button auth = findViewById(R.id.authenticate);
        //set the button color to diff color when havent generate otp
        if (randomNumber == null) {
            auth.setBackgroundColor(Color.parseColor("#FFF44336")); // Set color using a hex string
        }
        Button otp = findViewById(R.id.otpnew);
        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //after otp has been generated
                //color of authenticate button will change
                auth.setBackgroundColor(Color.parseColor("#A966CC")); // Set color using a hex string
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
                    //if otp not generated yet display toast
                    Toast.makeText(OTP_Page.this,"Generate OTP first!",Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        // Attempt to parse the string as an integer
                        int otpnum = Integer.parseInt(tokentyped);

                        // If parsing succeeds, it's a valid integer
                        if(otpnum == randomNumber){
                            Intent MainActivity = new Intent(OTP_Page.this,MainActivity.class);
                            startActivity(MainActivity);
                        }
                        else{
                            //display if input is not the same as the otp given
                            Toast.makeText(OTP_Page.this,"Incorrect OTP", Toast.LENGTH_SHORT).show();
                        }

                    } catch (NumberFormatException e) {
                        // If parsing fails, it's not a valid integer
                        Toast.makeText(OTP_Page.this,"Incorrect OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //back button go back to login page
        ImageView backbu = findViewById(R.id.backb1);
        backbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten111 = new Intent(OTP_Page.this, Login_Page.class);
                startActivity(inten111);
            }
        });
    }
}