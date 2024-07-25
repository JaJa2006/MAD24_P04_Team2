package com.example.main_activity;

import static android.content.Intent.getIntent;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SongForegroundService extends Service {
    String SongName;
    ArrayList<String> SongNameList;
    String SongURI;
    ArrayList<String> SongURIList;
    int pos = 0;
    boolean isPlaying = true;
    MediaPlayer myMPlayer;
    String duration;
    Handler handler;
    Runnable stopServiceRunnable;
    String MusicEnabled;


    @Override
    public void onCreate() {
        super.onCreate();
        // set up the music player if it is not set up yet
        if (myMPlayer == null) {
            myMPlayer = new MediaPlayer();
            myMPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        // set on complete listener to detect when the music stop
        myMPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                // Send PendingIntent to start the next song
                NextSongIntent();
            }
        });

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // check if music is enabled only make sure to do so only if the values is null
        if (MusicEnabled == null) {
            MusicEnabled = intent.getStringExtra("MusicEnabled");
        }
        duration = intent.getStringExtra("Time");
        // start the count down to stop the service
        ServiceStopTimer();
        // set up the music player
        if (myMPlayer == null) {
            myMPlayer = new MediaPlayer();
            myMPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        // allow for the service to be stopped
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case "STOP":
                    if (MusicEnabled.matches("1")) {
                        stopForeground(true);
                    }
                    handler.removeCallbacks(stopServiceRunnable);
                    Log.d("check","done");
                    stopSelf();
                    break;
            }
        }
        // set up all the intent actions for the navigation of the notification
        if (intent.getAction() != null && MusicEnabled.matches("1")) {
            switch (intent.getAction()) {
                case "PREVIOUS":
                    // case to go to the previous song
                    isPlaying = true;
                    if (pos == 0) {
                        pos = SongNameList.size()-1;
                    } else {
                        pos -= 1;
                    }
                    // stop the music and reset the media player
                    myMPlayer.reset();
                    // play the previous music
                    try {
                        myMPlayer.setDataSource(SongForegroundService.this, Uri.parse(SongURIList.get(pos)));
                        myMPlayer.prepare();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    myMPlayer.start();
                    notification();
                    break;
                case "PLAY":
                    // case to play or pause the song
                    if (isPlaying) {
                        isPlaying = false;
                        myMPlayer.pause();
                    } else {
                        isPlaying = true;
                        myMPlayer.start();
                    }
                    notification();
                    break;
                case "NEXT":
                    // case to switch to the next song
                    isPlaying = true;
                    if (pos == SongNameList.size()-1) {
                        pos = 0;
                    } else {
                        pos += 1;
                    }
                    // stop the music and reset the media player
                    myMPlayer.reset();
                    // play the  next music
                    try {
                        myMPlayer.setDataSource(SongForegroundService.this, Uri.parse(SongURIList.get(pos)));
                        myMPlayer.prepare();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    myMPlayer.start();
                    notification();
                    break;
            }
        } else if (MusicEnabled.matches("1")){
            // if first time setting up the notification, get all the data required and start the notification
            SongName = intent.getStringExtra("SongNames");
            SongURI = intent.getStringExtra("SongURI");
            if (SongName.matches("")) {

            } else {
                String [] SongNames = SongName.split("`");
                SongNameList = new ArrayList<String>(Arrays.asList(SongNames));
                String [] SongURIs = SongURI.split("`");
                SongURIList = new ArrayList<String>(Arrays.asList(SongURIs));
                // start foreground service of the notification
                startForeground(301,ShowNotification());
                // update the notification
                notification();
                // play the music
                try {
                    myMPlayer.setDataSource(SongForegroundService.this, Uri.parse(SongURIList.get(pos)));
                    myMPlayer.prepare();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                myMPlayer.start();
            }
        }


        return super.onStartCommand(intent, flags, startId);
    }

    private Notification ShowNotification() {
        // make the notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Song","SongPlayer",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this, "tag");
        // Add playback actions
        PendingIntent previousIntent = PendingIntent.getService(this, 0,
                new Intent(this, SongForegroundService.class).setAction("PREVIOUS"), PendingIntent.FLAG_IMMUTABLE);
        PendingIntent playPauseIntent = PendingIntent.getService(this, 0,
                new Intent(this, SongForegroundService.class).setAction("PLAY"), PendingIntent.FLAG_IMMUTABLE);
        PendingIntent nextIntent = PendingIntent.getService(this, 0,
                new Intent(this, SongForegroundService.class).setAction("NEXT"), PendingIntent.FLAG_IMMUTABLE);
        // Build and show notification
        return new NotificationCompat.Builder(this, "Song")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Study Music Player")
                .setContentText(SongNameList.get(pos))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.notification_skip_previous, "Previous", previousIntent)
                .addAction(isPlaying ? R.drawable.notification_pause : R.drawable.notification_play, "Play/Pause", playPauseIntent)
                .addAction(R.drawable.notification_skip_next, "Next", nextIntent)
                .setAutoCancel(true)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionCompat.getSessionToken())
                        .setShowActionsInCompactView(0, 1, 2) // Indices of the buttons to show in compact view
                        .setShowCancelButton(false))
                .build();
    }
    // task to be executed in the foreground service
    private void notification() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                updateNotification();
            }
        });
    }
    // show the notification and and updates to the notification
    private void updateNotification() {
        Notification notification = ShowNotification();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("SoNotificationReceiver", "No Notification!");
        }else{
            // notify the user
            notificationManager.notify(301, notification);
        }
    }
    private void NextSongIntent() {
        // create intent
        Intent intent = new Intent(this, SongForegroundService.class);
        intent.setAction("NEXT");

        // create pending intent
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // handle exception of sending pending intent
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
    public void ServiceStopTimer() {
        // to stop the service after the timer end
        if (handler == null && stopServiceRunnable == null){
            handler = new Handler();
            stopServiceRunnable = new Runnable() {
                @Override
                public void run() {
                    // create an intent
                    Intent intent = new Intent(SongForegroundService.this, TimerNotificationReceiver.class);
                    // create the intent for broadcast
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(SongForegroundService.this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    // handle exception of sending pending intent
                    try {
                        pendingIntent.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                    // Stop the service and remove notification
                    stopForeground(true);
                    stopSelf();
                }
            };
            // delay the runnable by the set amount of time by the user. The runnable will end the service
            handler.postDelayed(stopServiceRunnable, Long.parseLong(duration));
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // release the media player when the service end
        if (myMPlayer != null) {
            myMPlayer.release();
            myMPlayer = null;
        }
    }
}
