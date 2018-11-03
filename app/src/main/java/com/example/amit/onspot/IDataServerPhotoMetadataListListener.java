package com.example.amit.onspot;


import java.util.List;

interface IDataServerPhotoMetadataListListener {

    void onDataServerPhotoMetadataListReceive(List<AppPhotoMetaData> photoMetadataList);

    void onError(String errorMsg);
}