package com.recept_kezelo_mobil.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.recept_kezelo_mobil.R;
import com.recept_kezelo_mobil.models.Ingredient;
import com.recept_kezelo_mobil.models.Recipe;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {

    ArrayList<Ingredient> models;
    public IngredientAdapter(ArrayList<Ingredient> ingredients) {
        models=ingredients;
    }

    @NonNull
    @Override
    public IngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        IngredientHolder viewHolder = new IngredientHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientHolder holder, int position) {
        Ingredient model = models.get(position);
        holder.ing.setText(String.format("• %s %s %s", model.getAmount(), model.getUnit(), model.getNameOfIngredient()));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class IngredientHolder extends RecyclerView.ViewHolder{
        TextView ing;
        public IngredientHolder(@NonNull View itemView) {
            super(itemView);
            ing =itemView.findViewById(R.id.ingredientItem);
        }
    }
}
