package com.example.amit.onspot;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class AppService extends Service {

    private static final String TAG = "AppService";

    DataServer mDataServer = new DataServer();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        //TODO- utilize the LocationManager
        //AppLocationManager mAppLocationManager = new AppLocationManager(this);

        double latitude = 32.0740381;
        double longitude = 34.7756635;
        mDataServer.getPhotosAroundALocation(latitude, longitude);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }
}