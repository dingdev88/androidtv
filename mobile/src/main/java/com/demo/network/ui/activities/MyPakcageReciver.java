package com.demo.network.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.greenrobot.event.EventBus;


public class MyPakcageReciver extends BroadcastReceiver {
    private EventBus bus = EventBus.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        bus.post("refresh");
    }
}