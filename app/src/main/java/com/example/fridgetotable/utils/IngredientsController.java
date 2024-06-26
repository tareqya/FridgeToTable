package com.example.fridgetotable.utils;

import androidx.annotation.Nullable;

import com.example.fridgetotable.callback.ImageDownloadListener;
import com.example.fridgetotable.callback.IngredientsCallBack;
import com.example.fridgetotable.callback.RecipeCallBack;
import com.example.fridgetotable.database.Ingredient;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class IngredientsController {

    public static final String Ingredients_TABLE = "Ingredients";
    private FirebaseFirestore db;
    private IngredientsCallBack ingredientsCallBack;

    public IngredientsController(){
        this.db = FirebaseFirestore.getInstance();
    }

    public void setIngredientsCallBack(IngredientsCallBack ingredientsCallBack){
        this.ingredientsCallBack = ingredientsCallBack;
    }

    public void fetchIngredients(){
        this.db.collection(Ingredients_TABLE).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value == null) return;

                StorageController storageController = new StorageController();
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                AtomicInteger count = new AtomicInteger(value.size() - 1);
                for(DocumentSnapshot snapshot: value.getDocuments()){
                    Ingredient ingredient = snapshot.toObject(Ingredient.class);
                    ingredient.setKey(snapshot.getId());
                    if(ingredient.getImagePath() != null){

                        storageController.downloadImageUrl(ingredient.getImagePath(), new ImageDownloadListener() {
                            @Override
                            public void onImageUrlDownloadSuccess(String imageUrl) {
                                ingredient.setImageUrl(imageUrl);
                                ingredients.add(ingredient);
                                if(count.getAndDecrement() == 0)
                                    ingredientsCallBack.onIngredientsFetchComplete(ingredients);
                            }

                            @Override
                            public void onImageUrlDownloadFailed(String errorMessage) {

                            }
                        });

                    }else{
                        ingredients.add(ingredient);
                        if(count.getAndDecrement() == 0)
                            ingredientsCallBack.onIngredientsFetchComplete(ingredients);
                    }


                }



            }
        });
    }
}
