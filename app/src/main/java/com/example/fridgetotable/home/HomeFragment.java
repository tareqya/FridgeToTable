package com.example.fridgetotable.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.fridgetotable.R;
import com.example.fridgetotable.adapter.RecipeAdapter;
import com.example.fridgetotable.callback.RecipeCallBack;
import com.example.fridgetotable.callback.RecipeListener;
import com.example.fridgetotable.database.Recipe;
import com.example.fridgetotable.database.User;
import com.example.fridgetotable.utils.RecipeController;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private Context context;
    private TextInputLayout fHome_TF_search;
    private RecyclerView fHome_RV_recipes;
    private RecipeController recipeController;
    private ArrayList<Recipe> allRecipes;
    private CircularProgressIndicator fHome_PB_loading;
    private CheckBox fHome_CB_fav;
    private User currentUser;


    public HomeFragment(Context context) {
        this.context = context;
        recipeController = new RecipeController();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        findViews(root);
        initVars();
        return root;
    }
    public void setUser(User user) {
        this.currentUser = user;
    }
    private void findViews(View root) {
        fHome_TF_search = root.findViewById(R.id.fHome_TF_search);
        fHome_RV_recipes = root.findViewById(R.id.fHome_RV_recipes);
        fHome_PB_loading = root.findViewById(R.id.fHome_PB_loading);
        fHome_CB_fav = root.findViewById(R.id.fHome_CB_fav);
    }

    private void initVars() {
        fHome_PB_loading.setVisibility(View.VISIBLE);
        recipeController.setRecipeCallBack(new RecipeCallBack() {
            @Override
            public void onRecipesFetchComplete(ArrayList<Recipe> recipes) {
                fHome_PB_loading.setVisibility(View.INVISIBLE);
                allRecipes = recipes;
                displayRecycleView(recipes);
            }
        });

        recipeController.fetchAllRecipes();

        fHome_TF_search.getEditText().addTextChangedListener(new TextWatcher() {
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

               displayRecycleView(recipes);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        fHome_CB_fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    ArrayList<Recipe> recipes = new ArrayList<>();
                    ArrayList<Recipe> favRecipes = currentUser.getFavorites();
                    for(Recipe recipe: allRecipes){
                       boolean exist = false;
                       for(Recipe favRecipe : favRecipes){
                           if(recipe.getName().equals(favRecipe.getName())){
                               exist = true;
                               break;
                           }
                       }
                       if(exist)
                           recipes.add(recipe);
                    }

                    displayRecycleView(recipes);

                }else{
                    displayRecycleView(allRecipes);
                }
            }
        });
    }

    private void displayRecycleView(ArrayList<Recipe> recipes) {
        RecipeAdapter recipeAdapter = new RecipeAdapter(context, recipes);
        recipeAdapter.setRecipeListener(new RecipeListener() {
            @Override
            public void onClick(Recipe recipe) {
                openRecipeScreen(recipe);
            }
        });
        fHome_RV_recipes.setLayoutManager(new GridLayoutManager(context, 2));
        fHome_RV_recipes.setHasFixedSize(true);
        fHome_RV_recipes.setItemAnimator(new DefaultItemAnimator());
        fHome_RV_recipes.setAdapter(recipeAdapter);
    }

    private void openRecipeScreen(Recipe recipe) {
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra("RECIPE", recipe);
        startActivity(intent);
    }


}