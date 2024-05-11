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
import com.example.fridgetotable.callback.RecipeListener;
import com.example.fridgetotable.database.Recipe;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Recipe> recipes;
    private Context context;
    private RecipeListener recipeListener;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes){
        this.context = context;
        this.recipes = recipes;
    }

    public void  setRecipeListener(RecipeListener recipeListener){
        this.recipeListener = recipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Recipe recipe = getItem(position);
        RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;

        recipeViewHolder.recipe_TV_calories.setText(recipe.getCalories() + "");
        recipeViewHolder.recipe_TV_name.setText(recipe.getName());
        Glide.with(context).load(recipe.getImageUrl()).into(recipeViewHolder.recipe_IV_image);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public Recipe getItem(int pos){
        return this.recipes.get(pos);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        public ImageView recipe_IV_image;
        public TextView recipe_TV_name;
        public TextView recipe_TV_calories;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipe_IV_image = itemView.findViewById(R.id.recipe_IV_image);
            recipe_TV_name = itemView.findViewById(R.id.recipe_TV_name);
            recipe_TV_calories = itemView.findViewById(R.id.recipe_TV_calories);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Recipe recipe = getItem(pos);

                    recipeListener.onClick(recipe);
                }
            });
        }
    }
}
