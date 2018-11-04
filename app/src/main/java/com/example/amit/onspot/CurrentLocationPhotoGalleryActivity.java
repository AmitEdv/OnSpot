package com.example.amit.onspot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import java.util.List;

public class CurrentLocationPhotoGalleryActivity extends AppCompatActivity {
    private static final String TAG = "CurrLocation...Activity";

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

        Log.d(TAG, "photosMetadata = " + photosMetadata);
    }
} //CurrentLocationPhotoGalleryActivity
