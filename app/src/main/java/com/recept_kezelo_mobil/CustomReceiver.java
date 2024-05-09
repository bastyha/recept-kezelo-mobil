package com.recept_kezelo_mobil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.recept_kezelo_mobil.models.Recipe;
import com.recept_kezelo_mobil.serverhandlers.RecipeHandler;

import java.util.List;
import java.util.Random;

public class CustomReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new HighNoonFood(context).send("Készíts magadnak valamit");



    }
}
