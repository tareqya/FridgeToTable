package com.example.fridgetotable.utils;

import androidx.annotation.NonNull;

import com.example.fridgetotable.callback.UserCallBack;
import com.example.fridgetotable.database.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class UserController {
    public static final String USERS_TABLE = "Users";
    private FirebaseDatabase db;

    private UserCallBack userCallBack;

    public UserController() {
        db = FirebaseDatabase.getInstance();
    }

    public void setUserCallBack(UserCallBack userCallBack) {
        this.userCallBack = userCallBack;
    }

    public void saveUserData(User user){
        this.db.getReference().child(USERS_TABLE).child(user.getKey()).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        userCallBack.onSaveUserDataComplete(task);
                    }
                });
    }
}
