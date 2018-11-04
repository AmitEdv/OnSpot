package com.example.amit.onspot;

import android.util.Log;

import com.flickr4java.flickr.photos.Photo;

/******************************
 * This class represents the required photo's metadata for the app.
 * In case the app requires a trait of an photo that is not yet exposed, modify this class.
 * In case of a change in the server API and the Photo object, modify this class to support the new API.
 */

class AppPhotoMetaData {
    private static final String TAG = "AppPhotoMetaData";

    private Photo mPhoto = null;

    AppPhotoMetaData(Photo serverPhotoMetadata) {
        Log.v(TAG, "AppPhotoMetaData()");
        mPhoto = serverPhotoMetadata;
    }
}
