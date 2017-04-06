package com.streamdata.apps.cryptochat;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.streamdata.apps.cryptochat.services.DateService;

public class MainActivity extends AppCompatActivity {

    boolean bound = false;
    boolean isStarted = false;
    DateService myService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, DateService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
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

    public void selfDestruct(View view) {
        TextView textView = (TextView)findViewById(R.id.myTextView);

        if (bound) {
            textView.setText(myService.getDate());
        } else {
            textView.setText("Cannot bind DateService");
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected (ComponentName name, IBinder binder){
            Log.i("DateService", "MainActivity onServiceConnected");
            bound = true;

            DateService.LocalBinder mLocalBinder = (DateService.LocalBinder) binder;
            myService = mLocalBinder.getService();

        }

        public void onServiceDisconnected(ComponentName name) {
            Log.i("DateService", "MainActivity onServiceDisconnected");
            bound = false;
        }
    };

}
