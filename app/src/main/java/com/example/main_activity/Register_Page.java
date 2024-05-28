package com.example.main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Register_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText username = findViewById(R.id.userinputreg);
        EditText password = findViewById(R.id.passinputreg);
        EditText emaila = findViewById(R.id.emailinputreg);
        final int maxLines = username.getMaxLines();
        final int pmaxLines = password.getMaxLines();
        final int emaxLines = emaila.getMaxLines();
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
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

                int Thelinez = emaila.getLineCount();
                if (Thelinez > emaxLines) {
                    // Remove the last character to prevent further input
                    emaila.getText().delete(emaila.getSelectionStart() - 1, emaila.getSelectionStart());
                }
            }
        };

        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        emaila.addTextChangedListener(textWatcher);

        Button btnreg = findViewById(R.id.regbtn);
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userstring = username.getText().toString();
                String pwstring = password.getText().toString();
                String emailstring = emaila.getText().toString();

                //checking all test cases
                if((userstring == null || userstring.isEmpty()) && (pwstring == null || pwstring.isEmpty()) && (emailstring == null || emailstring.isEmpty())){
                    Toast.makeText(Register_Page.this, "All fields are empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    if ((userstring == null || userstring.isEmpty()) && (pwstring == null || pwstring.isEmpty())) {
                        Toast.makeText(Register_Page.this, "Username and password is empty", Toast.LENGTH_SHORT).show();
                    }
                    else if ((pwstring == null || pwstring.isEmpty()) && (emailstring == null || emailstring.isEmpty())) {
                        Toast.makeText(Register_Page.this, "Password and email is empty", Toast.LENGTH_SHORT).show();
                    }
                    else if ((userstring == null || userstring.isEmpty()) && (emailstring == null || emailstring.isEmpty())) {
                        Toast.makeText(Register_Page.this, "Username and email is empty", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(userstring == null || userstring.isEmpty()){
                            Toast.makeText(Register_Page.this, "Username is empty", Toast.LENGTH_SHORT).show();
                        }
                        else if(pwstring == null || pwstring.isEmpty()){
                            Toast.makeText(Register_Page.this, "Password is empty", Toast.LENGTH_SHORT).show();
                        }
                        else if(emailstring == null || emailstring.isEmpty()){
                            Toast.makeText(Register_Page.this, "Email is empty", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //verifies if email address is in correct format
                            //if it is not correct format it will enter if statement
                            if(!Patterns.EMAIL_ADDRESS.matcher(emailstring).matches()){
                                Toast.makeText(Register_Page.this, "Email is not valid", Toast.LENGTH_SHORT).show();
                            }
                            else if()
                            {
                                //code that takes the list of emails currently in the database

                                if() {
                                    //code that checks if email address does not exist in database
                                    //email address to be used as identifier key


                                }
                                else{
                                    Toast.makeText(Register_Page.this, "Email is taken", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if()
                            {
                                // code that takes the list of usernames currently in the database

                                if() {
                                    //username will be unique
                                    //check if username is taken


                                }
                                else{
                                    Toast.makeText(Register_Page.this, "Username is taken", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                // Define an Intent to navigate to the NextActivity
                                Intent intent = new Intent(Register_Page.this, Login_Page.class);

                                // Start the NextActivity
                                startActivity(intent);
                            }
                        }

                    }
                }
            }
        });
    }
}