package com.example.main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.TimeUnit;

public class StudySessionActivity extends AppCompatActivity {

    private EditText etMinutes;
    private Button btnStart;
    private TextView tvTimer;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_session);

        etMinutes = findViewById(R.id.et_minutes);
        btnStart = findViewById(R.id.btn_start);
        tvTimer = findViewById(R.id.tv_timer);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });
    }

    private void startTimer() {
        String minutesStr = etMinutes.getText().toString();
        if (minutesStr.isEmpty()) {
            Toast.makeText(this, "Please enter the minutes", Toast.LENGTH_SHORT).show();
            return;
        }

        int minutes = Integer.parseInt(minutesStr);
        if (minutes < 1 || minutes > 60) {
            Toast.makeText(this, "Please enter a value between 1 and 60 minutes", Toast.LENGTH_SHORT).show();
            return;
        }

        long duration = TimeUnit.MINUTES.toMillis(minutes);

        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(minutes);
                tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                Toast.makeText(StudySessionActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
            }
        }.start();

        // Navigate to the next activity/layout
        Intent intent = new Intent(this, StudySessionActivity2.class);
        startActivity(intent);
    }
}

