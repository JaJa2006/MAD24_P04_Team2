package com.example.main_activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
        // background animation
        ConstraintLayout constraintLayout = findViewById(R.id.main);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();


        //assign the edittext fields to variables
        EditText username = findViewById(R.id.usernameImputEdit);
        EditText password = findViewById(R.id.passwordImputEdit);

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
        TextView loginbutton = findViewById(R.id.loginb);
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
                        //check if database contains the username
                        DatabaseHandler db = new DatabaseHandler(Login_Page.this);
                        User thedatabase = db.checkuser(userstring);
                        if(thedatabase != null)
                        {
                            if(thedatabase.getPassword().equals(pwstring)){
                                //check if password is correct
                                //successfully login if correct

                                // Define an Intent to navigate to the NextActivity
                                Intent intent = new Intent(Login_Page.this, OTP_Page.class);
                                intent.putExtra("Username",userstring);
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
        TextView regist = findViewById(R.id.regbtn);

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