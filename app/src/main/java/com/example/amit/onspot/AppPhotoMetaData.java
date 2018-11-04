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

    /*package*/ AppPhotoMetaData(Photo serverPhotoMetadata) {
        Log.v(TAG, "AppPhotoMetaData()");
        mPhoto = serverPhotoMetadata;
    }

    //Note! This method should be used by ServerData class ONLY!
    //To get photos info, please implement a relevant "get" method
    //which returns a NON-3rd party object, but an App Object
    //(e.x. do not return a specific object from Flickr's library, like com.flickr4java.flickr.photos.Photo)
    //TODO - find a more elegant and efficient way to alert developers about the usage of this method
    //TODO - Check: instead of risking exposing Photo object, return String of URL, then refactor the app to use:
    //URL url = new URL(urlString);
    //InputStream is = (InputStream)url.getContent();
    /*package*/ Photo getServerPhotoForDataServerUseOnly() {
        return mPhoto;
    }
}
