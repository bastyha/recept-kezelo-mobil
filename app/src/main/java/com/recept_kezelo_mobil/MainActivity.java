package com.recept_kezelo_mobil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.recept_kezelo_mobil.adapters.MainRecipeAdapter;
import com.recept_kezelo_mobil.models.Recipe;
import com.recept_kezelo_mobil.serverhandlers.RecipeHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

        } else if (itemId == R.id.ownrecipe) {
            startActivity(new Intent(this, OwnRecipeActivity.class));

        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recipesRV = findViewById(R.id.recipes);
        ArrayList<Recipe> recipesList = new ArrayList<>();

        RecipeHandler.readAll()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot documentSnapshot: task.getResult()){

                           recipesList.add(documentSnapshot.toObject(Recipe.class));
                        }
                        MainRecipeAdapter adapter = new MainRecipeAdapter(this, recipesList);
                        recipesRV.setAdapter(adapter);
                        FlexboxLayoutManager flm = new FlexboxLayoutManager(this);
                        flm.setJustifyContent(JustifyContent.SPACE_EVENLY);
                        flm.setFlexWrap(FlexWrap.WRAP);

                        recipesRV.setLayoutManager(flm);

                    }else {
                        Log.e("RecipeAdd", "onCreate: ",task.getException() );
                    }
                });
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, CustomReceiver.class);
        PendingIntent pendingIntent  = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,14);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)>calendar.get(Calendar.HOUR_OF_DAY)){
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),3* AlarmManager.INTERVAL_HOUR, pendingIntent);

    }
}