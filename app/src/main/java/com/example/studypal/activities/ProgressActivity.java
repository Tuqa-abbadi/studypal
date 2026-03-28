package com.example.studypal.activities;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studypal.R;
import com.example.studypal.database.AppDatabase;

public class ProgressActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView tvProgressText = findViewById(R.id.tvProgressText);
        TextView tvProgressDetail = findViewById(R.id.tvProgressDetail);

        AppDatabase db = AppDatabase.getInstance(this);
        long last24Hours = System.currentTimeMillis() - 24L * 60L * 60L * 1000L;

        int total = db.taskDao().getTotalTasksLast24Hours(last24Hours);
        int completed = db.taskDao().getCompletedTasksLast24Hours(last24Hours);
        int percent = total == 0 ? 0 : (completed * 100 / total);

        progressBar.setProgress(percent);
        tvProgressText.setText(percent + "% completed");
        tvProgressDetail.setText(completed + " of " + total + " tasks done");
    }
}
