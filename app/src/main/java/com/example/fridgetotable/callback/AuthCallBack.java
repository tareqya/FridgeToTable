package com.example.fridgetotable.callback;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface AuthCallBack {
    void onCreateAccountComplete(Task<AuthResult> task);
}
