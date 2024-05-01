package com.recept_kezelo_mobil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.autofill.AutofillValue;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.recept_kezelo_mobil.adapters.AddIngredientAdapter;
import com.recept_kezelo_mobil.adapters.AddStepAdapter;
import com.recept_kezelo_mobil.models.Ingredient;
import com.recept_kezelo_mobil.models.Picture;
import com.recept_kezelo_mobil.models.Recipe;
import com.recept_kezelo_mobil.models.Step;
import com.recept_kezelo_mobil.serverhandlers.ServerUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewRecipeActivity extends AppCompatActivity {
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

    TextInputEditText recipeName;
    TimePicker timeInMinutes;
    RecyclerView ingredients;
    RecyclerView steps;
    ImageView pictureToBeUploaded;
    Uri picUri;

    ServerUtil mSU;

    ActivityResultLauncher<PickVisualMediaRequest> pickPic =
            registerForActivityResult(new PickCorrectPic(), uri ->{
                if(uri!=null){
                    Picasso.get().load((Uri) uri).into(pictureToBeUploaded);
                }
                picUri = uri;
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newrecipe);
        mSU = new ServerUtil();

        recipeName = findViewById(R.id.recipeName);
        timeInMinutes = findViewById(R.id.timeInMinutes);
        timeInMinutes.setIs24HourView(true);
        timeInMinutes.setHour(1);
        timeInMinutes.setMinute(0);

        ingredients = findViewById(R.id.ingredients);

        AddIngredientAdapter addIngredientAdapter = new AddIngredientAdapter(this,new ArrayList<>());
        ingredients.setAdapter(addIngredientAdapter);
        ingredients.setLayoutManager(new LinearLayoutManager(this));

        steps = findViewById(R.id.steps);
        AddStepAdapter addStepAdapter = new AddStepAdapter(new ArrayList<>());
        steps.setAdapter(addStepAdapter);
        steps.setLayoutManager(new LinearLayoutManager(this));


        pictureToBeUploaded = findViewById(R.id.pictureToBeUploaded);

    }
    public void addIngredient(View v){
        ingredients.getRecycledViewPool().clear();
        ((AddIngredientAdapter) ingredients.getAdapter()).addItem(null);
    }

    public void addStep(View v){
        steps.getRecycledViewPool().clear();
        ((AddStepAdapter) steps.getAdapter()).addItem(null);
    }
    public void selectPic(View v){
        pickPic.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }


    public void submit(View v){
        String name = recipeName.getText().toString();
        int time = timeInMinutes.getHour()*60+timeInMinutes.getMinute();
        ArrayList<Ingredient> ingredients1 = ((AddIngredientAdapter) ingredients.getAdapter()).getModels();
        ArrayList<Step> steps1 = ((AddStepAdapter) steps.getAdapter()).getModels();

        if( name.trim().equals("")){
            Toast.makeText(this, "Hiányzó receptnév", Toast.LENGTH_SHORT).show();
            return;
        }
        if(time==0){
            Toast.makeText(this, "Hiányzó receptnév", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ingredients1.size()==0){
            Toast.makeText(this, "1 hozzávaló kötelező", Toast.LENGTH_SHORT).show();
            return;
        }
        if (steps1.size()==0){
            Toast.makeText(this, "1 lépés kötelező", Toast.LENGTH_SHORT).show();
            return;
        }
        for(Ingredient item: ingredients1){
            Log.d("Ingredients", String.format("%f %s %s", item.getAmount(), item.getUnit(), item.getNameOfIngredient()));
            if(item.getAmount()<0){
                Toast.makeText(this, "Mennyiség ne legyen kisebb, mint 0!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(item.getUnit()==null|| item.getUnit().trim().equals("")) {
                Toast.makeText(this, "Mértékegység ne legyen üres", Toast.LENGTH_SHORT).show();
                return;
            }
            if(item.getNameOfIngredient()==null|| item.getNameOfIngredient().trim().equals("")){
                Toast.makeText(this, "Hozzávaló név ne legyen üres", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        for(Step item: steps1){
            Log.d("Step", String.format("%s", item.getStepDescription()));
            if(item.getStepDescription()==null|| item.getStepDescription().trim().equals("")){
                Toast.makeText(this, "Lépés leírása ne legyen üres", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        FirebaseFirestore FFst = FirebaseFirestore.getInstance();
        FirebaseStorage FS  = FirebaseStorage.getInstance();
        Recipe toDB = new Recipe();

        toDB.setId(FFst.collection("Recipes").document().getId());
        toDB.setName(name);
        toDB.setTimeInMinutes(time);
        toDB.setOwner(FirebaseAuth.getInstance().getCurrentUser().getUid());
        toDB.setIngredients(ingredients1);
        toDB.setSteps(steps1);
        if(picUri!=null){
            Picture picture = new Picture();
            picture.setId(FFst.collection("Images").document().getId());
            picture.setUploader(FirebaseAuth.getInstance().getCurrentUser().getUid());
            picture.setExtension(mSU.getMimeType(this, picUri));
            toDB.setImage_id(picture.getId());
            FFst.collection("Images")
                    .document(picture.getId())
                    .set(picture)
                    .addOnFailureListener(Throwable::printStackTrace);
            FS.getReference()
                    .child("images/"+picture.getId()+"."+picture.getExtension())
                    .putFile(picUri)
                    .addOnFailureListener(Throwable::printStackTrace);
        }else{
            toDB.setImage_id("");
        }
        FFst.collection("Recipes")
                .document(toDB.getId())
                .set(toDB)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        startActivity(new Intent(this, OwnRecipeActivity.class));
                        finish();
                    }else {
                        task.getException().printStackTrace();
                    }
                });
    }
}
