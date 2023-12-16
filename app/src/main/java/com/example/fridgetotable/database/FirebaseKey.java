package com.example.fridgetotable.database;

public class FirebaseKey {
    protected String key;

    public FirebaseKey(){}

    public FirebaseKey setKey(String key) {
        this.key = key;
        return this;
    }

    public String getKey() {
        return key;
    }

}
