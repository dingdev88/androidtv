package com.selecttvapp.channels;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.selecttvapp.ui.helper.MyApplication;

/**
 * Created by babin on 8/23/2017.
 */

public class ClosingService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ClearFromRecentService", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("ClearFromRecentService", "Service Destroyed");
        if(MyApplication.getmWebService()!=null){
           // MyApplication.getmWebService().clearCache();
        }
        super.onDestroy();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        //Code here
        stopSelf();
    }


}
