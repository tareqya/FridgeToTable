package com.example.fridgetotable.home;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fridgetotable.R;
import com.example.fridgetotable.callback.UserCallBack;
import com.example.fridgetotable.database.User;
import com.example.fridgetotable.utils.Generic;
import com.example.fridgetotable.utils.StorageController;
import com.example.fridgetotable.utils.UserController;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateAccountActivity extends AppCompatActivity {

    private CircleImageView editAccount_IV_image;
    private FloatingActionButton editAccount_FBTN_uploadImage;
    private TextInputLayout editAccount_TF_fullName;
    private Button editAccount_BTN_updateAccount;
    private Uri selectedImageUri;
    private User user;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(ProfileFragment.USER);

        if(!checkPermissions()){
            requestPermissions();
        }

        findViews();
        initVars();
        displayUser(user);

    }

    private void initVars() {
        userController = new UserController();
        userController.setUserCallBack(new UserCallBack() {
            @Override
            public void onSaveUserDataComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdateAccountActivity.this, "account updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    String err = task.getException().getMessage().toString();
                    Toast.makeText(UpdateAccountActivity.this, err, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFetchUserDataComplete(User user) {

            }
        });
        editAccount_BTN_updateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserData();
            }
        });

        editAccount_FBTN_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageSourceDialog();
            }
        });
    }

    private void updateUserData() {
        user.setName(editAccount_TF_fullName.getEditText().getText().toString());
        if(selectedImageUri != null){
            String imagePath = "Users/"+user.getKey()+"."+Generic.getFileExtension(this, selectedImageUri);
            StorageController storageController = new StorageController();
            if(storageController.uploadImage(selectedImageUri, imagePath)){
                user.setImagePath(imagePath);
            }
        }
        userController.saveUserData(user);
    }

    private void findViews() {
        editAccount_IV_image = findViewById(R.id.editAccount_IV_image);
        editAccount_FBTN_uploadImage = findViewById(R.id.editAccount_FBTN_uploadImage);
        editAccount_TF_fullName = findViewById(R.id.editAccount_TF_fullName);
        editAccount_BTN_updateAccount = findViewById(R.id.editAccount_BTN_updateAccount);
    }

    private void displayUser(User user) {
        if(user.getImageUrl() != null){
            Glide.with(this).load(user.getImageUrl()).into(editAccount_IV_image);
        }

        editAccount_TF_fullName.getEditText().setText(user.getName());
    }

    public  boolean checkPermissions() {
        return (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                },
                100
        );
    }
    private void showImageSourceDialog() {
        CharSequence[] items = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateAccountActivity.this);
        builder.setTitle("Choose Image Source");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch (which) {
                    case 0:
                        openCamera();
                        break;
                    case 1:
                        openGallery();
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraResults.launch(intent);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        gallery_results.launch(intent);
    }

    private final ActivityResultLauncher<Intent> gallery_results = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    switch (result.getResultCode()) {
                        case Activity.RESULT_OK:
                            try {
                                Intent intent = result.getData();
                                selectedImageUri = intent.getData();
                                final InputStream imageStream = UpdateAccountActivity.this.getContentResolver().openInputStream(selectedImageUri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                editAccount_IV_image.setImageBitmap(selectedImage);
                            }
                            catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(UpdateAccountActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(UpdateAccountActivity.this, "Gallery canceled", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });

    private final ActivityResultLauncher<Intent> cameraResults = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    switch (result.getResultCode()) {
                        case Activity.RESULT_OK:
                            Intent intent = result.getData();
                            Bitmap bitmap = (Bitmap)  intent.getExtras().get("data");
                            editAccount_IV_image.setImageBitmap(bitmap);
                            selectedImageUri = Generic.getImageUri(UpdateAccountActivity.this, bitmap);
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(UpdateAccountActivity.this, "Camera canceled", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
}