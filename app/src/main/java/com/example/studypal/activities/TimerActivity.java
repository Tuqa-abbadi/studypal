package com.example.studypal.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studypal.R;
import com.example.studypal.receivers.TimerReceiver;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity {
    private static final long FOCUS_DURATION = 25 * 60 * 1000L;
    private static final long BREAK_DURATION = 5 * 60 * 1000L;

    private TextView tvTimer, tvTimerMode;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = FOCUS_DURATION;
    private boolean isRunning = false;
    private boolean isFocusMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        tvTimer = findViewById(R.id.tvTimer);
        tvTimerMode = findViewById(R.id.tvTimerMode);
        Button btnStartTimer = findViewById(R.id.btnStartTimer);
        Button btnResetTimer = findViewById(R.id.btnResetTimer);

        updateTimerText();

        btnStartTimer.setOnClickListener(v -> {
            if (!isRunning) {
                startTimer();
            }
        });

        btnResetTimer.setOnClickListener(v -> resetTimer());
    }

    private void startTimer() {
        isRunning = true;
        scheduleFinishNotification(timeLeftInMillis);

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                isRunning = false;
                if (isFocusMode) {
                    isFocusMode = false;
                    tvTimerMode.setText("Break Time");
                    timeLeftInMillis = BREAK_DURATION;
                } else {
                    isFocusMode = true;
                    tvTimerMode.setText("Focus Time");
                    timeLeftInMillis = FOCUS_DURATION;
                }
                updateTimerText();
            }
        }.start();
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        cancelFinishNotification();
        isRunning = false;
        isFocusMode = true;
        tvTimerMode.setText("Focus Time");
        timeLeftInMillis = FOCUS_DURATION;
        updateTimerText();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }

    private void scheduleFinishNotification(long delayMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, TimerReceiver.class);
        intent.putExtra("mode", isFocusMode ? "Focus session finished" : "Break finished");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                401,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + delayMillis,
                    pendingIntent
            );
        }
    }

    private void cancelFinishNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, TimerReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                401,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
