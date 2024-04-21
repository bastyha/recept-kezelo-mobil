package com.recept_kezelo_mobil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.recept_kezelo_mobil.adapters.MainRecipeAdapter;
import com.recept_kezelo_mobil.models.Recipe;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recipesRV;
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
            finish();
        } else if (itemId == R.id.ownrecipe) {
            startActivity(new Intent(this, OwnRecipeActivity.class));
            finish();
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recipesRV = findViewById(R.id.recipes);
        ArrayList<Recipe> recipesList = new ArrayList<>();

        FirebaseFirestore.getInstance()
                .collection("Recipes")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot documentSnapshot: task.getResult()){

                           recipesList.add(documentSnapshot.toObject(Recipe.class));
                        }
                        MainRecipeAdapter adapter = new MainRecipeAdapter(this, recipesList);
                        recipesRV.setAdapter(adapter);
                        recipesRV.setLayoutManager(new LinearLayoutManager(this));

                    }else {
                        Log.e("RecipeAdd", "onCreate: ",task.getException() );
                    }
                });



    }
}