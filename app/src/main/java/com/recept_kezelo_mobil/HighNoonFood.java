package com.recept_kezelo_mobil;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.recept_kezelo_mobil.models.Recipe;

import java.util.List;
import java.util.Random;

public class HighNoonFood  {
        private static final String  CHANNEL_ID="recept_kezelo";
        private NotificationManager manager;
        Context mContext;
        public HighNoonFood(Context context) {
            mContext=context;
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            createChannel();
        }

        private void createChannel(){
            if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O)
                return;

            NotificationChannel channel = new NotificationChannel( CHANNEL_ID,
                    "Recept kezelo",
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("Receptes uzi");
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.argb(1, 160, 0, 160));
            manager.createNotificationChannel(channel);
        }
        public  void send(String message, String recipeId){
            Intent intent = new Intent(mContext, ViewRecipeActivity.class);
            intent.putExtra("RECIPE", recipeId);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                    .setContentTitle("Ebéd idő lassan")
                    .setContentText(message)
                    .setSmallIcon(R.drawable.recept_icon)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            manager.notify(0, builder.build());


        }

}
