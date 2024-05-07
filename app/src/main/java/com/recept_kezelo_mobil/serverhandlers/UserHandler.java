package com.recept_kezelo_mobil.serverhandlers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.recept_kezelo_mobil.models.User;

public class UserHandler {
    private static final String collection = "Users";
    public static Task<Void> create(User model){
        FirebaseFirestore mFFst = FirebaseFirestore.getInstance();
        return mFFst.collection(collection).document(model.getId()).set(model);
    }

    public static Task<QuerySnapshot> readForId(String userid){
        FirebaseFirestore mFFst = FirebaseFirestore.getInstance();
        return mFFst.collection(collection)
                .whereEqualTo("id", userid)
                .get();
    }
}
