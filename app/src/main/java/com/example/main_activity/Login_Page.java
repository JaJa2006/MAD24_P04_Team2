package com.example.main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText username = findViewById(R.id.userinput);
        EditText password = findViewById(R.id.passinput);
        final int maxLines = username.getMaxLines();
        final int pmaxLines = password.getMaxLines();
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //checks whenever the text field is changed
                //ensures that the text never goes beyond 1 line
                //ensures no overflowing up or down
                int lines = username.getLineCount();
                if (lines > maxLines) {
                    // Remove the last character to prevent further input
                    username.getText().delete(username.getSelectionStart() - 1, username.getSelectionStart());
                }

                int linesOther = password.getLineCount();
                if (linesOther > pmaxLines) {
                    // Remove the last character to prevent further input
                    password.getText().delete(password.getSelectionStart() - 1, password.getSelectionStart());
                }
            }
        };

        //implements the textwatcher
        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);

        //assigns xml button to loginbutton
        Button loginbutton = findViewById(R.id.loginb);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checks the strings that are in the current text fields
                String userstring = username.getText().toString();
                String pwstring = password.getText().toString();

                //checks if both username and password fields are empty
                //if it is it will toss a toast that states it
                if((userstring == null || userstring.isEmpty()) && (pwstring == null || pwstring.isEmpty())){
                    Toast.makeText(Login_Page.this, "Username and Password is empty", Toast.LENGTH_SHORT).show();
                }
                else {

                    //checks if username is empty
                    //if it is it will toss a toast that states it
                    if (userstring == null || userstring.isEmpty()) {
                        Toast.makeText(Login_Page.this, "Username is empty", Toast.LENGTH_SHORT).show();
                    }

                    //checks if password is empty
                    else if (pwstring == null || pwstring.isEmpty()) {
                        Toast.makeText(Login_Page.this, "Password is empty", Toast.LENGTH_SHORT).show();
                    } else {
                        //code that takes the list of usernames currently in the database

                        if()
                        {
                            //check if username in editText field exists in database
                            //move on to next check if it exists

                            if(){
                                //check if password is correct
                                //successfully login if correct



                                // Define an Intent to navigate to the NextActivity
                                //only if the fields are all filled
                                Intent intent = new Intent(Login_Page.this, OTP_Page.class);

                                // Start the NextActivity
                                startActivity(intent);

                            }
                            else{
                                Toast.makeText(Login_Page.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(Login_Page.this, "Cannot find username", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });

        Button regist = findViewById(R.id.regbtn);
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set intent to go to register page
                Intent intenn = new Intent(Login_Page.this,Register_Page.class);

                startActivity(intenn);
            }
        });
    }
}