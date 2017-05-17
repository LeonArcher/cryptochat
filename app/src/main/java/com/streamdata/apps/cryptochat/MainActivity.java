package com.streamdata.apps.cryptochat;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Intent intentService;
    private ServiceConnection connection;
    private boolean serviceIsBound = false;
    private DateProviderService.DateProviderBinder serviceBinder;
    private TextView dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateView = (TextView) findViewById(R.id.dateTextView);

        intentService = new Intent(this, DateProviderService.class);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                serviceBinder = (DateProviderService.DateProviderBinder) service;
                serviceIsBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // called only in extreme situations, so we need to duplicate this in onPause
                serviceIsBound = false;
            }
        };

        startService(intentService);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(intentService, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (serviceIsBound) {
            unbindService(connection);
            serviceIsBound = false;
        }
    }

    public void onClickGetDate(View view) {
        if (serviceIsBound) {
            String newText = serviceBinder.getCurrentDate();

            if (newText != null) {
                dateView.setText(newText);
            }
        }
    }
}
