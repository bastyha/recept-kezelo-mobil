package com.recept_kezelo_mobil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.recept_kezelo_mobil.adapters.OwnRecipeAdapter;
import com.recept_kezelo_mobil.models.Recipe;
import com.recept_kezelo_mobil.serverhandlers.RecipeHandler;

import java.util.ArrayList;

public class OwnRecipeActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            getMenuInflater().inflate(R.menu.signedout_menu, menu);
        }else {
            getMenuInflater().inflate(R.menu.signedin_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId==R.id.login){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if (itemId == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if(itemId == R.id.home){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (itemId == R.id.newrecipe) {
            startActivity(new Intent(this, NewRecipeActivity.class));

        } else if (itemId == R.id.ownrecipe) {
            startActivity(new Intent(this, OwnRecipeActivity.class));

        }
        return true;
    }


    private ArrayList<Recipe> recipesList=new ArrayList<>();
    private RecyclerView recipes;
    private TextView norecipes;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ownrecipe);

        recipes = findViewById(R.id.recipes);
        norecipes = findViewById(R.id.norecipe);
        RecipeHandler.readByLoggedInUser()
                .addOnCompleteListener(recipeTask->{
                   if (recipeTask.isSuccessful()){
                       recipesList= (ArrayList<Recipe>) recipeTask.getResult().toObjects(Recipe.class);
                       Log.d("OwnRec", String.valueOf(recipesList.size()));
                       if (recipesList.size()==0){
                           norecipes.setVisibility(View.VISIBLE);
                           recipes.setVisibility(View.GONE);
                       }else {
                           norecipes.setVisibility(View.GONE);
                           recipes.setVisibility(View.VISIBLE);
                           OwnRecipeAdapter ownRecipeAdapter  = new OwnRecipeAdapter(this, recipesList);
                           recipes.setAdapter(ownRecipeAdapter);
                           recipes.setLayoutManager(new LinearLayoutManager(this));
                       }
                   }else{
                       recipeTask.getException().printStackTrace();
                   }
                });



    }
}
