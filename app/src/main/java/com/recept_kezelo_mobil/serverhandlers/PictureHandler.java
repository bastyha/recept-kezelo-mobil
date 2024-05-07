package com.recept_kezelo_mobil.serverhandlers;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.recept_kezelo_mobil.models.Picture;

public class PictureHandler {
    private static final String collection = "Images";
    private static final String bucketName = "images/";

    public static Task<QuerySnapshot> readPicture(String imageId){
        FirebaseFirestore mFFst=FirebaseFirestore.getInstance();
        return mFFst.collection(collection)
                .whereEqualTo("id", imageId)
                .get();
    }

    public static Task<Uri> readDownloadUri(Picture image) {
        return FirebaseStorage.getInstance()
                .getReference()
                .child(bucketName+image.getId()+"."+image.getExtension()).getDownloadUrl();
    }

    public static String create(Picture model, Uri picUri){
        FirebaseFirestore mFFst=FirebaseFirestore.getInstance();
        model.setId(mFFst.collection(collection).document().getId());
        update(model, picUri);
        return model.getId();
    }


    public static String update(Picture model, Uri picUri) {

        FirebaseFirestore mFFst=FirebaseFirestore.getInstance();
        FirebaseStorage mFS = FirebaseStorage.getInstance();

        mFFst.collection(collection)
                .document(model.getId())
                .set(model)
                .addOnFailureListener(Throwable::printStackTrace);
        mFS.getReference()
                .child(bucketName+model.getId()+"."+model.getExtension())
                .putFile(picUri)
                .addOnFailureListener(Throwable::printStackTrace);
        return model.getId();
    }

    public static void delete(String picId){
        FirebaseFirestore mFFst=FirebaseFirestore.getInstance();
        FirebaseStorage mFS = FirebaseStorage.getInstance();
        mFFst.collection(collection)
                .whereEqualTo("id", picId)
                .get()
                .addOnCompleteListener(pictureObj->{
                    if (pictureObj.isSuccessful()){
                        if(pictureObj.getResult().toObjects(Picture.class).size()>0) {
                            Picture picture = pictureObj.getResult().toObjects(Picture.class).get(0);

                            mFS.getReference().child(bucketName + picture.getId() + "." + picture.getExtension()).delete().addOnFailureListener(Throwable::printStackTrace);
                            mFFst.collection(collection).document(picture.getId()).delete().addOnFailureListener(Throwable::printStackTrace);
                        }
                    }else {
                        pictureObj.getException().printStackTrace();
                    }
                });
    }
}
