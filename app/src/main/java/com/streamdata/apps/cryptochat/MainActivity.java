package com.streamdata.apps.cryptochat;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.streamdata.apps.cryptochat.services.DateService;

public class MainActivity extends AppCompatActivity {

    boolean bound = false;
    ServiceConnection serviceConnection;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        intent = new Intent(this, DateService.class);

        serviceConnection = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.i("DateService", "MainActivity onServiceConnected");
                bound = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                Log.i("DateService", "MainActivity onServiceDisconnected");
                bound = false;
            }
        };

        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        startService(intent);

    }

    @Override
    protected void onPause() {
        if (bound) {
            Log.i("DateService", "MainActivity unbindService");
            unbindService(serviceConnection);
            bound = false;
        }

        super.onPause();
    }
}
