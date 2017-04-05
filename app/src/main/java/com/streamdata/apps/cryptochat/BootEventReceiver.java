package com.streamdata.apps.cryptochat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootEventReceiver extends BroadcastReceiver {
    public BootEventReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // Starting the logging service at device boot
        Intent loggingServiceIntent = new Intent(context, DateProviderService.class);
        context.startService(loggingServiceIntent);
    }
}
