package com.example.fridgetotable.database;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

public class Recipe extends FirebaseKey{

    private String name;
    private String imagePath;
    private String imageUrl;
    private ArrayList<String> ingredients;
    private int calories;


    public Recipe() {}

    public String getName() {
        return name;
    }

    public Recipe setName(String name) {
        this.name = name;
        return this;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Recipe setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }
    @Exclude
    public String getImageUrl() {
        return imageUrl;
    }

    public Recipe setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public Recipe setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public int getCalories() {
        return calories;
    }

    public Recipe setCalories(int calories) {
        this.calories = calories;
        return this;
    }
}
