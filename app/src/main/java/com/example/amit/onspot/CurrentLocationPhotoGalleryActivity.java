package com.example.amit.onspot;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class CurrentLocationPhotoGalleryActivity extends AppCompatActivity {
    private static final String TAG = "CurrLocation...Activity";

    TextView mDownloadProgressTitle;

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

        mDownloadProgressTitle = (TextView)findViewById(R.id.tv_download_progress_title);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_option_settings:
                startSettingsActivity();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void updateViewWithPhotos(final List<InputStream> photos) {
        //TODO - use data binding
        mPhotoGalleryAdapter.clearAllItems();
        final int total = photos.size();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDownloadProgressTitle.setText(getString(R.string.download_title, total));
                mDownloadProgressTitle.setVisibility(View.VISIBLE);
            }
        });

        for(InputStream photo: photos) {
            final Drawable drawable = Drawable.createFromStream(photo, "src");
            mPhotoGalleryAdapter.addItem(drawable);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDownloadProgressTitle.setVisibility(View.INVISIBLE);
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
