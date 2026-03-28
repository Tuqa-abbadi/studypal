package com.example.studypal.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studypal.R;
import com.example.studypal.adapters.TaskAdapter;
import com.example.studypal.database.AppDatabase;
import com.example.studypal.models.Task;
import com.example.studypal.utils.NotificationHelper;
import com.example.studypal.utils.SchedulerHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskActionListener {

    private TextView tvGreeting, tvEmpty;
    private RecyclerView recyclerTasks;
    private TaskAdapter adapter;
    private AppDatabase db;
    private final List<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationHelper.createChannel(this);
        SchedulerHelper.scheduleDailySummary(this);

        db = AppDatabase.getInstance(this);

        tvGreeting = findViewById(R.id.tvGreeting);
        tvEmpty = findViewById(R.id.tvEmpty);
        recyclerTasks = findViewById(R.id.recyclerTasks);
        Button btnAddTask = findViewById(R.id.btnAddTask);
        Button btnTimer = findViewById(R.id.btnTimer);
        ImageButton btnProgress = findViewById(R.id.btnProgress);

        SharedPreferences prefs = getSharedPreferences("StudyPalPrefs", MODE_PRIVATE);
        String name = prefs.getString("username", "Student");
        tvGreeting.setText("Hello, " + name + " 👋");

        adapter = new TaskAdapter(taskList, this);
        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerTasks.setAdapter(adapter);

        btnAddTask.setOnClickListener(v -> startActivity(new Intent(this, AddTaskActivity.class)));
        btnTimer.setOnClickListener(v -> startActivity(new Intent(this, TimerActivity.class)));
        btnProgress.setOnClickListener(v -> startActivity(new Intent(this, ProgressActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks() {
        taskList.clear();
        taskList.addAll(db.taskDao().getAllTasks());
        adapter.setTaskList(taskList);
        tvEmpty.setVisibility(taskList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onTaskChecked(Task task, boolean checked) {
        task.setCompleted(checked);
        db.taskDao().update(task);
        loadTasks();
    }

    @Override
    public void onTaskDelete(Task task) {
        db.taskDao().delete(task);
        NotificationHelper.show(this, "Task deleted", task.getTitle() + " was removed.", 102);
        loadTasks();
    }
}
