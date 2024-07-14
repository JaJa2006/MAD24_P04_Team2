package com.example.main_activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ForgotPass_Page extends AppCompatActivity {

    private ExecutorService emailExecutor;
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
        // background animation
        ConstraintLayout constraintLayout = findViewById(R.id.main);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        //to send email
        emailExecutor = Executors.newSingleThreadExecutor();

        //assign the edittext fields to variables
        TextInputLayout tokenInputLayout = findViewById(R.id.usernameImputLayout);
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
                if (s.length() > 0) {
                    tokenInputLayout.setHelperText(null);
                } else {
                    tokenInputLayout.setHelperText("Required*");
                }
            }
        };

        //implements the textwatcher
        usernamez.addTextChangedListener(textWatcher);

        //assign retrieve xml button to ret variable
        TextView ret = findViewById(R.id.retrieve);

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checks the string in the username field
                String userstringz = usernamez.getText().toString();
                if(userstringz.isEmpty()){
                    //display toast if username is empty and shake the edittext field
                    shakeEditText(usernamez);
                    tokenInputLayout.setHelperText("Required*");
                }
                else{
                    //check if username is in database
                    DatabaseHandler db = new DatabaseHandler(ForgotPass_Page.this);
                    User useruseruser = db.checkuser(userstringz);

                    if(useruseruser == null){
                        shakeEditText(usernamez);
                        tokenInputLayout.setHelperText("Invalid username");
                    }
                    else{
                        sendEmailInBackground(useruseruser.getName(), useruseruser.getEmail(), useruseruser.getPassword());
                    }
                }
            }
        });

        ImageView backtolog = findViewById(R.id.backb2);
        backtolog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendEmailInBackground(String userName, String userEmail, String userPassword) {
        ProgressDialog progressDialog = new ProgressDialog(ForgotPass_Page.this);
        progressDialog.setMessage("Sending email...");
        progressDialog.setCancelable(false); // Prevent dismiss by tapping outside of the dialog
        progressDialog.show();
        emailExecutor.execute(() -> {
            try {
                Mailsender sender = new Mailsender("emailsender933@gmail.com","gicw gzfu ihkt mokg");
                sender.sendMail("Password retrieval for "+userName, "Your password is: "+userPassword, "emailsender933@gmail.com", userEmail);
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(ForgotPass_Page.this, "Email sent successfully", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(ForgotPass_Page.this, "Email not sent. Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (emailExecutor != null && !emailExecutor.isShutdown()) {
            emailExecutor.shutdown();
        }
    }
    private void shakeEditText(EditText editText) {
        Animation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        editText.startAnimation(shake);
    }
}