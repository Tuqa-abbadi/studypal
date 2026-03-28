package com.example.studypal.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.studypal.database.AppDatabase;
import com.example.studypal.utils.NotificationHelper;

public class DailySummaryReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper.createChannel(context);

        AppDatabase db = AppDatabase.getInstance(context);
        long last24Hours = System.currentTimeMillis() - 24L * 60L * 60L * 1000L;
        int total = db.taskDao().getTotalTasksLast24Hours(last24Hours);
        int completed = db.taskDao().getCompletedTasksLast24Hours(last24Hours);

        String message = "You completed " + completed + " out of " + total + " tasks today.";
        NotificationHelper.show(context, "Daily Study Summary", message, 202);
    }
}
