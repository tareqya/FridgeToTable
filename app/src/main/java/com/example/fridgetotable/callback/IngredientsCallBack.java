package com.example.fridgetotable.callback;

import com.example.fridgetotable.database.Ingredient;

import java.util.ArrayList;

public interface IngredientsCallBack {
    void onIngredientsFetchComplete(ArrayList<Ingredient> ingredients);
}
