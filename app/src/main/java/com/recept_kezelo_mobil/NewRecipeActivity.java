package com.recept_kezelo_mobil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.recept_kezelo_mobil.adapters.AddIngredientAdapter;
import com.recept_kezelo_mobil.adapters.AddStepAdapter;
import com.recept_kezelo_mobil.models.Ingredient;
import com.recept_kezelo_mobil.models.Picture;
import com.recept_kezelo_mobil.models.Recipe;
import com.recept_kezelo_mobil.models.Step;
import com.recept_kezelo_mobil.serverhandlers.ServerUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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
        store=false;
        getPreferences(Context.MODE_PRIVATE).edit().remove("RECIPE").apply();
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

    TextInputEditText recipeName;
    TimePicker timeInMinutes;
    RecyclerView ingredients;
    RecyclerView steps;
    ImageView pictureToBeUploaded;
    Uri picUri;

    ServerUtil mSU;
    FirebaseFirestore mFFst;
    FirebaseStorage mFS;
    Recipe base;

    boolean store = true;

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
        mFFst = FirebaseFirestore.getInstance();
        mFS  = FirebaseStorage.getInstance();
        store = true;
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String recipe_from_pref = sharedPreferences.getString("RECIPE", null);

        String recipe =  getIntent().getStringExtra("RECIPE");
        if(recipe!=null){
            base = new Gson().fromJson(recipe, Recipe.class);
        } else if (recipe_from_pref!=null) {
            base = new Gson().fromJson(recipe_from_pref, Recipe.class);

        }else{
            base = null;
        }



        recipeName = findViewById(R.id.recipeName);
        timeInMinutes = findViewById(R.id.timeInMinutes);
        timeInMinutes.setIs24HourView(true);
        if(base==null){

            timeInMinutes.setHour(1);
            timeInMinutes.setMinute(0);
        }else{
            recipeName.setText(base.getName());
            timeInMinutes.setHour(base.getTimeInMinutes()/60);
            timeInMinutes.setMinute(base.getTimeInMinutes()%60);
        }

        ingredients = findViewById(R.id.ingredients);
        AddIngredientAdapter addIngredientAdapter ;
        if(base==null) {
            addIngredientAdapter = new AddIngredientAdapter(this, new ArrayList<>());
        }else {
            addIngredientAdapter = new AddIngredientAdapter(this, (ArrayList<Ingredient>) base.getIngredients());
        }
        ingredients.setAdapter(addIngredientAdapter);
        ingredients.setLayoutManager(new LinearLayoutManager(this));

        steps = findViewById(R.id.steps);
        AddStepAdapter addStepAdapter;
        if(base == null){
            addStepAdapter  = new AddStepAdapter(new ArrayList<>());
        }else{
            addStepAdapter  = new AddStepAdapter((ArrayList<Step>) base.getSteps());
        }
        steps.setAdapter(addStepAdapter);
        steps.setLayoutManager(new LinearLayoutManager(this));


        pictureToBeUploaded = findViewById(R.id.pictureToBeUploaded);
        if(base!=null&&base.getImage_id()!=null &&!base.getImage_id().equals("")){
            mSU.getDownloadUrl(base.getImage_id(), pictureToBeUploaded);
        }
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
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)== PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

            selectPicPicSelectPart();
        }else{
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){

                requestPermissions( new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, 100);
            }else{
                requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }
    private void selectPicPicSelectPart(){
        pickPic.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 100:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    selectPicPicSelectPart();
                }else{
                    Toast.makeText(this, "Nem engedted, szóval nincs kép", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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


        Recipe toDB = new Recipe();
        if(base==null){

            toDB.setId(mFFst.collection("Recipes").document().getId());
        }else{
            toDB.setId(base.getId());
        }
        toDB.setName(name);
        toDB.setTimeInMinutes(time);
        toDB.setOwner(FirebaseAuth.getInstance().getCurrentUser().getUid());
        toDB.setIngredients(ingredients1);
        toDB.setSteps(steps1);
        if(picUri!=null){
            Picture picture = new Picture();
            if(base==null ||base.getImage_id().equals("")){

            picture.setId(mFFst.collection("Images").document().getId());
            }else{
                picture.setId(base.getImage_id());
            }
            picture.setUploader(FirebaseAuth.getInstance().getCurrentUser().getUid());
            picture.setExtension(mSU.getMimeType(this, picUri));
            toDB.setImage_id(picture.getId());
            mFFst.collection("Images")
                    .document(picture.getId())
                    .set(picture)
                    .addOnFailureListener(Throwable::printStackTrace);
            mFS.getReference()
                    .child("images/"+picture.getId()+"."+picture.getExtension())
                    .putFile(picUri)
                    .addOnFailureListener(Throwable::printStackTrace);
        }else if(base==null ||base.getImage_id().equals("")){
            toDB.setImage_id("");
        }
        mFFst.collection("Recipes")
                .document(toDB.getId())
                .set(toDB)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        store=false;
                        getPreferences(Context.MODE_PRIVATE).edit().remove("RECIPE").apply();
                        startActivity(new Intent(this, OwnRecipeActivity.class));
                        finish();
                    }else {
                        task.getException().printStackTrace();
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (store){
            Recipe toDB = new Recipe();
            String name = recipeName.getText().toString();
            int time = timeInMinutes.getHour() * 60 + timeInMinutes.getMinute();
            ArrayList<Ingredient> ingredients1 = ((AddIngredientAdapter) ingredients.getAdapter()).getModels();
            ArrayList<Step> steps1 = ((AddStepAdapter) steps.getAdapter()).getModels();
            toDB.setName(name);
            toDB.setTimeInMinutes(time);
            toDB.setOwner(FirebaseAuth.getInstance().getCurrentUser().getUid());
            toDB.setIngredients(ingredients1);
            toDB.setSteps(steps1);
            SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("RECIPE", new Gson().toJson(toDB));
            editor.apply();
        }
    }

}
