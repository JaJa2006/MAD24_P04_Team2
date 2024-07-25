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
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.se.omapi.Session;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class StudySessionPage extends AppCompatActivity {
    private EditText etMinutes;
    private TextInputLayout timeInputLayout;
    private TextInputLayout TimerOptionInputLayout;
    private TextView tvStart;
    private TextView tvEnd;
    private TextView tvTimer;
    private CountDownTimer timer;
    private long duration;
    private long endTime;
    private boolean timerRunning;
    private boolean serviceStart = false;
    public int TimerType;
    public boolean TimerTypeSelected = false;
    public TextView tvTimerStatus;

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
        timeInputLayout = findViewById(R.id.timeInputLayout);
        TimerOptionInputLayout = findViewById(R.id.TimerOptionInputLayout);
        tvStart = findViewById(R.id.tvStart);
        tvEnd = findViewById(R.id.tvEnd);
        tvTimer = findViewById(R.id.tv_timer);
        tvTimer.setVisibility(View.GONE);
        tvEnd.setEnabled(false);
        tvEnd.setBackgroundResource(R.drawable.button_outline_unactive);
        tvEnd.setTextColor(Color.parseColor("#FF939495"));
        tvTimerStatus = findViewById(R.id.tv_timerStatus);
        ImageView ivBack = findViewById(R.id.ivSessionBack);
        TextView tvCreatePlaylist = findViewById(R.id.tvCreatePlaylist);
        MaterialAutoCompleteTextView TimerInput = findViewById(R.id.TimerInput);

        tvTimerStatus.setText("");

        // back button to go back to the main activity page
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // text watcher
        textwatcher(timeInputLayout, etMinutes);
        textwatcher(TimerOptionInputLayout, TimerInput);

        // Timer option list
        TimerInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        TimerType = 0;
                        timeInputLayout.setVisibility(View.VISIBLE);
                        timeInputLayout.setHint("Time in Minutes");
                        TimerTypeSelected = true;
                        break;
                    case 1:
                        TimerType = 1;
                        timeInputLayout.setVisibility(View.VISIBLE);
                        timeInputLayout.setHint("Number of Repetitions");
                        TimerTypeSelected = true;
                        break;
                    case 2:
                        TimerType = 2;
                        timeInputLayout.setVisibility(View.VISIBLE);
                        timeInputLayout.setHint("Number of Repetitions");
                        TimerTypeSelected = true;
                        break;
                    case 3:
                        TimerType = 3;
                        timeInputLayout.setVisibility(View.VISIBLE);
                        timeInputLayout.setHint("Number of Repetitions");
                        TimerTypeSelected = true;
                        break;
                    case 4:
                        TimerType = 4;
                        timeInputLayout.setVisibility(View.GONE);
                        TimerTypeSelected = true;
                        break;
                }
            }
        });

        // button to create new music playlist
        tvCreatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogLayout = LayoutInflater.from(StudySessionPage.this).inflate(R.layout.add_music_playlist_dialog, null);
                TextInputEditText PlaylistName = dialogLayout.findViewById(R.id.etPlaylistName);
                TextInputLayout PlaylistNameInputLayout = dialogLayout.findViewById(R.id.PlaylistNameInputLayout);
                textwatcher(PlaylistNameInputLayout, PlaylistName);
                AlertDialog alertDialog = new MaterialAlertDialogBuilder(StudySessionPage.this)
                        .setTitle("Create Playlist")
                        .setView(dialogLayout)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (PlaylistName.getText().toString().matches("")) {
                                    PlaylistNameInputLayout.setHelperText("Required*");
                                    Toast.makeText(v.getContext(), "Please enter playlist name", Toast.LENGTH_SHORT).show();
                                } else if (PlaylistName.getText().toString().contains("`")) {
                                    Toast.makeText(v.getContext(), "Playlist name cannot contain (`)", Toast.LENGTH_SHORT).show();
                                } else {
                                    // if playlist name is not empty
                                    MusicPlaylist playlist = new MusicPlaylist(PlaylistName.getText().toString(),"","","","0");
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
                if ((etMinutes.getText().toString().isEmpty() && (TimerType < 4 && TimerType > -1)) || !TimerTypeSelected) {
                    // if time is not set
                    if (TimerInput.getText().toString().matches("")) {
                        shakeEditText(TimerInput);
                        TimerOptionInputLayout.setHelperText("Required*");
                    }
                    if (etMinutes.getText().toString().matches("")) {
                        shakeEditText(etMinutes);
                        timeInputLayout.setHelperText("Required*");
                    }
                    Toast.makeText(v.getContext(), "Please make sure there are no empty Fields", Toast.LENGTH_SHORT).show();
                } else if ((Integer.parseInt(etMinutes.getText().toString()) < 1 && TimerType == 0) || (Integer.parseInt(etMinutes.getText().toString()) > 60 && TimerType == 0)) {
                    // if time is less than 1 minute or more than 60 minute for TimerType 0
                    shakeEditText(etMinutes);
                    Toast.makeText(v.getContext(), "Please enter a value between 1 and 60 minutes", Toast.LENGTH_SHORT).show();
                } else if ((TimerType < 4 && TimerType > 0) && (Integer.parseInt(etMinutes.getText().toString()) < 1 || Integer.parseInt(etMinutes.getText().toString()) > 5)) {
                    // if the repetition for timer type 1, 2, 3 is less than 1 or more than 10
                    shakeEditText(etMinutes);
                    Toast.makeText(v.getContext(), "Please enter a value between 1 and 5 repetition", Toast.LENGTH_SHORT).show();
                } else {
                    // start the timer and set the page to show the timer
                    timerRunning = true;
                    // setting the time based on the timer type
                    switch (TimerType) {
                        case 0:
                            duration = TimeUnit.MINUTES.toMillis(Integer.parseInt(etMinutes.getText().toString()));
                            break;
                        case 1:
                            duration = TimeUnit.MINUTES.toMillis(Integer.parseInt(etMinutes.getText().toString()))*30;
                            break;
                        case 2:
                            duration = TimeUnit.MINUTES.toMillis(Integer.parseInt(etMinutes.getText().toString()))*69;
                            break;
                        case 3:
                            duration = TimeUnit.MINUTES.toMillis(Integer.parseInt(etMinutes.getText().toString()))*120;
                            break;
                        case 4:
                            duration = TimeUnit.MINUTES.toMillis(105);
                            break;
                    }
                    tvStart.setBackgroundResource(R.drawable.button_unactive);
                    tvEnd.setBackgroundResource(R.drawable.button_outline);
                    tvEnd.setTextColor(Color.parseColor("#025B81"));
                    tvTimer.setVisibility(View.VISIBLE);
                    timeInputLayout.setVisibility(View.GONE);
                    TimerOptionInputLayout.setVisibility(View.GONE);
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
                    // start foreground service for music player
                    if (SongName.matches("") || SongURI.matches("")) {
                    } else {
                        Intent foregroundService = new Intent(StudySessionPage.this,SongForegroundService.class);
                        foregroundService.putExtra("SongNames",SongName);
                        foregroundService.putExtra("SongURI",SongURI);
                        foregroundService.putExtra("SongIndicators",SongURI);
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
                tvStart.setBackgroundResource(R.drawable.button);
                tvEnd.setBackgroundResource(R.drawable.button_outline_unactive);
                tvEnd.setTextColor(Color.parseColor("#FF939495"));
                timerRunning = false;
                tvTimerStatus.setText("");
                tvTimer.setVisibility(View.GONE);
                timeInputLayout.setVisibility(View.VISIBLE);
                TimerOptionInputLayout.setVisibility(View.VISIBLE);
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
        // set the display for the time left for the different timer type
        switch (TimerType) {
            case 0:
                tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
                tvTimerStatus.setText("Study");
                tvTimerStatus.setTextColor(Color.parseColor("#FF0000"));
                break;
            case 1:
                tvTimer.setText(String.format("%02d:%02d", (minutes%30>=5) ? (minutes%30)-5 : minutes%5, seconds));
                tvTimerStatus.setText((minutes%30>=5) ? "Study" : "Break");
                tvTimerStatus.setTextColor(Color.parseColor((minutes%30>=15) ? "#FF0000" : "#4CC500"));
                break;
            case 2:
                tvTimer.setText(String.format("%02d:%02d", (minutes%69>=17) ? (minutes%69)-17 : minutes%69, seconds));
                tvTimerStatus.setText((minutes%69>=17) ? "Study" : "Break");
                tvTimerStatus.setTextColor(Color.parseColor((minutes%69>=17) ? "#FF0000" : "#4CC500"));
                break;
            case 3:
                tvTimer.setText(String.format("%02d:%02d", (minutes%120>=30) ? (minutes%120)-30 : minutes%120, seconds));
                tvTimerStatus.setText((minutes%120>=30) ? "Study" : "Break");
                tvTimerStatus.setTextColor(Color.parseColor((minutes%120>=30) ? "#FF0000" : "#4CC500"));
                break;
            case 4:
                tvTimer.setText(String.format("%02d:%02d", (minutes>=45) ? minutes-45 : (minutes>15) ? minutes-15 : minutes, seconds));
                tvTimerStatus.setText((minutes>=45) ? "1 Big task" : (minutes>15) ? "3 Medium Task" : "5 Small Task");
                tvTimerStatus.setTextColor(Color.parseColor((minutes>=45) ? "#FF0000" : (minutes>15) ? "#C59A00" : "#4CC500"));
                break;
        }

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
                TimerOptionInputLayout.setVisibility(View.VISIBLE);
                timeInputLayout.setVisibility(View.VISIBLE);
                tvTimerStatus.setText("");
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
        editor.putInt("TimerType",TimerType);
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
        TimerType = preferences.getInt("TimerType",0);
        duration = preferences.getLong("millisleft",60000);
        timerRunning = preferences.getBoolean("timerRunning",false);
        // check if the timer was running before
        if (timerRunning) {
            // set colour
            tvStart.setBackgroundResource(R.drawable.button_unactive);
            tvEnd.setBackgroundResource(R.drawable.button_outline);
            tvEnd.setTextColor(Color.parseColor("#025B81"));
            // if is running, get the end time and calculate the duration left by getting end time minus current time
            endTime = preferences.getLong("endTime",0);
            duration = endTime-System.currentTimeMillis();
            // check if timer is still going or not
            if (duration<0) {
                // if timer is up, set the values
                duration = 0;
                timerRunning = false;
                updateTimer(duration);
                tvTimerStatus.setText("");
                tvStart.setBackgroundResource(R.drawable.button);
                tvEnd.setBackgroundResource(R.drawable.button_outline_unactive);
                tvEnd.setTextColor(Color.parseColor("#FF939495"));
            } else {
                // if timer is still running
                // start the timer with the amount of time left
                tvTimer.setVisibility(View.VISIBLE);
                timeInputLayout.setVisibility(View.GONE);
                TimerOptionInputLayout.setVisibility(View.GONE);
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