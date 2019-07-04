package com.razu.weather.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, NotificationService.class);
        context.startService(i);
        // Toast.makeText(context, "Booting Completed", Toast.LENGTH_LONG).show();
    }
}