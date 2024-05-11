package com.example.fridgetotable.utils;

import androidx.annotation.Nullable;

import com.example.fridgetotable.callback.ImageDownloadListener;
import com.example.fridgetotable.callback.RecipeCallBack;
import com.example.fridgetotable.database.Ingredient;
import com.example.fridgetotable.database.Recipe;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

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
                AtomicInteger count = new AtomicInteger(value.size() - 1);
                StorageController storageController = new StorageController();
                for(DocumentSnapshot snapshot: value.getDocuments()){
                    Recipe recipe = snapshot.toObject(Recipe.class);
                    recipe.setKey(snapshot.getId());
                    if(recipe.getImagePath() != null){
                        storageController.downloadImageUrl(recipe.getImagePath(), new ImageDownloadListener() {
                            @Override
                            public void onImageUrlDownloadSuccess(String imageUrl) {
                                recipe.setImageUrl(imageUrl);
                                recipes.add(recipe);
                                if(count.getAndDecrement() == 0)
                                    recipeCallBack.onRecipesFetchComplete(recipes);
                            }

                            @Override
                            public void onImageUrlDownloadFailed(String errorMessage) {

                            }
                        });

                    }else {
                        recipes.add(recipe);
                        if(count.getAndDecrement() == 0)
                            recipeCallBack.onRecipesFetchComplete(recipes);
                    }
                }
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
                            storageController.downloadImageUrl(recipe.getImagePath(), new ImageDownloadListener() {
                                @Override
                                public void onImageUrlDownloadSuccess(String imageUrl) {
                                    recipe.setImageUrl(imageUrl);
                                    recipes.add(recipe);
                                    recipeCallBack.onRecipesFetchComplete(recipes);
                                }

                                @Override
                                public void onImageUrlDownloadFailed(String errorMessage) {

                                }
                            });
                            break;
                        }
                    }

                }


            }
        });
    }
}
