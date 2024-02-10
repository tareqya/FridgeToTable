package com.example.fridgetotable.callback;

import com.example.fridgetotable.database.Recipe;

import java.util.ArrayList;

public interface RecipeCallBack {
    void onRecipesFetchComplete(ArrayList<Recipe> recipes);
}
