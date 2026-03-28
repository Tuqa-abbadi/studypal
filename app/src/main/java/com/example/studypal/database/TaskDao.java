package com.example.studypal.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studypal.models.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    List<Task> getAllTasks();

    @Query("SELECT COUNT(*) FROM tasks WHERE createdAt >= :timeLimit")
    int getTotalTasksLast24Hours(long timeLimit);

    @Query("SELECT COUNT(*) FROM tasks WHERE completed = 1 AND createdAt >= :timeLimit")
    int getCompletedTasksLast24Hours(long timeLimit);
}
