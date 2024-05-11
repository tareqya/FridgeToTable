package com.example.fridgetotable.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fridgetotable.R;
import com.example.fridgetotable.callback.UserCallBack;
import com.example.fridgetotable.database.Recipe;
import com.example.fridgetotable.database.User;
import com.example.fridgetotable.utils.AuthController;
import com.example.fridgetotable.utils.UserController;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RecipeActivity extends AppCompatActivity {
    private ImageView recipe_IV_image;
    private TextView recipe_TV_name;
    private TextView recipe_TV_calories;
    private TextView recipe_TV_description;
    private TextView recipe_TV_ingredients;
    private TextView recipe_TV_missingIngredients;
    private FloatingActionButton recipe_FAB_AddFav;
    private UserController userController;
    private AuthController authController;
    private String[] selectedIngredients = new String[0];
    private User currentUser;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        recipe = (Recipe) intent.getSerializableExtra("RECIPE");
        if(intent.getSerializableExtra("INGREDIENTS") != null){
            selectedIngredients = (String[]) intent.getSerializableExtra("INGREDIENTS");
        }
        findViews();
        initVars();
        displayUI(recipe);
    }

    private void initVars() {
        authController = new AuthController();
        userController = new UserController();
        userController.setUserCallBack(new UserCallBack() {
            @Override
            public void onSaveUserDataComplete(Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RecipeActivity.this, "Recipe added to favorites", Toast.LENGTH_SHORT).show();
                }else{
                    String err = task.getException().getMessage();
                    Toast.makeText(RecipeActivity.this, err, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFetchUserDataComplete(User user) {
               currentUser = user;
            }
        });

        userController.fetchUserData(authController.getCurrentUser().getUid());
        recipe_FAB_AddFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser.addRecipe(recipe))
                    userController.saveUserData(currentUser);
                else{
                    Toast.makeText(RecipeActivity.this, "This recipe is already in your favorites", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void displayUI(Recipe recipe) {
        Glide.with(this).load(recipe.getImageUrl()).into(recipe_IV_image);
        recipe_TV_name.setText(recipe.getName());
        recipe_TV_calories.setText(recipe.getCalories() + "");
        recipe_TV_description.setText(recipe.getDescription());

        String ingredients = "";

        for(String ingredient : recipe.getIngredients()){
            ingredients += " " + ingredient;
        }
        recipe_TV_ingredients.setText(ingredients);

        if(this.selectedIngredients.length > 0){
            String missingIngredients = getMissingIngredients(recipe);
            recipe_TV_missingIngredients.setText(missingIngredients);
        }else{
            recipe_TV_missingIngredients.setText("");
        }

    }

    private void findViews() {
        recipe_IV_image = findViewById(R.id.recipe_IV_image);
        recipe_TV_name = findViewById(R.id.recipe_TV_name);
        recipe_TV_calories = findViewById(R.id.recipe_TV_calories);
        recipe_TV_description = findViewById(R.id.recipe_TV_description);
        recipe_TV_ingredients = findViewById(R.id.recipe_TV_ingredients);
        recipe_TV_missingIngredients = findViewById(R.id.recipe_TV_missingIngredients);
        recipe_FAB_AddFav = findViewById(R.id.recipe_FAB_AddFav);
    }

    public String getMissingIngredients(Recipe recipe){
        String missingIngredients = "";


        for(String recipeIngredient: recipe.getIngredients()){
            boolean exist = false;
            for(String selectedIngredient: this.selectedIngredients){
                if(recipeIngredient.equalsIgnoreCase(selectedIngredient)){
                    exist = true;
                    break;
                }
            }

            if(!exist){
                missingIngredients += recipeIngredient + ", ";
            }
        }
        return missingIngredients;
    }
}