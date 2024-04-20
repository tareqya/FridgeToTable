package com.example.fridgetotable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fridgetotable.R;
import com.example.fridgetotable.callback.IngredientListener;
import com.example.fridgetotable.database.Ingredient;
import com.example.fridgetotable.database.Recipe;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Ingredient> ingredients;
    private Context context;
    private IngredientListener ingredientListener;

    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredients){
        this.context = context;
        this.ingredients = ingredients;
    }

    public void setIngredientListener(IngredientListener ingredientListener){
        this.ingredientListener = ingredientListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Ingredient ingredient = getItem(position);
        IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;

        Glide.with(context).load(ingredient.getImageUrl()).into(ingredientViewHolder.ingredient_IV_image);
        if(ingredient.isSelected()){
            ingredientViewHolder.ingredient_IV_select.setImageResource(R.drawable.baseline_check_24);
        }else{
            ingredientViewHolder.ingredient_IV_select.setImageResource(R.drawable.baseline_add_circle_outline_24);
        }

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public Ingredient getItem(int pos){
        return this.ingredients.get(pos);
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        public ImageView ingredient_IV_image;
        public ImageView ingredient_IV_select;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredient_IV_select = itemView.findViewById(R.id.ingredient_IV_select);
            ingredient_IV_image = itemView.findViewById(R.id.ingredient_IV_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Ingredient ingredient = getItem(pos);
                    ingredient.setSelected(!ingredient.isSelected());
                    notifyDataSetChanged();
                    if(ingredientListener != null){
                        ingredientListener.onClick(ingredient);
                    }
                }
            });
        }
    }
}
