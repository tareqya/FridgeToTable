package com.example.fridgetotable.callback;

import com.google.android.gms.tasks.Task;

public interface UserCallBack {
    void onSaveUserDataComplete(Task<Void> task);
}
