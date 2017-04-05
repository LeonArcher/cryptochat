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

public class DateProviderService extends Service {
    public static final String LOG_TAG = "DateProviderService";
    public static final long SAVING_INTERVAL_SECONDS = 30;

    private ScheduledExecutorService executor = null;
    private String currentDateStr = "";
    private final DateProviderBinder dateProvider = new DateProviderBinder();

    public DateProviderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return dateProvider;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true; // returning super.onUnbind(intent) will suppress additional calls of onBind and onUnbind
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (executor != null) {
            executor.shutdown();
        }
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(new DateMonitoringTask(this), 0, SAVING_INTERVAL_SECONDS, TimeUnit.SECONDS);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (executor != null) {
            executor.shutdown();
        }

        super.onDestroy();
    }

    private static class DateMonitoringTask implements Runnable {

        private final WeakReference<DateProviderService> parentReference;
        private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);

        public DateMonitoringTask(DateProviderService parent) {
            this.parentReference = new WeakReference<>(parent);
        }

        @Override
        public void run() {
            DateProviderService parent = parentReference.get();

            if (parent != null) {
                parent.currentDateStr = sdf.format(new Date());
                Log.d(LOG_TAG, "Saving date");

            } else {
                Log.d(LOG_TAG, "Can't save date - parent is null");
            }
        }
    }

    public class DateProviderBinder extends Binder {
        String getCurrentDate() {
            return currentDateStr;
        }
    }
}
