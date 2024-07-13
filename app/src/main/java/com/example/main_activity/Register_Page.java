package com.example.main_activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

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


        //assign xml textinputlayout fields to variables
        TextInputLayout uInputLayout = findViewById(R.id.usernameImputLayout);
        TextInputLayout pInputLayout = findViewById(R.id.passwordImputLayout);
        TextInputLayout eInputLayout = findViewById(R.id.emailImputLayout);

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
                if (s.length() > 0) {
                    uInputLayout.setHelperText(null);
                } else {
                    uInputLayout.setHelperText("Required*");
                }
            }
        };
        TextWatcher textWatcher1 = new TextWatcher() {
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
                int lines = password.getLineCount();
                if (lines > pmaxLines) {
                    // Remove the last character to prevent further input
                    password.getText().delete(password.getSelectionStart() - 1, password.getSelectionStart());
                }
                if (s.length() > 0) {
                    pInputLayout.setHelperText(null);
                } else {
                    pInputLayout.setHelperText("Required*");
                }
            }
        };
        TextWatcher textWatcher2 = new TextWatcher() {
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
                int lines = emaila.getLineCount();
                if (lines > emaxLines) {
                    // Remove the last character to prevent further input
                    emaila.getText().delete(emaila.getSelectionStart() - 1, emaila.getSelectionStart());
                }
                if (s.length() > 0) {
                    eInputLayout.setHelperText(null);
                } else {
                    eInputLayout.setHelperText("Required*");
                }
            }
        };

        //create a new dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //implement the textwatcher
        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher1);
        emaila.addTextChangedListener(textWatcher2);

        //assign xml button createbtn to btnreg variable
        TextView btnreg = findViewById(R.id.createbtn);
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
                    shakeEditText(username);
                    uInputLayout.setHelperText("Required*");
                    shakeEditText(emaila);
                    eInputLayout.setHelperText("Required*");
                    shakeEditText(password);
                    pInputLayout.setHelperText("Required*");
                }
                else {
                    //check if username and password is empty
                    if ((userstring == null || userstring.isEmpty()) && (pwstring == null || pwstring.isEmpty())) {
                        shakeEditText(username);
                        uInputLayout.setHelperText("Required*");
                        shakeEditText(password);
                        pInputLayout.setHelperText("Required*");
                    }
                    //check if password and email is empty
                    else if ((pwstring == null || pwstring.isEmpty()) && (emailstring == null || emailstring.isEmpty())) {
                        shakeEditText(emaila);
                        eInputLayout.setHelperText("Required*");
                        shakeEditText(password);
                        pInputLayout.setHelperText("Required*");
                    }
                    //check if username and email is empty
                    else if ((userstring == null || userstring.isEmpty()) && (emailstring == null || emailstring.isEmpty())) {
                        shakeEditText(username);
                        uInputLayout.setHelperText("Required*");
                        shakeEditText(emaila);
                        eInputLayout.setHelperText("Required*");
                    }
                    //if no 2 fields are empty move on to individual fields
                    else {
                        //check if username is empty
                        if(userstring == null || userstring.isEmpty()){
                            shakeEditText(username);
                            uInputLayout.setHelperText("Required*");
                        }
                        //check if password is empty
                        else if(pwstring == null || pwstring.isEmpty()){
                            shakeEditText(password);
                            pInputLayout.setHelperText("Required*");
                        }
                        //check if email is empty
                        else if(emailstring == null || emailstring.isEmpty()){
                            shakeEditText(emaila);
                            eInputLayout.setHelperText("Required*");
                        }
                        else {
                            DatabaseHandler db = new DatabaseHandler(Register_Page.this);
                            User theusertaken = db.checkuser(userstring);

                            // email can be reusable it does not have to be unique
                            //username to be used as primary key
                            if(theusertaken != null)
                            {
                                //create toast if username in database
                                shakeEditText(username);
                                uInputLayout.setHelperText("Username is taken");
                            }
                            //verifies if email address is in correct format
                            //if it is not correct format it will enter if statement
                            else if(!Patterns.EMAIL_ADDRESS.matcher(emailstring).matches()){
                                shakeEditText(emaila);
                                eInputLayout.setHelperText("Invalid email address");
                            }
                            else {
                                //create account if username available
                                User usertoadd = new User(userstring,pwstring,emailstring);
                                db.addUser(usertoadd);

                                builder.setMessage("Account created");

                                //when ok button clicked it will bring back to the login page
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });

                                //go back to login page even if click outside of dialog box
                                builder.setCancelable(true);
                                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        finish();
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
                finish();
            }
        });
    }
    private void shakeEditText(EditText editText) {
        Animation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        editText.startAnimation(shake);
    }
}