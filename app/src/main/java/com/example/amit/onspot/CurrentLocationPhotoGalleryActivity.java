package com.example.amit.onspot;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.GridView;
import java.io.InputStream;
import java.util.List;

public class CurrentLocationPhotoGalleryActivity extends AppCompatActivity {
    private static final String TAG = "CurrLocation...Activity";

    /*package*/ static final int MAX_NUM_PHOTOS_TO_DISPLAY = 5;
    //TODO - DataServer should be static class, it is a waste to init it every time i want to use it...
    private DataServer mDataServer = new DataServer();
    private ReceivePhotosStreamListener mReceivePhotosStreamListener = new ReceivePhotosStreamListener();
    CurrentLocationGalleryAdapter mPhotoGalleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate() called");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_current_location_photo_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPhotoGalleryAdapter = new CurrentLocationGalleryAdapter(this);
        GridView gridView = (GridView)findViewById(R.id.gv_gallery);
        gridView.setAdapter(mPhotoGalleryAdapter);
    }

    @Override
    protected void onStart() {
        Log.v(TAG, "onStart() called");
        super.onStart();

        List<AppPhotoMetaData> photosMetadata = AppGlobals.getInstance().getCurrentLocationPhotosMetadata();
        AppGlobals.getInstance().setCurrentLocationPhotosMetadata(null);
        Log.d(TAG, "onStart() photosMetadata = " + photosMetadata);

        if(photosMetadata != null) {
            requestPhotosFromServer(photosMetadata);
        }
    }

    private void updateViewWithPhotos(final List<InputStream> photos) {
        mPhotoGalleryAdapter.clearAllItems();
        for(InputStream photo: photos) {
            final Drawable drawable = Drawable.createFromStream(photo, "src");
            mPhotoGalleryAdapter.addItem(drawable);
        }
    }

    private void requestPhotosFromServer(List<AppPhotoMetaData> photosMetadata) {
        mDataServer.getPhotosAsInputStream(photosMetadata, mReceivePhotosStreamListener);
    }

    private class ReceivePhotosStreamListener implements IDataServerPhotosStreamListener {
        private static final String TAG = "PhotosStreamListener";

        @Override
        public void onDataServerPhotosStreamListReceive(List<InputStream> photos) {
            Log.v(TAG, "onDataServerPhotosStreamListReceive() called with " + photos.size() + " photos");
            updateViewWithPhotos(photos);
        }

        @Override
        public void onError(String errorMsg) {
            Log.e(TAG, errorMsg);
        }
    }
} //CurrentLocationPhotoGalleryActivity
