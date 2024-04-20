package com.example.fridgetotable.database;

import com.google.firebase.firestore.Exclude;

public class Ingredient extends FirebaseKey{
    private String name;
    private String imagePath;
    private String imageUrl;
    private boolean selected;

    public String getName() {
        return name;
    }

    public Ingredient setName(String name) {
        this.name = name;
        return this;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Ingredient setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }
    @Exclude
    public String getImageUrl() {
        return imageUrl;
    }

    public Ingredient setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    @Exclude
    public boolean isSelected() {
        return selected;
    }

    public Ingredient setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }
}
