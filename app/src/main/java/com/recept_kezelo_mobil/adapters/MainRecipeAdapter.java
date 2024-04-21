package com.recept_kezelo_mobil.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.recept_kezelo_mobil.R;
import com.recept_kezelo_mobil.ViewRecipeActivity;
import com.recept_kezelo_mobil.models.Ingredient;
import com.recept_kezelo_mobil.models.Recipe;
import com.recept_kezelo_mobil.serverhandlers.ServerUtil;

import java.util.ArrayList;


public class MainRecipeAdapter extends RecyclerView.Adapter<MainRecipeAdapter.RecipeHolder> {
    Context mContext;
    ArrayList<Recipe> models;


    public MainRecipeAdapter(Context mContext, ArrayList<Recipe> models) {
        this.mContext = mContext;
        this.models = models;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recipe, parent, false);
        RecipeHolder viewHolder = new RecipeHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        Recipe model = models.get(position);
        holder.recipeName.setText(model != null ? model.getName() : "nincs");
        holder.time.setText(new ServerUtil().timeWriter(model.getTimeInMinutes()));
        if(model.getImage_id()==null || model.getImage_id().isEmpty() ){
            holder.pic.setVisibility(View.GONE);
            holder.line.setVisibility(View.VISIBLE);
            holder.ingredientsTV.setVisibility(View.VISIBLE);
            holder.ingredients.setVisibility(View.VISIBLE);
            IngredientAdapter adapter = new IngredientAdapter( (ArrayList<Ingredient>) model.getIngredients());
            holder.ingredients.setAdapter(adapter);

            holder.ingredients.setLayoutManager(new GridLayoutManager(mContext, 2));
        }else{
            holder.pic.setVisibility(View.VISIBLE);
            holder.line.setVisibility(View.GONE);
            holder.ingredientsTV.setVisibility(View.GONE);
            holder.ingredients.setVisibility(View.GONE);
            new ServerUtil().getDownloadUrl(model.getImage_id(), holder.pic);
        }

        holder.holderView.setOnClickListener(v -> {
            Intent viewRecipe = new Intent(mContext, ViewRecipeActivity.class);
            viewRecipe.putExtra("RECIPE", model.getId());
            mContext.startActivity(viewRecipe);

        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class RecipeHolder extends RecyclerView.ViewHolder {
        TextView recipeName;
        ImageView pic;
        View line;
        TextView ingredientsTV;
        TextView time;
        RecyclerView ingredients;
        View holderView;
        public RecipeHolder(@NonNull View itemView) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.recipeName);
            pic = itemView.findViewById(R.id.picture);
            line = itemView.findViewById(R.id.line);
            ingredientsTV = itemView.findViewById(R.id.ingredientsTV);
            ingredients = itemView.findViewById(R.id.ingredients);
            time = itemView.findViewById(R.id.timeInMinutes);
            holderView = itemView;
        }
    }

}
