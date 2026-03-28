package com.example.studypal.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studypal.R;
import com.example.studypal.database.AppDatabase;
import com.example.studypal.models.Task;
import com.example.studypal.utils.NotificationHelper;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        EditText etTitle = findViewById(R.id.etTaskTitle);
        EditText etDescription = findViewById(R.id.etTaskDescription);
        Button btnSave = findViewById(R.id.btnSaveTask);

        AppDatabase db = AppDatabase.getInstance(this);

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            if (title.isEmpty()) {
                etTitle.setError("Title is required");
                return;
            }

            Task task = new Task(title, description, System.currentTimeMillis(), false);
            db.taskDao().insert(task);
            NotificationHelper.show(this, "Task saved", title + " added successfully.", 101);
            finish();
        });
    }
}
