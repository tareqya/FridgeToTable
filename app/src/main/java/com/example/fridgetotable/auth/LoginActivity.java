package com.example.fridgetotable.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fridgetotable.R;
import com.example.fridgetotable.callback.AuthCallBack;
import com.example.fridgetotable.home.HomeActivity;
import com.example.fridgetotable.utils.AuthController;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout login_TF_email;
    private TextInputLayout login_TF_password;
    private Button login_BTN_forgetPassword;
    private Button login_BTN_login;
    private Button login_BTN_signup;
    private ProgressBar login_PB_loading;

    private AuthController authController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        initVars();
    }

    private void initVars() {
        authController = new AuthController();
        authController.setAuthCallBack(new AuthCallBack() {
            @Override
            public void onCreateAccountComplete(Task<AuthResult> task) {

            }

            @Override
            public void onLoginComplete(Task<AuthResult> task) {
                login_PB_loading.setVisibility(View.INVISIBLE);
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    String err = task.getException().getMessage().toString();
                    Toast.makeText(LoginActivity.this, err, Toast.LENGTH_SHORT).show();
                }
            }
        });
        login_BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do login
                String email = login_TF_email.getEditText().getText().toString();
                String password = login_TF_password.getEditText().getText().toString();
                authController.loginUser(email, password);
                login_PB_loading.setVisibility(View.VISIBLE);
            }
        });

        login_BTN_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        login_BTN_forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = login_TF_email.getEditText().getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Email is required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                authController.resetPassword(email);
            }
        });

    }

    private void findViews() {
        login_TF_email = findViewById(R.id.login_TF_email);
        login_TF_password = findViewById(R.id.login_TF_password);
        login_BTN_forgetPassword = findViewById(R.id.login_BTN_forgetPassword);
        login_BTN_login = findViewById(R.id.login_BTN_login);
        login_BTN_signup = findViewById(R.id.login_BTN_signup);
        login_PB_loading = findViewById(R.id.login_PB_loading);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AuthController authController1 = new AuthController();

        if(authController1.getCurrentUser() != null ){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }
}