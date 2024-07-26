package com.example.main_activity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.main_activity.ml.MusicClassifierModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.UniversalAudioInputStream;
import be.tarsos.dsp.mfcc.MFCC;

public class Manage_Playlist extends AppCompatActivity {

    ActivityResultLauncher<Intent> resultLauncher;
    TextView addAudio;
    String StringURI;
    Uri audioUri;
    TextInputEditText SongName;
    int PlaylistID;
    String PlaylistName;
    TextView tvPlaylistName;
    SongListAdapter mAdapter;
    ArrayList<float[]> mfccList = new ArrayList<float[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_playlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // receive intent to get song name and song uri
        Intent receivingEnd = getIntent();
        PlaylistID = receivingEnd.getIntExtra("PlaylistID",-1);
        PlaylistName = receivingEnd.getStringExtra("PlaylistName");
        if (PlaylistID != -1) {
            // if this is the first time to enter this screen, save the data
            SharedPreferences preferences = getSharedPreferences("ManagePlaylist",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("PlaylistID", PlaylistID);
            editor.putString("PlaylistName", PlaylistName);
            editor.apply();
        } else {
            // if had to go to files to select the song, load the data
            SharedPreferences loadPreferences = getSharedPreferences("ManagePlaylist",MODE_PRIVATE);
            PlaylistID = loadPreferences.getInt("PlaylistID",-1);
            PlaylistName = loadPreferences.getString("PlaylistName","");
        }
        // get the memo from the data base
        MusicPlaylistDatabaseHandler dbHandler = new MusicPlaylistDatabaseHandler(Manage_Playlist.this, null, null, 1);
        MusicPlaylist playlist = dbHandler.getPlaylistFromID(PlaylistID);
        // get the recyclerview from the XML
        RecyclerView recyclerView = findViewById(R.id.rvManagePlaylist);
        // fill the layout with the information from the data base
        mAdapter = new SongListAdapter(playlist,this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        // get the elements from xml
        addAudio = findViewById(R.id.tvAddMusic);
        tvPlaylistName = findViewById(R.id.tvPlaylistName);
        tvPlaylistName.setText(PlaylistName);
        ImageView ivBack = findViewById(R.id.ivPlaylistback);
        // back button
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // call the register result function
        registerResult();

        // set on click listener for the add audio button
        addAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogLayout = LayoutInflater.from(Manage_Playlist.this).inflate(R.layout.add_music_dialog, null);
                SongName = dialogLayout.findViewById(R.id.etMusicName);
                TextInputLayout MusicNameInputLayout = dialogLayout.findViewById(R.id.MusicNameInputLayout);
                textwatcher(MusicNameInputLayout, SongName);
                AlertDialog alertDialog = new MaterialAlertDialogBuilder(Manage_Playlist.this)
                        .setTitle("Song Name")
                        .setView(dialogLayout)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (SongName.getText().toString().matches("")) {
                                    // if song name is empty
                                    MusicNameInputLayout.setHelperText("Required*");
                                    Toast.makeText(v.getContext(), "Please enter song name", Toast.LENGTH_SHORT).show();
                                } else if (SongName.getText().toString().contains("`")) {
                                    // if song name contains the delimiter
                                    Toast.makeText(v.getContext(), "Playlist name cannot contain (`)", Toast.LENGTH_SHORT).show();
                                } else {
                                    // if song name is not empty and does not contain the delimiter
                                    pickAudio();
                                    dialog.dismiss();
                                }

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                alertDialog.show();
            }
        });
    }
    // function to pick the Audio from files
    private void pickAudio(){
        Intent intent_upload = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent_upload);
    }
    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try{
                            // if have result, get music data as an uri
                            audioUri = result.getData().getData();
                            StringURI = audioUri.toString();
                            // get the type of the file
                            ContentResolver contentResolver = Manage_Playlist.this.getContentResolver();
                            String mimeType = contentResolver.getType(audioUri);
                            if (mimeType.startsWith("audio")) {
                                // It's an audio file
                                // make the uri persistent and will last
                                getContentResolver().takePersistableUriPermission(audioUri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                // get the input stream
                                InputStream inStream = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    inStream = Manage_Playlist.this.getContentResolver().openInputStream(audioUri);
                                }
                                // Create an AudioDispatcher using TarsosDSP
                                int sampleRate = 44100;
                                int bufferSize = 1024;
                                int bufferOverlap = 128;
                                AudioDispatcher dispatcher = new AudioDispatcher(new UniversalAudioInputStream(inStream, new TarsosDSPAudioFormat(sampleRate, bufferSize, 1, true, true)), bufferSize, bufferOverlap);
                                // Create an MFCC object to compute MFCC coefficients
                                MFCC mfcc = new MFCC(bufferSize, sampleRate, 20, 50, 300, 3000);;
                                dispatcher.addAudioProcessor(mfcc);
                                dispatcher.addAudioProcessor(new AudioProcessor() {
                                    @Override
                                    public boolean process(AudioEvent audioEvent) {
                                        // process the audio and get float array of 20 elements and add to mfcc list
                                        float[] mfccBuffer = mfcc.getMFCC();
                                        mfccBuffer = Arrays.copyOfRange(mfccBuffer, 0,
                                                mfccBuffer.length);
                                        //Storing in arraylist
                                        mfccList.add(mfccBuffer);
                                        return true;
                                    }

                                    @Override
                                    public void processingFinished() {
                                        // MFCC processing is complete
                                    }
                                });
                                dispatcher.run();
                                // initialise the list
                                float[] meanMfccList = new float[20];
                                // iterate 20 times as the model need the average values for the 20 mfcc values
                                for (int i = 0; i < 20; i++) {
                                    float meanMfcc = 0;
                                    for (int mfccVal = 0; mfccVal < mfccList.size(); mfccVal++) {
                                        meanMfcc += mfccList.get(mfccVal)[i];
                                    }
                                    meanMfccList[i] = meanMfcc/mfccList.size();
                                }
                                // call the model to predict if the song is good for studying and store the outcome into a string
                                String ModelOutcome = classifyMusic(meanMfccList);
                                // create database handler to add song
                                MusicPlaylistDatabaseHandler dbHandler = new MusicPlaylistDatabaseHandler(Manage_Playlist.this, null, null, 1);
                                MusicPlaylist songlist =  dbHandler.getPlaylistFromID(PlaylistID);
                                if (songlist.SongNames.matches("")) {
                                    // if song list is empty
                                    songlist.SongNames = SongName.getText().toString();
                                    songlist.SongsURI = StringURI;
                                    songlist.SongIndicator = ModelOutcome;
                                } else {
                                    // if there are already songs in the song list
                                    songlist.SongNames += "`"+SongName.getText().toString();
                                    songlist.SongsURI += "`"+StringURI;
                                    songlist.SongIndicator += "`"+ModelOutcome;
                                }
                                // add the song into the database
                                dbHandler.AddSong(PlaylistID,songlist.SongsURI,songlist.SongNames,songlist.SongIndicator);
                                // refresh the page
                                Intent refresh = new Intent(Manage_Playlist.this,Manage_Playlist.class);
                                refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(refresh);
                                finish();
                            } else {
                                // Not an audio file
                                Toast.makeText(Manage_Playlist.this,"Please select an audio", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(Manage_Playlist.this,"No Audio Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
    // check the text if there is error
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
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.clearMediaPlayer(); // calling the method inside the adapter
    }
    public String classifyMusic(float[] data){
        int Pos = 0;
        try {
            // initiate the model
            MusicClassifierModel model = MusicClassifierModel.newInstance(Manage_Playlist.this);

            // Creates inputs for reference
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 20}, DataType.FLOAT32);
            // each float need 4 bytes and there are 20 floats to process so 4 times 20 bytes needed
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 20);
            byteBuffer.order(ByteOrder.nativeOrder());
            for (int i = 0; i<20; i++) {
                byteBuffer.putFloat(data[i]);
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result
            MusicClassifierModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidence = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence
            float MaxConfidence = 0;
            for (int i = 0; i<confidence.length;i++) {
                if (confidence[i] > MaxConfidence) {
                    MaxConfidence = confidence[i];
                    Pos = i;
                }

            }
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            Log.d("model", "Error");
        }
        // return the output of the model based on the confidence rate
        String[] classes = {"bad","medium","good"};
        return classes[Pos];
    }
}