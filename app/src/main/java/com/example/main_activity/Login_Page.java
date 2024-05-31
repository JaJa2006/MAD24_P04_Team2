package com.example.main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Login_Page extends AppCompatActivity {

    //create variables to use to store info about database user objects
    private String usernamefb;
    private String emailfb;
    private String passwordfb;
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

        //assign the edittext fields to variables
        EditText username = findViewById(R.id.userinput);
        EditText password = findViewById(R.id.passinput);

        //get the max lines that was specified in the xml code
        //max lines is set at 1 line
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

                //this checks after text is changed
                //the number of lines in edittext field
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

        //assigns xml button to loginbutton variable
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
                        DatabaseReference usercheckz = FirebaseDatabase.getInstance().getReference().child("Users");

                        //gets a list of the usernames currently in the database
                        ArrayList<String> usernamelistz = new ArrayList<>();
                        usercheckz.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                // Iterate through each child node under the "Users" node
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                                    // Get the username from the snapshot
                                    String userName = userSnapshot.child("username").getValue(String.class);

                                    // Add the username to the list
                                    usernamelistz.add(userName);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                //create toast if the read operation is cancelled or fails
                                Toast.makeText(Login_Page.this, "Failed to fetch data. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        if(usernamelistz.contains(userstring))
                        {
                            //check if username in editText field exists in database
                            //move on to next check if it exists

                            DatabaseReference checkpass = FirebaseDatabase.getInstance().getReference().child("Users");

                            // Create a query to search for the user with the specified username
                            Query querytofind = checkpass.orderByChild("username").equalTo(userstring);

                            querytofind.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Iterate through the results to get matching username
                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                        // Get the user data
                                        usernamefb = userSnapshot.child("username").getValue(String.class);
                                        emailfb = userSnapshot.child("email").getValue(String.class);
                                        passwordfb = userSnapshot.child("password").getValue(String.class);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    //create toast if the read operation is cancelled or fails
                                    Toast.makeText(Login_Page.this, "Failed to fetch data. Please try again later.", Toast.LENGTH_SHORT).show();
                                }
                            });


                            if(passwordfb.equals(pwstring)){
                                //check if password is correct
                                //successfully login if correct

                                // Define an Intent to navigate to the NextActivity
                                Intent intent = new Intent(Login_Page.this, OTP_Page.class);

                                // Start the NextActivity
                                startActivity(intent);

                            }
                            else{
                                //create toast if password is incorrect
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

        //assign regbtn xml button to regist variable
        Button regist = findViewById(R.id.regbtn);

        //create onclicklistener for regist button
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set intent to go to register page once clicked
                //go to page to register account
                Intent intenn = new Intent(Login_Page.this,Register_Page.class);

                startActivity(intenn);
            }
        });

        //assign forgotpass xml button to forgot variable
        TextView forgot = findViewById(R.id.forgotpass);
        forgot.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //set intent to go to forgot pass page
                //go to page to email pass
                Intent intenn22 = new Intent(Login_Page.this, ForgotPass_Page.class);
                startActivity(intenn22);
            }
        });
    }
}