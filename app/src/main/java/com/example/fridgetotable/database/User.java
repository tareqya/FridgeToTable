package com.example.fridgetotable.database;


import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class User extends FirebaseKey implements Serializable {
    private String name;
    private String email;
    private String imagePath;
    private String imageUrl;
    private ArrayList<Recipe> favorites;

    public User(){
        favorites = new ArrayList<>();
    }

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

    public ArrayList<Recipe> getFavorites() {
        return favorites;
    }

    public User setFavorites(ArrayList<Recipe> favorites) {
        this.favorites = favorites;
        return this;
    }

    public boolean addRecipe(Recipe recipe) {
        for(Recipe r: this.favorites){
            if(r.getName().equals(recipe.getName()))
                return false;
        }

        this.favorites.add(recipe);
        return true;
    }
}
