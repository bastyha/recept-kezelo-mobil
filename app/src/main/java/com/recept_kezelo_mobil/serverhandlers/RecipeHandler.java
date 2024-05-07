package com.recept_kezelo_mobil.serverhandlers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.recept_kezelo_mobil.models.Recipe;

import java.util.Objects;

public class RecipeHandler {
    private static final String collection = "Recipes";

    public static Task<QuerySnapshot> readByLoggedInUser(){
        FirebaseFirestore mFFst= FirebaseFirestore.getInstance();
        return mFFst.collection(collection)
                .whereEqualTo("owner", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get();
    }
    public static Task<QuerySnapshot> readAll(){
        FirebaseFirestore mFFst= FirebaseFirestore.getInstance();
        return mFFst.collection(collection)
                .get();
    }

    public static Task<QuerySnapshot> readById(String recipeId){
        FirebaseFirestore mFFst= FirebaseFirestore.getInstance();
        return mFFst.collection(collection)
                .whereEqualTo("id", recipeId )
                .get();
    }

    public static Task<Void> create(Recipe model){
        FirebaseFirestore mFFst= FirebaseFirestore.getInstance();
        if(model.getId()==null|| model.getId().equals("")){
            model.setId(mFFst.collection(collection).document().getId());
        }
        return mFFst.collection(collection)
                .document(model.getId())
                .set(model);

    }


    public static void delete(Recipe model){
        FirebaseFirestore mFFst= FirebaseFirestore.getInstance();
        if(!Objects.equals(model.getImage_id(), "")){
            PictureHandler.delete(model.getImage_id());
        }
        ReviewHandler.deleteForRecipe(model.getId());

        mFFst.collection(collection).document(model.getId()).delete().addOnFailureListener(Throwable::printStackTrace);
    }


}
