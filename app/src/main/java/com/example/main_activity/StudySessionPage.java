package com.example.main_activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.se.omapi.Session;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class StudySessionPage extends AppCompatActivity {
    private EditText etMinutes;
    private TextInputLayout timeInputLayout;
    private TextView tvStart;
    private TextView tvEnd;
    private TextView tvTimer;
    private CountDownTimer timer;
    private long duration;
    private long endTime;
    private boolean timerRunning;
    private boolean serviceStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_study_session_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //StudySessionPage.this.deleteDatabase("PlaylistDB.db");
        // get all the elements from the xml
        etMinutes = findViewById(R.id.et_minutes);
        timeInputLayout = findViewById(R.id.timeImputLayout);
        tvStart = findViewById(R.id.tvStart);
        tvEnd = findViewById(R.id.tvEnd);
        tvTimer = findViewById(R.id.tv_timer);
        tvTimer.setVisibility(View.GONE);
        tvEnd.setEnabled(false);
        ImageView ivBack = findViewById(R.id.ivSessionBack);
        TextView tvCreatePlaylist = findViewById(R.id.tvCreatePlaylist);

        // back button to go back to the main activity page
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // button to create new music playlist
        tvCreatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogLayout = LayoutInflater.from(StudySessionPage.this).inflate(R.layout.add_music_playlist_dialog, null);
                TextInputEditText PlaylistName = dialogLayout.findViewById(R.id.etPlaylistName);
                AlertDialog alertDialog = new MaterialAlertDialogBuilder(StudySessionPage.this)
                        .setTitle("Create Playlist")
                        .setView(dialogLayout)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (PlaylistName.getText().toString().matches("")) {
                                    Toast.makeText(v.getContext(), "Please enter playlist name", Toast.LENGTH_SHORT).show();
                                } else if (PlaylistName.getText().toString().contains("`")) {
                                    Toast.makeText(v.getContext(), "Playlist name cannot contain (`)", Toast.LENGTH_SHORT).show();
                                } else {
                                    // if playlist name is not empty
                                    MusicPlaylist playlist = new MusicPlaylist(PlaylistName.getText().toString(),"","","0");
                                    // create database handler to add playlist
                                    MusicPlaylistDatabaseHandler dbHandler = new MusicPlaylistDatabaseHandler(StudySessionPage.this, null, null, 1);
                                    dbHandler.addPlaylist(playlist);
                                    dialog.dismiss();
                                    // refresh the page
                                    Intent refresh = new Intent(v.getContext(),StudySessionPage.class);
                                    refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(refresh);
                                    finish();
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

        // onclick listener for the start timer
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMinutes.getText().toString().isEmpty()) {
                    // if time is not set
                    Toast.makeText(v.getContext(), "Please enter the minutes", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(etMinutes.getText().toString()) < 1 || Integer.parseInt(etMinutes.getText().toString()) > 60) {
                    // if time is less than 1 minute or more than 60 minute
                    Toast.makeText(v.getContext(), "Please enter a value between 1 and 60 minutes", Toast.LENGTH_SHORT).show();
                } else {
                    // start the timer and set the page to show the timer
                    timerRunning = true;
                    duration = TimeUnit.MINUTES.toMillis(Integer.parseInt(etMinutes.getText().toString()));
                    tvTimer.setVisibility(View.VISIBLE);
                    timeInputLayout.setVisibility(View.GONE);
                    tvStart.setEnabled(false);
                    tvEnd.setEnabled(true);
                    // function to start timer
                    startTimer();
                    timer.start();
                    // Initialize Notification Channel
                    createNotificationChannel();
                    // Schedule Alarm
                    SessionAlarm.scheduleAlarm(v.getContext(),duration);
                    // get the song data
                    String SongName;
                    String SongURI;
                    try {
                        MusicPlaylistDatabaseHandler dbHandler = new MusicPlaylistDatabaseHandler(StudySessionPage.this, null, null, 1);
                        SongName = dbHandler.getSelectedSongName();
                        SongURI = dbHandler.getSelectedSongURI();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(v.getContext(), SongName, Toast.LENGTH_SHORT).show();
                    // start foreground service for music player
                    if (SongName.matches("") || SongURI.matches("")) {
                    } else {
                        Intent foregroundService = new Intent(StudySessionPage.this,SongForegroundService.class);
                        foregroundService.putExtra("SongNames",SongName);
                        foregroundService.putExtra("SongURI",SongURI);
                        foregroundService.putExtra("Time",""+duration);
                        startService(foregroundService);
                        serviceStart = true;
                    }

                }
            }
        });
        // button to end the timer
        tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceStart) {
                    // create pending intent to stop the service
                    PendingIntent pendingIntent = PendingIntent.getService(v.getContext(), 0, new Intent(v.getContext(),
                            SongForegroundService.class).setAction("STOP"), PendingIntent.FLAG_IMMUTABLE);
                    // handle exception of sending pending intent
                    try {

                        pendingIntent.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                }
                serviceStart = false;
                //set all the elements to be able to set timer
                timerRunning = false;
                tvTimer.setVisibility(View.GONE);
                timeInputLayout.setVisibility(View.VISIBLE);
                tvStart.setEnabled(true);
                tvEnd.setEnabled(false);
                // cancel timer
                timer.cancel();
                // cancel alarm
                SessionAlarm.cancelAlarm(v.getContext());
            }
        });
    }
    // function to update the timer
    private void updateTimer(long timeLeft) {
        // get minutes and seconds
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft) -
                TimeUnit.MINUTES.toSeconds(minutes);
        // set the display for the time left
        tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
    }

    // function to start the timer
    private void startTimer() {
        // get the time when the timer should end
        endTime = System.currentTimeMillis()+duration;
        // set up the timer with the duration and an countdown interval of 1000
        timer = new CountDownTimer(duration, 1000) {
            // for each tick update the timer
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimer(millisUntilFinished);
            }

            // when the timer ends
            @Override
            public void onFinish() {
                // set all the timer elements back to allow user to set a new timer
                timerRunning = false;
                tvTimer.setVisibility(View.GONE);
                timeInputLayout.setVisibility(View.VISIBLE);
                tvStart.setEnabled(true);
                tvEnd.setEnabled(false);
                timer.cancel();
                Toast.makeText(StudySessionPage.this, "Time's up!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    // to handle the timer when the user exits the activity
    @Override
    protected void onStop() {
        // set up the shared preference to get the duration left, if the timer is still running and the end time of the timer
        SharedPreferences preferences = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("millisleft",duration);
        editor.putBoolean("timerRunning",timerRunning);
        editor.putLong("endTime",endTime);
        editor.apply();
        // stop the timer if it is running
        if (timer != null){
            timer.cancel();
        }
        super.onStop();
    }

    // when the activity is started
    @Override
    protected void onStart() {
        // when the activity starts get the shared preference values for the duration and if the timer was running before
        SharedPreferences preferences = getSharedPreferences("prefs",MODE_PRIVATE);
        duration = preferences.getLong("millisleft",60000);
        timerRunning = preferences.getBoolean("timerRunning",false);
        // check if the timer was running before
        if (timerRunning) {
            // if is running, get the end time and calculate the duration left by getting end time minus current time
            endTime = preferences.getLong("endTime",0);
            duration = endTime-System.currentTimeMillis();
            // check if timer is still going or not
            if (duration<0) {
                // if timer is up, set the values
                duration = 0;
                timerRunning = false;
                updateTimer(duration);
            } else {
                // if timer is still running
                // start the timer with the amount of time left
                tvTimer.setVisibility(View.VISIBLE);
                timeInputLayout.setVisibility(View.GONE);
                tvStart.setEnabled(false);
                tvEnd.setEnabled(true);
                startTimer();
                timer.start();
            }
        }
        super.onStart();
    }
    // update the page with the data base when the page is reloaded
    @Override
    protected void onResume() {
        super.onResume();
        // get the memo from the data base
        MusicPlaylistDatabaseHandler dbHandler = new MusicPlaylistDatabaseHandler(StudySessionPage.this, null, null, 1);
        ArrayList<MusicPlaylist> playlists = dbHandler.getPlaylist();
        // get the recyclerview from the XML
        RecyclerView recyclerView = findViewById(R.id.rvPlaylist);
        // fill the layout with the information from the data base
        MusicPlaylistAdapter mAdapter = new MusicPlaylistAdapter(playlists,this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
    // create the notification channel
    private void createNotificationChannel() {
        // check if the build version is more than the oreo which is 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TimerChannel";
            String description = "Channel for Timer";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Timer",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}