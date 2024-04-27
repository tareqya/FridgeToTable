package com.example.fridgetotable.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.fridgetotable.R;
import com.example.fridgetotable.adapter.IngredientAdapter;
import com.example.fridgetotable.callback.IngredientListener;
import com.example.fridgetotable.callback.IngredientsCallBack;
import com.example.fridgetotable.database.Ingredient;
import com.example.fridgetotable.utils.IngredientsController;

import java.util.ArrayList;

public class CookingFragment extends Fragment {
    private Button fCooking_BTN_search;
    private RecyclerView fCooking_RV_ingredients;
    private Context context;
    private IngredientsController ingredientsController;
    private ArrayList<Ingredient> selectedIngredients;

    public CookingFragment(Context context) {
        // Required empty public constructor
        this.context = context;
        selectedIngredients = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cooking, container, false);
        findViews(view);
        initVars();
        return view;
    }

    private void findViews(View view) {
        fCooking_BTN_search = view.findViewById(R.id.fCooking_BTN_search);
        fCooking_RV_ingredients = view.findViewById(R.id.fCooking_RV_ingredients);
    }

    private void initVars() {
        ingredientsController = new IngredientsController();
        ingredientsController.setIngredientsCallBack(new IngredientsCallBack() {
            @Override
            public void onIngredientsFetchComplete(ArrayList<Ingredient> ingredients) {
                IngredientAdapter ingredientAdapter = new IngredientAdapter(context, ingredients);
                ingredientAdapter.setIngredientListener(new IngredientListener() {
                    @Override
                    public void onClick(Ingredient ingredient) {
                        if(!ingredient.isSelected()){
                            for(int i = 0; i < selectedIngredients.size(); i++){
                                if(ingredient.getKey().equals(selectedIngredients.get(i).getKey())){
                                    selectedIngredients.remove(i);
                                    break;
                                }
                            }
                        }else{
                            selectedIngredients.add(ingredient);
                        }

                    }
                });


                fCooking_RV_ingredients.setLayoutManager(new GridLayoutManager(context, 3));
                fCooking_RV_ingredients.setHasFixedSize(true);
                fCooking_RV_ingredients.setItemAnimator(new DefaultItemAnimator());
                fCooking_RV_ingredients.setAdapter(ingredientAdapter);

            }
        });

        ingredientsController.fetchIngredients();

        fCooking_BTN_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedIngredients.size() == 0){
                    Toast.makeText(context, "You must select an ingredients", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent((Activity) context, RecipesActivity.class );
                intent.putExtra("selectedIngredients", selectedIngredients);
                startActivity(intent);
            }
        });
    }
}