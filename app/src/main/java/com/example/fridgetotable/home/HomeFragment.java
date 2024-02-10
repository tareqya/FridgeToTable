package com.example.fridgetotable.home;

import android.content.Context;
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

import com.example.fridgetotable.R;
import com.example.fridgetotable.adapter.RecipeAdapter;
import com.example.fridgetotable.callback.RecipeCallBack;
import com.example.fridgetotable.database.Recipe;
import com.example.fridgetotable.utils.RecipeController;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private Context context;
    private TextInputLayout fHome_TF_search;
    private RecyclerView fHome_RV_recipes;
    private RecipeController recipeController;
    private ArrayList<Recipe> allRecipes;

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

    private void findViews(View root) {
        fHome_TF_search = root.findViewById(R.id.fHome_TF_search);
        fHome_RV_recipes = root.findViewById(R.id.fHome_RV_recipes);
    }

    private void initVars() {
        recipeController.setRecipeCallBack(new RecipeCallBack() {
            @Override
            public void onRecipesFetchComplete(ArrayList<Recipe> recipes) {
                allRecipes = recipes;
                RecipeAdapter recipeAdapter = new RecipeAdapter(context, recipes);
                fHome_RV_recipes.setLayoutManager(new GridLayoutManager(context, 2));
                fHome_RV_recipes.setHasFixedSize(true);
                fHome_RV_recipes.setItemAnimator(new DefaultItemAnimator());
                fHome_RV_recipes.setAdapter(recipeAdapter);
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

                RecipeAdapter recipeAdapter = new RecipeAdapter(context, recipes);
                fHome_RV_recipes.setLayoutManager(new GridLayoutManager(context, 2));
                fHome_RV_recipes.setHasFixedSize(true);
                fHome_RV_recipes.setItemAnimator(new DefaultItemAnimator());
                fHome_RV_recipes.setAdapter(recipeAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}