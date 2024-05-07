package com.recept_kezelo_mobil.serverhandlers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.recept_kezelo_mobil.models.Review;

import java.util.List;

public class ReviewHandler {
    private static final String collection="Reviews";

    public static Task<Void> create(Review model){
        FirebaseFirestore mFFst=FirebaseFirestore.getInstance();
        model.setId(mFFst.collection(collection).document().getId());
        return mFFst.collection(collection)
                .document(model.getId())
                .set(model);
    }

    public static Task<QuerySnapshot> readForRecipe(String recipeid){
        FirebaseFirestore mFFst=FirebaseFirestore.getInstance();
        return mFFst.collection(collection)
                .whereEqualTo("recipe", recipeid)
                .orderBy("date", Query.Direction.DESCENDING)
                .get();
    }

    public static void deleteForRecipe(String recipeId){
        FirebaseFirestore mFFst=FirebaseFirestore.getInstance();
        readForRecipe(recipeId)
                .addOnCompleteListener(getReviews ->{
                    if (getReviews.isSuccessful()){
                        List<Review> reviews = getReviews.getResult().toObjects(Review.class);
                        if(reviews.size()>0){
                            for(Review review :reviews){
                                mFFst.collection(collection)
                                        .document(review.getId())
                                        .delete()
                                        .addOnFailureListener(Throwable::printStackTrace);
                            }
                        }

                    }else {
                        getReviews.getException().printStackTrace();
                    }
                } );
    }
}
