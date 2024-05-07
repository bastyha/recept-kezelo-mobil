package com.recept_kezelo_mobil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.firestore.FirebaseFirestore;
import com.recept_kezelo_mobil.models.Recipe;

import java.util.List;
import java.util.Random;

public class CustomReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        FirebaseFirestore.getInstance()
                .collection("Recipes").get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful()){
                       List<Recipe> recipeList = task.getResult().toObjects(Recipe.class);
                       if(recipeList.size()>0){
                           Recipe chosen = recipeList.get( new Random().nextInt(recipeList.size()));
                           new HighNoonFood(context).send("Készíts magadnak: "+chosen.getName(), chosen.getId());
                       }
                   }
                });



    }
}
