package com.recept_kezelo_mobil.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.recept_kezelo_mobil.NewRecipeActivity;
import com.recept_kezelo_mobil.OwnRecipeActivity;
import com.recept_kezelo_mobil.R;
import com.recept_kezelo_mobil.ViewRecipeActivity;
import com.recept_kezelo_mobil.models.Ingredient;
import com.recept_kezelo_mobil.models.Picture;
import com.recept_kezelo_mobil.models.Recipe;
import com.recept_kezelo_mobil.models.Review;
import com.recept_kezelo_mobil.models.Step;
import com.recept_kezelo_mobil.serverhandlers.ServerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OwnRecipeAdapter extends RecyclerView.Adapter<OwnRecipeAdapter.OwnRecipeHolder> {
    private ArrayList<Recipe> models;
    private Context mContext;
    private ServerUtil mSU;
    private FirebaseFirestore mFFst;
    private FirebaseStorage mFS;
    public OwnRecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        this.models = recipes;
        mContext=context;
        mSU = new ServerUtil();
        mFFst=FirebaseFirestore.getInstance();
        mFS = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public OwnRecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ownrecipe_item, parent, false);
        OwnRecipeHolder viewHolder = new OwnRecipeHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OwnRecipeHolder holder, int position) {
        Recipe model = models.get(holder.getBindingAdapterPosition());
        holder.recipeName.setText(model.getName());
        if(model.getImage_id()!=null&& !Objects.equals(model.getImage_id(), "")){
            holder.imageView.setVisibility(View.VISIBLE);
            mSU.getDownloadUrl(model.getImage_id(), holder.imageView);
        }else {
            holder.imageView.setVisibility(View.GONE);
        }
        holder.timeInMinutes.setText(mSU.timeWriter(model.getTimeInMinutes()));

        IngredientAdapter ingredientAdapter = new IngredientAdapter((ArrayList<Ingredient>) model.getIngredients());
        holder.ingredients.setAdapter(ingredientAdapter);
        holder.ingredients.setLayoutManager(new GridLayoutManager( mContext, 2));

        StepAdapter stepAdapter = new StepAdapter((ArrayList<Step>) model.getSteps());
        holder.steps.setAdapter(stepAdapter);
        holder.steps.setLayoutManager(new LinearLayoutManager(mContext));

        holder.modify.setOnClickListener(v -> {
            Intent modifyRecipe = new Intent(mContext, NewRecipeActivity.class);
            modifyRecipe.putExtra("RECIPE", new Gson().toJson(model));
            mContext.startActivity(modifyRecipe);
            ((AppCompatActivity) mContext).finish();

        });
        holder.delete.setOnClickListener(v -> {
            if(!Objects.equals(model.getImage_id(), "")){
                mFFst.collection("Images")
                        .whereEqualTo("id", model.getImage_id())
                        .get()
                        .addOnCompleteListener(pictureObj->{
                            if (pictureObj.isSuccessful()){
                                if(pictureObj.getResult().toObjects(Picture.class).size()>0) {
                                    Picture picture = pictureObj.getResult().toObjects(Picture.class).get(0);

                                    mFS.getReference().child("images/" + picture.getId() + "." + picture.getExtension()).delete().addOnFailureListener(Throwable::printStackTrace);
                                    mFFst.collection("Images").document(picture.getId()).delete().addOnFailureListener(Throwable::printStackTrace);
                                }
                            }else {
                                pictureObj.getException().printStackTrace();
                            }
                        });
            }
            mFFst.collection("Reviews")
                    .whereEqualTo("recipe", model.getId())
                    .get()
                    .addOnCompleteListener(getReviews ->{
                        if (getReviews.isSuccessful()){
                            List<Review> reviews = getReviews.getResult().toObjects(Review.class);
                            if(reviews.size()>0){
                                for(Review review :reviews){
                                    mFFst.collection("Reviews")
                                            .document(review.getId())
                                            .delete()
                                            .addOnFailureListener(Throwable::printStackTrace);
                                }
                            }

                        }else {
                            getReviews.getException().printStackTrace();
                        }
                    } );
            mFFst.collection("Recipes").document(model.getId()).delete().addOnFailureListener(Throwable::printStackTrace);
            mContext.startActivity(new Intent(mContext, OwnRecipeActivity.class));
            ((AppCompatActivity) mContext).finish();
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class OwnRecipeHolder extends RecyclerView.ViewHolder {
        TextView recipeName;
        ImageView imageView;
        TextView timeInMinutes;
        RecyclerView ingredients;
        RecyclerView steps;
        Button modify;
        Button delete;
        public OwnRecipeHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            imageView= itemView.findViewById(R.id.image);
            timeInMinutes= itemView.findViewById(R.id.timeInMinutes);
            ingredients= itemView.findViewById(R.id.ingredients);
            steps= itemView.findViewById(R.id.steps);
            modify= itemView.findViewById(R.id.modify);
            delete = itemView.findViewById(R.id.delete);

        }
    }
}
