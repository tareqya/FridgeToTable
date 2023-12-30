package com.example.fridgetotable.database;

import com.google.firebase.database.Exclude;

public class User extends FirebaseKey {
    private String name;
    private String email;
    private String imagePath;
    private String imageUrl;

    public User(){}

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public User setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    @Exclude
    public String getImageUrl() {
        return imageUrl;
    }

    public User setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public String getImagePath() {
        return imagePath;
    }
}
