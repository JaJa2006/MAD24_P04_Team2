package com.example.main_activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ForgotPass_Page extends AppCompatActivity {

    private String firebaseemail;
    private String firebaseusername;
    private String firebasepassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_pass_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //assign the edittext fields to variables
        EditText usernamez = findViewById(R.id.userinput);

        //get the max lines that was specified in the xml code
        //max lines is set at 1 line
        final int maxLines12 = usernamez.getMaxLines();
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
                int lines = usernamez.getLineCount();
                if (lines > maxLines12) {
                    // Remove the last character to prevent further input
                    usernamez.getText().delete(usernamez.getSelectionStart() - 1, usernamez.getSelectionStart());
                }
            }
        };

        //implements the textwatcher
        usernamez.addTextChangedListener(textWatcher);

        //assign retrieve xml button to ret variable
        Button ret = findViewById(R.id.retrieve);


        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checks the string in the username field
                String userstringz = usernamez.getText().toString();
                if((userstringz.isEmpty()) || userstringz == null){
                    //display toast if username is empty
                    Toast.makeText(ForgotPass_Page.this,"Username field is empty", Toast.LENGTH_SHORT);
                }
                else{
                    //check if username is in database
                    DatabaseReference usercheckzz = FirebaseDatabase.getInstance().getReference().child("Users");

                    //gets a list of the usernames currently in the database
                    ArrayList<String> usernamelistzz = new ArrayList<>();
                    usercheckzz.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            // Iterate through each child node under the "Users" node
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                                // Get the username from the snapshot
                                String userName = userSnapshot.child("username").getValue(String.class);

                                // Add the username to the list
                                usernamelistzz.add(userName);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //create toast if the read operation is cancelled or fails
                            Toast.makeText(ForgotPass_Page.this, "Failed to fetch data. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    if(!usernamelistzz.contains(userstringz)){
                        //if username not in database display toast
                        Toast.makeText(ForgotPass_Page.this,"Username does not exist", Toast.LENGTH_SHORT);
                    }
                    else{
                        //if username is in database email his password to him
                        DatabaseReference checkpassz = FirebaseDatabase.getInstance().getReference().child("Users");

                        // Create a query to search for the user with the specified username
                        Query querytofindz = checkpassz.orderByChild("username").equalTo(userstringz);

                        querytofindz.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Iterate through the results to get matching username
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    // Get the user data
                                    firebaseusername = userSnapshot.child("username").getValue(String.class);
                                    firebaseemail = userSnapshot.child("email").getValue(String.class);
                                    firebasepassword = userSnapshot.child("password").getValue(String.class);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                //create toast if the read operation is cancelled or fails
                                Toast.makeText(ForgotPass_Page.this, "Failed to fetch data. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Intent intent4 = new Intent(Intent.ACTION_SENDTO);

                        //the email it is going to
                        intent4.setData(Uri.parse("mailto:"+firebaseemail)); // Specify the recipient email address

                        //set the subject of the email
                        intent4.putExtra(Intent.EXTRA_SUBJECT, "Forgotten password");

                        //set the text of the email, which is the password
                        intent4.putExtra(Intent.EXTRA_TEXT, "Your password is: "+firebasepassword);

                        // Verify if there's an app to handle the intent
                        if (intent4.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent4);
                        } else {
                            Toast.makeText(ForgotPass_Page.this, "No email app installed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        ImageView backtolog = findViewById(R.id.backb2);
        backtolog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentback = new Intent(ForgotPass_Page.this,Login_Page.class);
                startActivity(intentback);
            }
        });
    }
}