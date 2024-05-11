package com.example.fridgetotable.database;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe extends FirebaseKey implements Serializable {

    private String name;
    private String imagePath;
    private String imageUrl;
    private ArrayList<String> ingredients;
    private int calories;
    private String description;


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

    public String getDescription(){
        return this.description;
    }

    public Recipe setDescription(String description){
        this.description = description;
        return this;
    }
}
