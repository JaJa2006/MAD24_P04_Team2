package com.example.main_activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
                    Toast.makeText(ForgotPass_Page.this,"Username field is empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    //check if username is in database
                    DatabaseHandler db = new DatabaseHandler(ForgotPass_Page.this);
                    User useruseruser = db.checkuser(userstringz);

                    if(useruseruser == null){
                        //if username not in database display toast
                        Toast.makeText(ForgotPass_Page.this,"Username does not exist", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        builder.setMessage("Password is "+useruseruser.getPassword());

                        //when ok button clicked it will bring back to the login page
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Define an Intent to navigate to the NextActivity
                                Intent intent = new Intent(ForgotPass_Page.this, Login_Page.class);

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
                                Intent intent = new Intent(ForgotPass_Page.this, Login_Page.class);

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