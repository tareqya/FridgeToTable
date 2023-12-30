package com.example.fridgetotable.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fridgetotable.callback.AuthCallBack;
import com.example.fridgetotable.R;
import com.example.fridgetotable.callback.UserCallBack;
import com.example.fridgetotable.database.User;
import com.example.fridgetotable.utils.AuthController;
import com.example.fridgetotable.utils.UserController;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;

public class SignupActivity extends AppCompatActivity {
    private TextInputLayout signup_TF_email;
    private TextInputLayout signup_TF_password;
    private TextInputLayout signup_TF_confirmPassword;
    private TextInputLayout signup_TF_fullName;
    private Button signup_BTN_signup;
    private ProgressBar signup_PB_loading;

    private AuthController authController;
    private UserController userController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        findViews();
        initVars();
    }

    private void findViews() {
        signup_TF_email = findViewById(R.id.signup_TF_email);
        signup_TF_password = findViewById(R.id.signup_TF_password);
        signup_TF_confirmPassword = findViewById(R.id.signup_TF_confirmPassword);
        signup_BTN_signup = findViewById(R.id.signup_BTN_signup);
        signup_PB_loading = findViewById(R.id.signup_PB_loading);
        signup_TF_fullName = findViewById(R.id.signup_TF_fullName);
    }

    private void initVars() {
        authController = new AuthController();
        userController = new UserController();
        authController.setAuthCallBack(new AuthCallBack() {
            @Override
            public void onCreateAccountComplete(Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // save user data in database
                    String email = signup_TF_email.getEditText().getText().toString();
                    String fullName = signup_TF_fullName.getEditText().getText().toString();
                    String uid = authController.getCurrentUser().getUid();
                    User user = new User()
                            .setEmail(email)
                            .setName(fullName);
                    user.setKey(uid);
                    userController.saveUserData(user);
                }else {
                    // show error msg
                    signup_PB_loading.setVisibility(View.INVISIBLE);
                    String error = task.getException().getMessage().toString();
                    Toast.makeText(SignupActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLoginComplete(Task<AuthResult> task) {

            }
        });


        userController.setUserCallBack(new UserCallBack() {
            @Override
            public void onSaveUserDataComplete(Task<Void> task) {
                signup_PB_loading.setVisibility(View.INVISIBLE);
                if(task.isSuccessful()){
                    Toast.makeText(SignupActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                    authController.logout();
                    finish();
                }else{
                    String error = task.getException().getMessage().toString();
                    Toast.makeText(SignupActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });


        signup_BTN_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check inputs
                if(!checkInput()){
                    return;
                }
                // create account
                String email = signup_TF_email.getEditText().getText().toString();
                String password = signup_TF_password.getEditText().getText().toString();
                signup_PB_loading.setVisibility(View.VISIBLE);
                authController.createAccount(email, password);
            }
        });
    }

    private boolean checkInput(){
        String email = signup_TF_email.getEditText().getText().toString();
        String password = signup_TF_password.getEditText().getText().toString();
        String confirmPassword = signup_TF_confirmPassword.getEditText().getText().toString();
        String fullName = signup_TF_fullName.getEditText().getText().toString();

        if(email.isEmpty()){
            Toast.makeText(SignupActivity.this, "email is required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(fullName.isEmpty()){
            Toast.makeText(SignupActivity.this, "name is required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password.isEmpty()){
            Toast.makeText(SignupActivity.this, "password is required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!password.equals(confirmPassword)){
            Toast.makeText(SignupActivity.this, "password and confirm password are mismatch", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}