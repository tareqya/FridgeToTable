package com.example.fridgetotable.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fridgetotable.R;
import com.example.fridgetotable.auth.LoginActivity;
import com.example.fridgetotable.database.User;
import com.example.fridgetotable.utils.AuthController;
import com.example.fridgetotable.utils.StorageController;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private Activity activity;
    private CircleImageView fProfile_IV_profileImage;
    private TextView fProfile_TV_name;
    private CardView fProfile_CV_editDetails;
    private CardView fProfile_CV_logout;
    private StorageController storageController;
    private AuthController authController;
    private User user;

    public ProfileFragment(Activity activity) {
        this.activity = activity;
        storageController = new StorageController();
        authController = new AuthController();
    }

    public void setUser(User user){
        this.user = user;
        displayUserData();
    }

    private void displayUserData() {
        fProfile_TV_name.setText(user.getName());
        if(user.getImagePath() != null){
            // download image url
            String imageUrl = storageController.downloadImageUrl(user.getImagePath());
            // set image to user profile image
            Glide.with(activity).load(imageUrl).into(fProfile_IV_profileImage);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        findViews(view);
        initVars();
        return view;
    }

    private void initVars() {
        fProfile_CV_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do logout
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                authController.logout();
            }
        });

        fProfile_CV_editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to edit account screen
                Intent intent = new Intent(activity, UpdateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findViews(View view) {
        fProfile_IV_profileImage = view.findViewById(R.id.fProfile_IV_profileImage);
        fProfile_TV_name = view.findViewById(R.id.fProfile_TV_name);
        fProfile_CV_editDetails = view.findViewById(R.id.fProfile_CV_editDetails);
        fProfile_CV_logout = view.findViewById(R.id.fProfile_CV_logout);
    }
}