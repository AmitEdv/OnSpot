package com.example.amit.onspot;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import java.io.InputStream;
import java.util.List;

public class CurrentLocationPhotoGalleryActivity extends AppCompatActivity {
    private static final String TAG = "CurrLocation...Activity";

    //TODO - DataServer should be static class, it is a waste to init it every time i want to use it...
    private DataServer mDataServer = new DataServer();
    private ReceivePhotosStreamListener mReceivePhotosStreamListener = new ReceivePhotosStreamListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate() called");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_current_location_photo_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        Log.v(TAG, "onStart() called");
        super.onStart();

        List<AppPhotoMetaData> photosMetadata = AppGlobals.getInstance().getCurrentLocationPhotosMetadata();
        AppGlobals.getInstance().setCurrentLocationPhotosMetadata(null);
        Log.d(TAG, "onStart() photosMetadata = " + photosMetadata);
        requestPhotosFromServer(photosMetadata);
    }

    private void updateViewWithPhotos(final List<InputStream> photos) {
        final Drawable drawable1 = Drawable.createFromStream(photos.get(0), "src");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                ImageView imageView1 = (ImageView)findViewById(R.id.img1);
                imageView1.setImageDrawable(drawable1);
            }
        });
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
