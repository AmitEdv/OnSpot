package com.example.amit.onspot;

import android.util.Log;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;

/*package*/ class DataServer {

    private static final String TAG = "DataServer";

    private static final String API_KEY = "1cbf1754d8b2593e864bce045369a1e9";
    private static final String API_SECRET = "0069656dd0520e6b";
    private static final int PHOTOS_SEARCH_PARAM_LOCATION_RADIUS_KM = 3;

    private REST mRest = new REST();
    private Flickr mFlickrClient;

    /*package*/ PhotoList getPhotosAroundALocation(double latitude, double longitude)
    {
        Log.d(TAG, "getPhotosAroundALocation() called with latitude= " + latitude + "longitude = " + longitude);

        final String latitudeStr = String.valueOf(latitude);
        final String longitudeStr = String.valueOf(longitude);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    //TODO- extract to other public method
                    mFlickrClient = new Flickr(API_KEY, API_SECRET, mRest);

                    //TODO- seperate to private methods: one that searchs and another that sets the search params
                    SearchParameters searchParameters = new SearchParameters();
                    searchParameters.setLatitude(latitudeStr);
                    searchParameters.setLongitude(longitudeStr);
                    searchParameters.setRadius(PHOTOS_SEARCH_PARAM_LOCATION_RADIUS_KM); // Km around the given location where to search pictures

                    PhotoList photos = mFlickrClient.getPhotosInterface().search(searchParameters,5,1);
                    Log.d(TAG, "getPhotosAroundALocation() amount of photos found = " + photos.size());
                } catch (Exception ex) {
                    Log.d(TAG, "getPhotosAroundALocation() ERROR");
                    Log.d(TAG, ex.getLocalizedMessage());
                }
            }
        });

        thread.start();
        //TODO - how to return the photo list from the thread?!
        return null;
    }
}
