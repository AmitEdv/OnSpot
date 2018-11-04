package com.example.amit.onspot;

import java.io.InputStream;
import java.util.List;

interface IDataServerPhotosStreamListener {

    void onDataServerPhotosStreamListReceive(List<InputStream> photos);

    void onError(String errorMsg);
}
