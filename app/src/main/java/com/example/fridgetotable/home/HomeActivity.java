package com.example.fridgetotable.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.fridgetotable.R;
import com.example.fridgetotable.callback.UserCallBack;
import com.example.fridgetotable.database.User;
import com.example.fridgetotable.utils.AuthController;
import com.example.fridgetotable.utils.UserController;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private FrameLayout home_FL_home;
    private FrameLayout home_FL_cooking;
    private FrameLayout home_FL_profile;
    private BottomNavigationView home_BN;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private CookingFragment cookingFragment;
    private AuthController authController;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViews();
        initVars();
        fetchUser();
    }

    private void fetchUser() {
        authController = new AuthController();
        userController = new UserController();
        userController.setUserCallBack(new UserCallBack() {
            @Override
            public void onSaveUserDataComplete(Task<Void> task) {

            }

            @Override
            public void onFetchUserDataComplete(User user) {
                profileFragment.setUser(user);
            }
        });

        String uid = authController.getCurrentUser().getUid();
        userController.fetchUserData(uid);
    }

    private void findViews() {
        home_BN = findViewById(R.id.home_BN);
        home_FL_home = findViewById(R.id.home_FL_home);
        home_FL_cooking = findViewById(R.id.home_FL_cooking);
        home_FL_profile = findViewById(R.id.home_FL_profile);
    }

    private void initVars() {
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.home_FL_home, homeFragment).commit();

        profileFragment = new ProfileFragment(this);
        getSupportFragmentManager().beginTransaction().add(R.id.home_FL_profile, profileFragment).commit();

        cookingFragment = new CookingFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.home_FL_cooking, cookingFragment).commit();

        home_FL_home.setVisibility(View.VISIBLE);
        home_FL_cooking.setVisibility(View.INVISIBLE);
        home_FL_profile.setVisibility(View.INVISIBLE);
        home_BN.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home){
                    // on the home screen
                    home_FL_home.setVisibility(View.VISIBLE);
                    home_FL_cooking.setVisibility(View.INVISIBLE);
                    home_FL_profile.setVisibility(View.INVISIBLE);
                }else if(item.getItemId() == R.id.profile){
                    // on the profile screen
                    home_FL_home.setVisibility(View.INVISIBLE);
                    home_FL_cooking.setVisibility(View.INVISIBLE);
                    home_FL_profile.setVisibility(View.VISIBLE);
                }else{
                    // on cocking screen
                    home_FL_home.setVisibility(View.INVISIBLE);
                    home_FL_cooking.setVisibility(View.VISIBLE);
                    home_FL_profile.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });
    }
}