package com.example.studypal.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.studypal.utils.NotificationHelper;

public class TimerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper.createChannel(context);
        String mode = intent.getStringExtra("mode");
        if (mode == null) {
            mode = "Timer finished";
        }
        NotificationHelper.show(context, "StudyPal Timer", mode, 201);
    }
}
