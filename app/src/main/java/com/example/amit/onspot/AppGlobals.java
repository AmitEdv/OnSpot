package com.example.amit.onspot;


import java.util.List;

/*package*/ class AppGlobals {
    private static AppGlobals ourInstance = new AppGlobals();

    //TODO - check if should be WeakReference
    private List<AppPhotoMetaData> mCurrentLocationPhotosMetadataList;

    /*package*/ static AppGlobals getInstance() {
        return ourInstance;
    }

    private AppGlobals() {}

    /*package*/ void setCurrentLocationPhotosMetadata(List<AppPhotoMetaData> metadata) {
        mCurrentLocationPhotosMetadataList = metadata;
    }

    /*package*/ List<AppPhotoMetaData> getCurrentLocationPhotosMetadata() {
        return mCurrentLocationPhotosMetadataList;
    }
}
