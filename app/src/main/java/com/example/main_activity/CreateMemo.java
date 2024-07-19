package com.example.main_activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

public class CreateMemo extends AppCompatActivity {

    TextView addImage;
    ImageView imageAdded;
    String StringURI;
    EditText MemoText;
    Boolean isTextMemo = true;
    ActivityResultLauncher<Intent> resultLauncher;
    Boolean isImageAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_memo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // get all elements from the xml file
        TextInputLayout memoImputLayout = findViewById(R.id.memoImputLayout);
        MemoText = findViewById(R.id.etMemo);
        TextInputLayout MemoOptionImputLayout = findViewById(R.id.MemoOptionImputLayout);
        MaterialAutoCompleteTextView MemoInput = findViewById(R.id.MemoInput);
        addImage = findViewById(R.id.tvAddImage);
        TextView createMemo = findViewById(R.id.tvCreateMemo);
        imageAdded = findViewById(R.id.ivAddedImage);
        // set up text watcher
        textwatcher(memoImputLayout,MemoText);
        textwatcher(MemoOptionImputLayout,MemoInput);
        // option list
        MemoInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    memoImputLayout.setVisibility(View.VISIBLE);
                    MemoText.setVisibility(View.VISIBLE);
                    imageAdded.setVisibility(View.GONE);
                    addImage.setVisibility(View.GONE);
                    isTextMemo = true;
                } else if (position == 1) {
                    memoImputLayout.setVisibility(View.GONE);
                    MemoText.setVisibility(View.GONE);
                    imageAdded.setVisibility(View.VISIBLE);
                    addImage.setVisibility(View.VISIBLE);
                    isTextMemo = false;
                }
            }
        });


        // set the view for the page when you first enter the activity
        memoImputLayout.setVisibility(View.GONE);
        MemoText.setVisibility(View.GONE);
        imageAdded.setVisibility(View.GONE);
        addImage.setVisibility(View.GONE);
        registerResult();

        // button to add the image using image picker from the phone's gallery
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        // button to create memo
        createMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTextMemo && isImageAdded) {
                    // if memo is not text memo and image is added
                    Memo memo = new Memo(StringURI,"1");
                    // create database handler to add the image memo
                    MemoDatabaseHandler dbHandler = new MemoDatabaseHandler(CreateMemo.this, null, null, 1);
                    dbHandler.addMemo(memo);
                    finish();
                }else if (isTextMemo && MemoText.getText().toString().matches("")) {
                    // if memo is text memo and is empty
                    if (MemoText.getText().toString().matches("")) {
                        shakeEditText(MemoText);
                        memoImputLayout.setHelperText("Required*");
                    }
                    if (MemoInput.getText().toString().matches("")) {
                        shakeEditText(MemoInput);
                        MemoOptionImputLayout.setHelperText("Required*");
                    }
                    Toast.makeText(v.getContext(), "All fields need to be filled", Toast.LENGTH_SHORT).show();
                }else if (isTextMemo) {
                    // if memo is text memo and not empty
                    Memo memo = new Memo(MemoText.getText().toString(),"0");
                    // create database handler to add the text memo
                    MemoDatabaseHandler dbHandler = new MemoDatabaseHandler(CreateMemo.this, null, null, 1);
                    dbHandler.addMemo(memo);
                    finish();
                }else {
                    if (MemoText.getText().toString().matches("")) {
                        shakeEditText(MemoText);
                        memoImputLayout.setHelperText("Required*");
                    }
                    if (MemoInput.getText().toString().matches("")) {
                        shakeEditText(MemoInput);
                        MemoOptionImputLayout.setHelperText("Required*");
                    }
                    Toast.makeText(v.getContext(), "All fields need to be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // function to pick the image from the image gallery
    private void pickImage(){
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }
    // register result to call an intent to get a result from it
    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try{
                            // if have result, get image data as an uri
                            Uri imageUri = result.getData().getData();
                            // make the uri persistent and will last
                            getContentResolver().takePersistableUriPermission(imageUri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            // convert uri to string and set the image for the image preview
                            StringURI = imageUri.toString();
                            imageAdded.setImageURI(imageUri);
                            isImageAdded = true;
                        }catch (Exception e){
                            // if no result
                            isImageAdded = false;
                            Toast.makeText(CreateMemo.this,"No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
    private void shakeEditText(EditText editText) {
        Animation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        editText.startAnimation(shake);
    }
    private void textwatcher(TextInputLayout textInputLayout, EditText editText) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    textInputLayout.setHelperText("");
                } else {
                    textInputLayout.setHelperText("Required*");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }

        };
        editText.addTextChangedListener(textWatcher);
    }
}