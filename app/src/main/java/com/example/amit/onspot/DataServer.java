package com.example.amit.onspot;

import android.util.Log;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/*package*/ class DataServer {
    private static final String TAG = "DataServer";

    private static final String API_KEY = "1cbf1754d8b2593e864bce045369a1e9";
    private static final String API_SECRET = "0069656dd0520e6b";
    private static final int PHOTOS_SEARCH_PARAM_LOCATION_RADIUS_KM = 10;
    private static final int PHOTOS_SEARCH_PARAM_NUM_OF_PAGE = 1;

    private REST mRest = new REST();
    private Flickr mFlickrClient = null;

    /*package*/ DataServer() {
        try {
            mFlickrClient = new Flickr(API_KEY, API_SECRET, mRest);
        } catch (Exception ex) {
            //TODO- mark mFlickrClient as @Nullable and handle case of null
            Log.d(TAG, "getPhotosAroundALocation() ERROR creating flickr client");
            Log.d(TAG, ex.getLocalizedMessage());
        }
    }

    /*package*/ void getPhotosAroundALocation(double longitude, double latitude, final int maxNumOfPhotos, final IDataServerPhotoMetadataListListener listener) {
        Log.d(TAG, "getPhotosAroundALocation() called with latitude= " + latitude + "longitude = " + longitude);
        final String latitudeStr = String.valueOf(latitude);
        final String longitudeStr = String.valueOf(longitude);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //TODO- separate to private methods: one that sets the search params, and another that searches (for reuse purposes)
                    SearchParameters searchParameters = new SearchParameters();
                    searchParameters.setLatitude(latitudeStr);
                    searchParameters.setLongitude(longitudeStr);
                    searchParameters.setRadius(PHOTOS_SEARCH_PARAM_LOCATION_RADIUS_KM);

                    PhotoList photos = mFlickrClient.getPhotosInterface().search(searchParameters,
                            maxNumOfPhotos, PHOTOS_SEARCH_PARAM_NUM_OF_PAGE);
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

    //TODO - get AppSize as param and convert to Flickr library's Size
    /*package*/ void getPhotosAsInputStream(final List<AppPhotoMetaData>  appPhotoMetaDataList, final IDataServerPhotosStreamListener listener) {
        final List<InputStream> photos = new ArrayList<>(appPhotoMetaDataList.size());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    for(AppPhotoMetaData photoMetadata: appPhotoMetaDataList) {
                        Photo serverPhoto = photoMetadata.getServerPhotoForDataServerUseOnly();
                        InputStream  photoInputStream = mFlickrClient.getPhotosInterface().getImageAsStream(serverPhoto, Size.LARGE);
                        photos.add(photoInputStream);
                    }

                    listener.onDataServerPhotosStreamListReceive(photos);
                } catch (Exception ex) {
                    Log.d(TAG, "getPhotosAsInputStream() ERROR requesting photos");
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
