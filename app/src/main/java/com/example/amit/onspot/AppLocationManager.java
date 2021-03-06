package com.example.amit.onspot;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import java.util.ArrayList;


/*package*/ class AppLocationManager {
    private static final String TAG = "AppLocationManager";

    private static final int LOCATION_INTERVAL_MIN_MS = 5000;
    private static final float LOCATION_DISTANCE_MIN_METERS = 500;
    private static final String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;

    private LocationManager mLocationManager = null;
    private Context mContext = null;
    private LocationListener[] mLocationListeners = new LocationListener[]{new LocationListener(LOCATION_PROVIDER)};
    private ArrayList<IAppLocationListener> mAppListeners = new ArrayList<>();

    /*package*/  AppLocationManager(Context context) {
        mContext = context;
        initializeLocationManager();
    }

    /*package*/ void startLocationMonitoring() {
        if (mLocationManager != null) {
            try {
                mLocationManager.requestLocationUpdates(
                        LOCATION_PROVIDER, LOCATION_INTERVAL_MIN_MS, LOCATION_DISTANCE_MIN_METERS,
                        mLocationListeners[0]);

            } catch (java.lang.SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "network provider does not exist, " + ex.getMessage());
            }
        }
    }

    /*package*/ void endLocationMonitoring() {
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO - handle request permissions
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listeners, ignore", ex);
                }
            }
        }
    }

    /*package*/  void addLocationUpdateListener(IAppLocationListener listener) {
        if(listener != null) {
            mAppListeners.add(listener);
        }
    }

    /*package*/  void removeLocationUpdateListener(IAppLocationListener listener) {
        mAppListeners.remove(listener);
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void notifyAppLocationListeners(Location location) {
        for(IAppLocationListener listener: mAppListeners) {
            listener.onLocationUpdate(location);
        }
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        /*package*/  LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            notifyAppLocationListeners(mLastLocation);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }
} //LocationManager
