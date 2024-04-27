package com.example.fridgetotable.utils;

import androidx.annotation.Nullable;

import com.example.fridgetotable.callback.RecipeCallBack;
import com.example.fridgetotable.database.Ingredient;
import com.example.fridgetotable.database.Recipe;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecipeController {
    public static final String RECIPES_TABLE = "Recipes";
    private FirebaseFirestore db;
    private RecipeCallBack recipeCallBack;

    public RecipeController(){
        this.db = FirebaseFirestore.getInstance();
    }

    public void setRecipeCallBack(RecipeCallBack recipeCallBack){
        this.recipeCallBack = recipeCallBack;
    }

    public void fetchAllRecipes(){
        this.db.collection(RECIPES_TABLE).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value == null) return;
                ArrayList<Recipe> recipes = new ArrayList<>();

                StorageController storageController = new StorageController();
                for(DocumentSnapshot snapshot: value.getDocuments()){
                    Recipe recipe = snapshot.toObject(Recipe.class);
                    recipe.setKey(snapshot.getId());
                    if(recipe.getImagePath() != null){
                        String imageUrl = storageController.downloadImageUrl(recipe.getImagePath());
                        recipe.setImageUrl(imageUrl);
                    }

                    recipes.add(recipe);
                }

                recipeCallBack.onRecipesFetchComplete(recipes);
            }
        });
    }

    public void fetchRecipesWithIngredients(String[] ingredients){
        this.db.collection(RECIPES_TABLE).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value == null) return;
                ArrayList<Recipe> recipes = new ArrayList<>();
                StorageController storageController = new StorageController();
                for(DocumentSnapshot snapshot: value.getDocuments()){
                    Recipe recipe = snapshot.toObject(Recipe.class);

                    for(String ingredient: ingredients){
                        if(recipe.getIngredients().contains(ingredient.toLowerCase())){
                            String imageUrl = storageController.downloadImageUrl(recipe.getImagePath());
                            recipe.setImageUrl(imageUrl);
                            recipes.add(recipe);

                            break;
                        }
                    }

                }

                recipeCallBack.onRecipesFetchComplete(recipes);
            }
        });
    }
}
