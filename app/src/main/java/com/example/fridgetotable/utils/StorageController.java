package com.example.fridgetotable.utils;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.fridgetotable.callback.ImageDownloadListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

public class StorageController {
    private FirebaseStorage storage ;
    public StorageController(){
        this.storage = FirebaseStorage.getInstance();
    }

    public void downloadImageUrl(String imagePath, ImageDownloadListener imageDownloadListener){
        storage.getReference().child(imagePath).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageDownloadListener.onImageUrlDownloadSuccess(uri.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        imageDownloadListener.onImageUrlDownloadFailed(e.getMessage());
                    }
                });

    }

    public boolean uploadImage(Uri imageUri, String imagePath){
        try{
            UploadTask uploadTask = storage.getReference(imagePath).putFile(imageUri);
            while (!uploadTask.isComplete() && !uploadTask.isCanceled());
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage().toString());
            return false;
        }

    }
}
