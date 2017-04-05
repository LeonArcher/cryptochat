package com.streamdata.apps.cryptochat;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.lang.ref.WeakReference;
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
    private boolean isBound = false;

    public LoggingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        isBound = true;
        return new Binder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isBound = false;
        return true; // returning super.onUnbind(intent) will suppress additional calls of onBind and onUnbind
    }

    @Override
    public void onRebind(Intent intent) {
        isBound = true;
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (executor != null) {
            executor.shutdown();
        }
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(new LoggingTask(this), 0, LOG_INTERVAL_SECONDS, TimeUnit.SECONDS);

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

        private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
        private final WeakReference<LoggingService> parentReference;

        public LoggingTask(LoggingService parent) {
            this.parentReference = new WeakReference<>(parent);
        }

        @Override
        public void run() {
            boolean isBound = false;

            LoggingService parent = parentReference.get();
            if (parent != null) {
                isBound = parent.isBound;
            }

            String message = String.format("%s | bound state: %b", sdf.format(new Date()), isBound);
            Log.d(LOG_TAG, message);
        }
    }
}
