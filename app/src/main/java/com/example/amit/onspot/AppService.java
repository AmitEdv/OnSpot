package com.example.amit.onspot;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.List;

public class AppService extends Service {

    private static final String TAG = "AppService";
    private static final int MIN_PHOTOS_TO_SHOW = 0;
    private static final int NOTIFICATIONS_ID = 0;

    private AppLocationManager mAppLocationManager;
    private LocationUpdateListener mLocationListener = new LocationUpdateListener();
    private  DataServer mDataServer = new DataServer();
    private NotificationManagerCompat mNotificationManager = null;
    private ReceivePhotosMetadataAroundLocationListener mReceivePhotosAroundLocationListener = new ReceivePhotosMetadataAroundLocationListener();

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate");
        mAppLocationManager = new AppLocationManager(this);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        mAppLocationManager.addLocationUpdateListener(mLocationListener);
        mAppLocationManager.startLocationMonitoring();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
        mAppLocationManager.removeLocationUpdateListener(mLocationListener);
    }

    private void notifyUser(String msgToUser, PendingIntent intentOfActivityToInvoke) {
        Log.v(TAG, "notifyUser called()");

        if (mNotificationManager == null) {
            initNotifications();
        }

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("OnSpot")              //mandatory
                .setContentText(msgToUser)              //mandatory
                .setSmallIcon(R.drawable.ic_default)    //mandatory
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(intentOfActivityToInvoke)
                .setAutoCancel(true);
        //Note: No use of unique ID at the moment

        Log.v(TAG, "set intent = " + intentOfActivityToInvoke);
        mNotificationManager.notify(NOTIFICATIONS_ID , builder.build());
    }

    private void initNotifications() {
        //TODO- once supporting Android 8, implement here the channels init
        mNotificationManager = NotificationManagerCompat.from(this);
    }

    private class ReceivePhotosMetadataAroundLocationListener implements IDataServerPhotoMetadataListListener {
        private static final String TAG = "AroundLocationListener";

        @Override
        public void onDataServerPhotoMetadataListReceive(List<AppPhotoMetaData> photoMetadataList) {
            Log.v(TAG, "onDataServerPhotoMetadataListReceive called()");
            if (photoMetadataList.size() >= MIN_PHOTOS_TO_SHOW) {
                Log.v(TAG, "Need to alert user");
                String msgToUser = getString(R.string.notification_text_found_photos_around_location, photoMetadataList.size());

                AppGlobals.getInstance().setCurrentLocationPhotosMetadata(photoMetadataList);
                Intent intent = new Intent(AppService.this, CurrentLocationPhotoGalleryActivity.class);
                //An activity that exists exclusively for responses to the notification.
                //There's no reason the user would navigate to this activity during normal app use,
                // so the activity starts a new task instead of being added to your app's existing
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(AppService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notifyUser(msgToUser, pendingIntent);
            }
        }

        @Override
        public void onError(String errorMsg) {
            Log.e(TAG, "Couldn't get photos");
        }
    }

    private class LocationUpdateListener implements IAppLocationListener {
        private static final String TAG = "LocationUpdateListener";

        @Override
        public void onLocationUpdate(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            Log.v(TAG, "onLocationUpdate() called with long = " + longitude + ", lat = " + latitude);

            mDataServer.getPhotosAroundALocation(longitude, latitude, CurrentLocationPhotoGalleryActivity.MAX_NUM_PHOTOS_TO_DISPLAY, mReceivePhotosAroundLocationListener);
        }
    }
}