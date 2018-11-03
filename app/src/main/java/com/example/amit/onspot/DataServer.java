package com.example.amit.onspot;

import android.util.Log;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;

import java.util.ArrayList;
import java.util.List;

/*package*/ class DataServer {

    private static final String TAG = "DataServer";

    private static final String API_KEY = "1cbf1754d8b2593e864bce045369a1e9";
    private static final String API_SECRET = "0069656dd0520e6b";
    private static final int PHOTOS_SEARCH_PARAM_LOCATION_RADIUS_KM = 3;
    private static final int PHOTOS_SEARCH_PARAM_NUM_OF_PHOTOS_PER_PAGE = 5;
    private static final int PHOTOS_SEARCH_PARAM_NUM_OF_PAGE = 1;

    private REST mRest = new REST();
    private Flickr mFlickrClient;

    /*package*/ void getPhotosAroundALocation(double latitude, double longitude, final IDataServerPhotoMetadataListListener listener) {
        Log.d(TAG, "getPhotosAroundALocation() called with latitude= " + latitude + "longitude = " + longitude);

        final String latitudeStr = String.valueOf(latitude);
        final String longitudeStr = String.valueOf(longitude);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    //TODO- extract to other public method
                    mFlickrClient = new Flickr(API_KEY, API_SECRET, mRest);
                } catch (Exception ex) {
                    Log.d(TAG, "getPhotosAroundALocation() ERROR creating flickr client");
                    Log.d(TAG, ex.getLocalizedMessage());
                }

                try {
                    //TODO- separate to private methods: one that sets the search params, and another that searches (for reuse purposes)
                    SearchParameters searchParameters = new SearchParameters();
                    searchParameters.setLatitude(latitudeStr);
                    searchParameters.setLongitude(longitudeStr);
                    searchParameters.setRadius(PHOTOS_SEARCH_PARAM_LOCATION_RADIUS_KM);

                    PhotoList photos = mFlickrClient.getPhotosInterface().search(searchParameters,
                            PHOTOS_SEARCH_PARAM_NUM_OF_PHOTOS_PER_PAGE, PHOTOS_SEARCH_PARAM_NUM_OF_PAGE);
                    Log.d(TAG, "getPhotosAroundALocation() amount of photos found = " + photos.size());

                    listener.onDataServerPhotoMetadataListReceive(createAppPhotoMetadataList(photos));
                } catch (Exception ex) {
                    Log.d(TAG, "getPhotosAroundALocation() ERROR searching for photos");
                    Log.d(TAG, ex.getLocalizedMessage());
                    listener.onError(ex.getLocalizedMessage());
                }
            }
        });

        thread.start();
    }

    private List<AppPhotoMetaData> createAppPhotoMetadataList(PhotoList serverPhotoMetadataList) {
        Log.d(TAG, "createAppPhotoMetadataList() called()");

        //Note: PhotoList extends ArrayList<Photo>
        List<Photo> serverList = (List<Photo>)serverPhotoMetadataList;
        List<AppPhotoMetaData> appPhotosMetadeta = new ArrayList<>(serverList.size());
        for (Photo serverPhoto: serverList) {
            appPhotosMetadeta.add(new AppPhotoMetaData(serverPhoto));
        }
        return appPhotosMetadeta;
    }
} //DataServer
