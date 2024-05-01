package com.recept_kezelo_mobil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.recept_kezelo_mobil.adapters.IngredientAdapter;
import com.recept_kezelo_mobil.adapters.StepAdapter;
import com.recept_kezelo_mobil.models.Ingredient;
import com.recept_kezelo_mobil.models.Recipe;
import com.recept_kezelo_mobil.models.Review;
import com.recept_kezelo_mobil.models.Step;
import com.recept_kezelo_mobil.serverhandlers.ServerUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ViewRecipeActivity extends AppCompatActivity {
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

    private ImageView image;
    private TextView recipeName;
    private TextView owner;
    private TextView timeInMinutes;
    private RecyclerView ingredients;
    private RecyclerView steps;
    private RecyclerView reviews;
    private Editable reviewText;
    private LinearLayout addComment;

    private FirebaseFirestore mFFst;
    private FirebaseStorage mFS;
    private ServerUtil mSU;
    private Recipe onSreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewrecipe);

        image = findViewById(R.id.image);
        recipeName = findViewById(R.id.recipeName);
        owner = findViewById(R.id.owner);
        timeInMinutes = findViewById(R.id.timeInMinutes);
        ingredients = findViewById(R.id.ingredients);
        steps = findViewById(R.id.steps);
        reviews = findViewById(R.id.reviews);
        reviewText = ((TextInputEditText) findViewById(R.id.reviewText)).getText();
        addComment = findViewById(R.id.addComment);

        mFFst = FirebaseFirestore.getInstance();
        mFS = FirebaseStorage.getInstance();
        mSU = new ServerUtil();

        String recipeId = getIntent().getStringExtra("RECIPE");

        mFFst.collection("Recipes")
                .whereEqualTo("id", recipeId )
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){

                        ArrayList<Recipe> onSreens = (ArrayList<Recipe>) task.getResult().toObjects(Recipe.class);
                        if(onSreens.size()>0){

                            onSreen=onSreens.get(0);
                        }else{
                            Log.d("ViewRecipe", "Recipe not found");
                        }
                        if (onSreen.getImage_id()==null || Objects.equals(onSreen.getImage_id(), "")){
                            image.setVisibility(View.GONE);
                        }else {
                            image.setVisibility(View.VISIBLE);
                            new ServerUtil().getDownloadUrl(onSreen.getImage_id(), image);
                        }
                        recipeName.setText(onSreen.getName());

                        mSU.putNameInView(onSreen.getOwner(), owner);


                        timeInMinutes.setText(mSU.timeWriter(onSreen.getTimeInMinutes()));
                        timeInMinutes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_timer_24, 0, 0, 0);

                        IngredientAdapter ingredientAdapter = new IngredientAdapter((ArrayList<Ingredient>) onSreen.getIngredients());
                        ingredients.setAdapter(ingredientAdapter);
                        ingredients.setLayoutManager(new GridLayoutManager(this, 2));

                        StepAdapter stepAdapter = new StepAdapter((ArrayList<Step>) onSreen.getSteps());
                        steps.setAdapter(stepAdapter);
                        steps.setLayoutManager(new LinearLayoutManager(this));


                        mSU.fillReviewRecycler(onSreen.getId(), reviews);

                        if(FirebaseAuth.getInstance().getCurrentUser()==null){
                            addComment.setVisibility(View.GONE);
                        }else {
                            addComment.setVisibility(View.VISIBLE);
                        }

                    }else{
                       Log.e("ViewRecipe", "onCreate: ",task.getException() );
                       startActivity(new Intent(this, MainActivity.class));
                       finish();
                    }
                });
    }

    public void addReview(View view) {
        if (!reviewText.toString().equals("")){

            Review toDB = new Review();
            toDB.setRecipe(onSreen.getId());
            toDB.setReviewer(FirebaseAuth.getInstance().getCurrentUser().getUid());
            toDB.setDate(new Date().getTime());
            toDB.setText(reviewText.toString());
            toDB.setId(mFFst.collection("Review").document().getId());

            mFFst.collection("Reviews")
                    .document(toDB.getId())
                    .set(toDB)
                    .addOnCompleteListener(task -> {
                       if (task.isSuccessful()){
                           mSU.fillReviewRecycler(onSreen.getId(), reviews);
                           reviewText.clear();
                           Log.d("ReviewAdd", "Valami tortent");
                       }else {
                           task.getException().printStackTrace();
                       }
                    });
        }
    }

}
