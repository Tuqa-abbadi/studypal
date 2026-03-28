package com.example.studypal.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.studypal.R;
import com.example.studypal.utils.NotificationHelper;
import com.example.studypal.utils.SchedulerHelper;

public class UserInfoActivity extends AppCompatActivity {
    private EditText etName;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        NotificationHelper.createChannel(this);
        SchedulerHelper.scheduleDailySummary(this);
        requestNotificationPermissionIfNeeded();

        etName = findViewById(R.id.etName);
        btnStart = findViewById(R.id.btnStart);

        SharedPreferences prefs = getSharedPreferences("StudyPalPrefs", MODE_PRIVATE);
        String savedName = prefs.getString("username", "");
        if (!savedName.isEmpty()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        btnStart.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (!name.isEmpty()) {
                prefs.edit().putString("username", name).apply();
                startActivity(new Intent(UserInfoActivity.this, MainActivity.class));
                finish();
            } else {
                etName.setError("Please enter your name");
            }
        });
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 200);
        }
    }
}
