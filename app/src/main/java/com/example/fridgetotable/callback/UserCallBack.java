package com.example.fridgetotable.callback;

import com.example.fridgetotable.database.User;
import com.google.android.gms.tasks.Task;

public interface UserCallBack {
    void onSaveUserDataComplete(Task<Void> task);
    void onFetchUserDataComplete(User user);
}
