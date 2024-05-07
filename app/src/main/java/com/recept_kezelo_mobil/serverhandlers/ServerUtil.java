package com.recept_kezelo_mobil.serverhandlers;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.recept_kezelo_mobil.adapters.ReviewAdapter;
import com.recept_kezelo_mobil.models.Picture;
import com.recept_kezelo_mobil.models.Review;
import com.recept_kezelo_mobil.models.User;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class  ServerUtil {

    public ServerUtil(){
    }

    public void putNameInView(String guysid, TextView v){
        UserHandler.readForId(guysid)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()){
                        User usr = task1.getResult().toObjects(User.class).get(0);
                        v.setText(usr.getName().getLastname()+" "+usr.getName().getFirstname());
                    }else {
                        Log.e("UserName", "onCreate: ", task1.getException());
                    }

                });
    }
    public void fillReviewRecycler(String idOfRecipe, RecyclerView rv){
        ReviewHandler.readForRecipe(idOfRecipe)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()){
                        ArrayList<Review> reviews1 = (ArrayList<Review>) task1.getResult().toObjects(Review.class);
                        if(reviews1.size()>0){

                            ReviewAdapter reviewAdapter = new ReviewAdapter(reviews1);
                            rv.setAdapter(reviewAdapter);
                            rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
                        }
                    }else{
                        Log.e("Comments", "onCreate: ",task1.getException() );
                    }
                });
    }


    public void getDownloadUrl(String imageId, ImageView pic) {

        PictureHandler.readPicture(imageId)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Picture image;
                        image = task.getResult().toObjects(Picture.class).get(0);


                        PictureHandler.readDownloadUri(image)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()){
                                        Uri downloadUrl = task1.getResult();
                                        Picasso.get().load(downloadUrl).into(pic);
                                    }else {
                                        Log.e("GetImageToShow", "getDownloadUrl: ",task1.getException() );
                                    }
                                });
                    }else{
                        Log.e("GetImageToShow", "populateImage: ", task.getException());
                    }
                });
    }

    public String timeWriter(int timeInMinutes){
        return timeInMinutes/60+ " óra és "+timeInMinutes%60+" perc elkészíteni";
    }

    public String getMimeType(Context context, Uri uri){
        String extension;
        if(uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)){
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        }else{
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }
        return extension;
    }
}
