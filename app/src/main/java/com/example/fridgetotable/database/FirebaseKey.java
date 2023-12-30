package com.example.fridgetotable.database;

import com.google.firebase.database.Exclude;

public class FirebaseKey {
    protected String key;

    public FirebaseKey(){}

    public FirebaseKey setKey(String key) {
        this.key = key;
        return this;
    }

    @Exclude
    public String getKey() {
        return key;
    }

}
