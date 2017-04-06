package com.streamdata.apps.cryptochat.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class DateService extends Service{
    private static Timer timer = new Timer();
    private static final long REPEAT_TIME = 1000 * 30;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timer.scheduleAtFixedRate(new Task(), 0, REPEAT_TIME);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class Task extends TimerTask
    {
        public void run()
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            Log.i("DateService", "Current date: " + dateFormat.format(date));
        }
    }
}
