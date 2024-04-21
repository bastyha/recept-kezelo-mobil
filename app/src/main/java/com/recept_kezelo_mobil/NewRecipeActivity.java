package com.recept_kezelo_mobil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

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
    TextView picUploader;
    ImageView pictureToBeUploaded;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newrecipe);

        recipeName = findViewById(R.id.recipeName);
        timeInMinutes = findViewById(R.id.timeInMinutes);
        ingredients = findViewById(R.id.ingredients);
        steps = findViewById(R.id.steps);
        picUploader = findViewById(R.id.picUplader);
        pictureToBeUploaded = findViewById(R.id.pictureToBeUploaded);

    }
}
