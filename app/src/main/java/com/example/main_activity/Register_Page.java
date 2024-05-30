package com.example.main_activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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


        //assign xml edittext fields to variables
        EditText username = findViewById(R.id.userinputreg);
        EditText password = findViewById(R.id.passinputreg);
        EditText emaila = findViewById(R.id.emailinputreg);

        //get max lines of each edittext field which is set to 1
        final int maxLines = username.getMaxLines();
        final int pmaxLines = password.getMaxLines();
        final int emaxLines = emaila.getMaxLines();
        TextWatcher textWatcher = new TextWatcher() {
            //necessary to put beforetextchanged and ontextchanged else
            //it wont work
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            //aftertextchanged is active every time the edittext field is editted
            @Override
            public void afterTextChanged(Editable s) {
                //get current number of lines
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

        //create a new dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //implement the textwatcher
        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        emaila.addTextChangedListener(textWatcher);

        //assign xml button createbtn to btnreg variable
        Button btnreg = findViewById(R.id.createbtn);
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get the text that is currently in the edittext fields
                String userstring = username.getText().toString();
                String pwstring = password.getText().toString();
                String emailstring = emaila.getText().toString();

                //checking all test cases
                //check if edittext fields are empty
                if((userstring == null || userstring.isEmpty()) && (pwstring == null || pwstring.isEmpty()) && (emailstring == null || emailstring.isEmpty())){
                    Toast.makeText(Register_Page.this, "All fields are empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    //check if username and password is empty
                    if ((userstring == null || userstring.isEmpty()) && (pwstring == null || pwstring.isEmpty())) {
                        Toast.makeText(Register_Page.this, "Username and password is empty", Toast.LENGTH_SHORT).show();
                    }
                    //check if password and email is empty
                    else if ((pwstring == null || pwstring.isEmpty()) && (emailstring == null || emailstring.isEmpty())) {
                        Toast.makeText(Register_Page.this, "Password and email is empty", Toast.LENGTH_SHORT).show();
                    }
                    //check if username and email is empty
                    else if ((userstring == null || userstring.isEmpty()) && (emailstring == null || emailstring.isEmpty())) {
                        Toast.makeText(Register_Page.this, "Username and email is empty", Toast.LENGTH_SHORT).show();
                    }
                    //if no 2 fields are empty move on to individual fields
                    else {
                        //check if username is empty
                        if(userstring == null || userstring.isEmpty()){
                            Toast.makeText(Register_Page.this, "Username is empty", Toast.LENGTH_SHORT).show();
                        }
                        //check if password is empty
                        else if(pwstring == null || pwstring.isEmpty()){
                            Toast.makeText(Register_Page.this, "Password is empty", Toast.LENGTH_SHORT).show();
                        }
                        //check if email is empty
                        else if(emailstring == null || emailstring.isEmpty()){
                            Toast.makeText(Register_Page.this, "Email is empty", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            DatabaseReference usercheck = FirebaseDatabase.getInstance().getReference().child("Users");

                            //gets a list of the usernames currently in the database
                            ArrayList<String> usernamelist = new ArrayList<>();
                            usercheck.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    // Iterate through each child node under the "Users" node
                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                                        // Get the username from the snapshot
                                        String userName = userSnapshot.child("username").getValue(String.class);

                                        // Add the username to the list
                                        usernamelist.add(userName);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    //create toast if the read operation is cancelled or fails
                                    Toast.makeText(Register_Page.this, "Failed to fetch data. Please try again later.", Toast.LENGTH_SHORT).show();
                                }
                            });


                            //verifies if email address is in correct format
                            //if it is not correct format it will enter if statement
                            if(!Patterns.EMAIL_ADDRESS.matcher(emailstring).matches()){
                                Toast.makeText(Register_Page.this, "Email is not valid", Toast.LENGTH_SHORT).show();
                            }
                            // email can be reusable it does not have to be unique
                            //username to be used as primary key
                            else if(usernamelist.contains(userstring))
                            {
                                //create toast if username in database
                                Toast.makeText(Register_Page.this, "Username is taken", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //create account if username available
                                //get a firebase reference
                                DatabaseReference fireref = FirebaseDatabase.getInstance().getReference().child("Users");
                                User newaccount = new User(userstring,pwstring,emailstring);

                                // Convert the User object to a Map
                                //to put inside the database
                                Map<String, Object> userData = new HashMap<>();

                                //setting each part to the user node
                                userData.put("username", newaccount.getName());
                                userData.put("email", newaccount.getEmail());
                                userData.put("password", newaccount.getPassword());

                                //push the changes to the database
                                fireref.push().setValue(userData);

                                builder.setMessage("Account created");

                                //when ok button clicked it will bring back to the login page
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Define an Intent to navigate to the NextActivity
                                        Intent intent = new Intent(Register_Page.this, Login_Page.class);

                                        // Start the NextActivity
                                        startActivity(intent);
                                    }
                                });

                                //go back to login page even if click outside of dialog box
                                builder.setCancelable(true);
                                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        // Define an Intent to navigate to the NextActivity
                                        Intent intent = new Intent(Register_Page.this, Login_Page.class);

                                        // Start the NextActivity
                                        startActivity(intent);
                                    }
                                });

                                //implements the dialog
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    }
                }
            }
        });

        ImageView backbutton = findViewById(R.id.backb);
        backbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intentback = new Intent(Register_Page.this,Login_Page.class);
                startActivity(intentback);
            }
        });
    }
}