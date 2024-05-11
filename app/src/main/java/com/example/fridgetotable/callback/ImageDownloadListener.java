package com.example.fridgetotable.callback;

public interface ImageDownloadListener {
    void onImageUrlDownloadSuccess(String imageUrl);
    void onImageUrlDownloadFailed(String errorMessage);
}
