package com.example.fridgetotable.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.fridgetotable.R;
import com.example.fridgetotable.adapter.RecipeAdapter;
import com.example.fridgetotable.callback.RecipeCallBack;
import com.example.fridgetotable.callback.RecipeListener;
import com.example.fridgetotable.database.Ingredient;
import com.example.fridgetotable.database.Recipe;
import com.example.fridgetotable.utils.RecipeController;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class RecipesActivity extends AppCompatActivity {

    private TextInputLayout recipes_TF_search;
    private RecyclerView recipes_RV_recipes;
    private ArrayList<Ingredient> selectedIngredients;
    private RecipeController recipeController;
    private ArrayList<Recipe> allRecipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        Intent intent = getIntent();
        selectedIngredients = (ArrayList<Ingredient> ) intent.getSerializableExtra("selectedIngredients");
        
        findViews();
        initVars();
    }

    private void findViews() {
        recipes_TF_search = findViewById(R.id.recipes_TF_search);
        recipes_RV_recipes = findViewById(R.id.recipes_RV_recipes);

    }

    private void initVars() {
        recipeController = new RecipeController();
        allRecipes = new ArrayList<>();
        recipeController.setRecipeCallBack(new RecipeCallBack() {
            @Override
            public void onRecipesFetchComplete(ArrayList<Recipe> recipes) {
                allRecipes = recipes;
                String[] ingredients = getIngredientsNames();
                RecipeAdapter recipeAdapter = new RecipeAdapter(RecipesActivity.this, recipes);
                recipeAdapter.setRecipeListener(new RecipeListener() {
                    @Override
                    public void onClick(Recipe recipe) {
                        openRecipeScreen(recipe, ingredients);
                    }
                });
                recipes_RV_recipes.setLayoutManager(new GridLayoutManager(RecipesActivity.this, 2));
                recipes_RV_recipes.setHasFixedSize(true);
                recipes_RV_recipes.setItemAnimator(new DefaultItemAnimator());
                recipes_RV_recipes.setAdapter(recipeAdapter);
            }
        });

        recipes_TF_search.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ArrayList<Recipe> recipes = new ArrayList<>();
                if(charSequence.toString().equals("")){
                    recipes = allRecipes;
                }else{
                    for(Recipe recipe: allRecipes){
                        if(recipe.getName().toLowerCase().startsWith(charSequence.toString().toLowerCase())){
                            recipes.add(recipe);
                        }
                    }
                }

                String[] ingredients = getIngredientsNames();
                RecipeAdapter recipeAdapter = new RecipeAdapter(RecipesActivity.this, recipes);
                recipeAdapter.setRecipeListener(new RecipeListener() {
                    @Override
                    public void onClick(Recipe recipe) {
                        openRecipeScreen(recipe, ingredients);
                    }
                });
                recipes_RV_recipes.setLayoutManager(new GridLayoutManager(RecipesActivity.this, 2));
                recipes_RV_recipes.setHasFixedSize(true);
                recipes_RV_recipes.setItemAnimator(new DefaultItemAnimator());
                recipes_RV_recipes.setAdapter(recipeAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        String[] ingredients = getIngredientsNames();
        recipeController.fetchRecipesWithIngredients(ingredients);
    }

    private void openRecipeScreen(Recipe recipe,  String[] ingredients) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("RECIPE", recipe);
        intent.putExtra("INGREDIENTS", ingredients);
        startActivity(intent);
    }

    private String[] getIngredientsNames(){
        String[] ingredients = new String[selectedIngredients.size()];
        for(int i = 0; i < selectedIngredients.size(); i ++){
            ingredients[i] = selectedIngredients.get(i).getName();
        }

        return ingredients;
    }
}