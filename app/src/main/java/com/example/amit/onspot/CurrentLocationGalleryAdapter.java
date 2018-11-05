package com.example.amit.onspot;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/*package*/ class CurrentLocationGalleryAdapter extends BaseAdapter {
    private static final String TAG = "CurrLocation..Adapter";

    private Activity mActivity;
    private ArrayList<Drawable> mPhotos = new ArrayList<>(CurrentLocationPhotoGalleryActivity.MAX_NUM_PHOTOS_TO_DISPLAY);

    /*package*/ CurrentLocationGalleryAdapter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return mPhotos.size();
    }

    @Override
    public Object getItem(int position) {
        return mPhotos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mActivity);
        imageView.setImageDrawable(mPhotos.get(position));
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setLayoutParams(new GridView.LayoutParams(240, 240));

        return imageView;
    }

    /*package*/ void addItem(final Drawable photo) {
        Log.v(TAG, "addItem()");
        if(photo != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPhotos.add(photo);
                    notifyDataSetChanged();
                }
            });
        }
    }

    /*package*/ void removeItem(final Drawable photo) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPhotos.remove(photo);
                notifyDataSetChanged();
            }
        });
    }

    /*package*/ void clearAllItems() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPhotos.clear();
                notifyDataSetChanged();
            }
        });
    }
}// CurrentLocationGalleryAdapter
