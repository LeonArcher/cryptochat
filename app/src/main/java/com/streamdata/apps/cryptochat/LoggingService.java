package com.streamdata.apps.cryptochat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoggingService extends Service {
    public static final String LOG_TAG = "LoggingService";
    public static final long LOG_INTERVAL_SECONDS = 30;

    private ScheduledExecutorService executor = null;

    public LoggingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (executor != null) {
            executor.shutdown();
        }
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(new LoggingTask(), 0, LOG_INTERVAL_SECONDS, TimeUnit.SECONDS);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (executor != null) {
            executor.shutdown();
        }

        super.onDestroy();
    }

    private static class LoggingTask implements Runnable {

        private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);

        @Override
        public void run() {
            Log.d(LOG_TAG, sdf.format(new Date()));
        }
    }
}
