package com.example.fridgetotable.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fridgetotable.callback.UserCallBack;
import com.example.fridgetotable.database.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserController {
    public static final String USERS_TABLE = "Users";
    private FirebaseFirestore db;

    private UserCallBack userCallBack;

    public UserController() {
        db = FirebaseFirestore.getInstance();
    }

    public void setUserCallBack(UserCallBack userCallBack) {
        this.userCallBack = userCallBack;
    }

    public void saveUserData(User user){
        this.db.collection(USERS_TABLE).document(user.getKey()).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        userCallBack.onSaveUserDataComplete(task);
                    }
                });
    }

    public void fetchUserData(String uid){
        this.db.collection(USERS_TABLE).document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                User user = value.toObject(User.class);
                user.setKey(uid);
                userCallBack.onFetchUserDataComplete(user);
            }
        });
    }
}
