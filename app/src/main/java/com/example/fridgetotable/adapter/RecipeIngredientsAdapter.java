package com.example.fridgetotable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fridgetotable.R;
import com.example.fridgetotable.database.Ingredient;
import com.example.fridgetotable.database.Recipe;

import java.util.ArrayList;

public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Recipe> recipes;
    private Context context;
    private String[] selectedIngredients;

    public RecipeIngredientsAdapter(ArrayList<Recipe> recipes, Context context, String[] selectedIngredients){
        this.selectedIngredients = selectedIngredients;
        this.recipes = recipes;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_ingredents_item, parent, false);
        return new RecipeIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Recipe recipe = getItem(position);
        RecipeIngredientViewHolder recipeIngredientViewHolder = (RecipeIngredientViewHolder) holder;

        recipeIngredientViewHolder.RI_TV_calories.setText(recipe.getCalories() + "");
        recipeIngredientViewHolder.RI_TV_name.setText(recipe.getName());
        Glide.with(context).load(recipe.getImageUrl()).into(recipeIngredientViewHolder.RI_IV_image);

        String missingIngredients = "";


        for(String recipeIngredient: recipe.getIngredients()){
            boolean exist = false;
            for(String selectedIngredient: selectedIngredients){
                if(recipeIngredient.equalsIgnoreCase(selectedIngredient)){
                    exist = true;
                    break;
                }
            }

            if(!exist){
                missingIngredients += recipeIngredient + ", ";
            }
        }

        recipeIngredientViewHolder.RI_TV_missingIngredients.setText(missingIngredients);
    }

    private Recipe getItem(int position) {
        return this.recipes.get(position);
    }

    @Override
    public int getItemCount() {
        return this.recipes.size();
    }

    public class RecipeIngredientViewHolder extends RecyclerView.ViewHolder{
        public ImageView RI_IV_image;
        public TextView RI_TV_name;
        public TextView RI_TV_calories;
        public TextView RI_TV_missingIngredients;
        public RecipeIngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            RI_TV_missingIngredients = itemView.findViewById(R.id.RI_TV_missingIngredients);
            RI_IV_image = itemView.findViewById(R.id.RI_IV_image);
            RI_TV_name = itemView.findViewById(R.id.RI_TV_name);
            RI_TV_calories = itemView.findViewById(R.id.RI_TV_calories);

        }

    }
}
