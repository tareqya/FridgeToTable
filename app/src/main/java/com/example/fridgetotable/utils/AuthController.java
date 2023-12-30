package com.example.fridgetotable.utils;

import androidx.annotation.NonNull;

import com.example.fridgetotable.callback.AuthCallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthController {
    private FirebaseAuth mAuth;
    private AuthCallBack authCallBack;

    public AuthController(){
        this.mAuth = FirebaseAuth.getInstance();
    }

    public void setAuthCallBack(AuthCallBack authCallBack){
        this.authCallBack = authCallBack;
    }

    public void createAccount(String email, String password) {
        this.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        authCallBack.onCreateAccountComplete(task);
                    }
                });
    }

    public void loginUser(String email, String password) {
        this.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        authCallBack.onLoginComplete(task);
                    }
                });
    }

    public void resetPassword(String email){
        this.mAuth.sendPasswordResetEmail(email);
    }

    public FirebaseUser getCurrentUser(){
        return this.mAuth.getCurrentUser();
    }

    public void logout(){
        this.mAuth.signOut();
    }
}
