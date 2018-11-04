package com.example.amit.onspot;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.List;

public class AppService extends Service {

    private static final String TAG = "AppService";
    private static final int MIN_PHOTOS_TO_SHOW = 0;
    private static final int NOTIFICATIONS_ID = 0;

    DataServer mDataServer = new DataServer();
    NotificationManagerCompat mNotificationManager = null;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate");
        //TODO- utilize the LocationManager
        //AppLocationManager mAppLocationManager = new AppLocationManager(this);

        double latitude = 32.0740381;
        double longitude = 34.7756635;
        mDataServer.getPhotosAroundALocation(latitude, longitude, new AppServerReceivePhotosAroundLocationListener());
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
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

    private class AppServerReceivePhotosAroundLocationListener implements IDataServerPhotoMetadataListListener {

        private static final String TAG = "AroundLocationListener";

        @Override
        public void onDataServerPhotoMetadataListReceive(List<AppPhotoMetaData> photoMetadataList) {
            Log.v(TAG, "onDataServerPhotoMetadataListReceive called()");
            if (photoMetadataList.size() >= MIN_PHOTOS_TO_SHOW) {
                Log.v(TAG, "Need to alert user");
                String msgToUser = getString(R.string.notification_text_found_photos_around_location, photoMetadataList.size());

                ///////////////////
                //DEBUG TODO- comment out if not on debug mode
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //DEBUG
                ///////////////////
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
}