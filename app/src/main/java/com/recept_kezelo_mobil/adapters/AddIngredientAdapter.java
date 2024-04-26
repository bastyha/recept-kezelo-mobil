package com.recept_kezelo_mobil.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.recept_kezelo_mobil.R;
import com.recept_kezelo_mobil.models.Ingredient;
import com.recept_kezelo_mobil.models.Step;

import java.util.ArrayList;

public class AddIngredientAdapter extends RecyclerView.Adapter<AddIngredientAdapter.AddIngredientHolder>{

    ArrayList<Ingredient> models;

    public AddIngredientAdapter(ArrayList<Ingredient> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public AddIngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.addingredient_item, parent, false);
        AddIngredientHolder viewHolder = new AddIngredientHolder(v);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddIngredientHolder holder, int position) {
        Ingredient model = models.get(holder.getBindingAdapterPosition());

        holder.ingredientName.setText( model.getNameOfIngredient());
        holder.ingredientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                models.get(holder.getBindingAdapterPosition()).setNameOfIngredient( holder.ingredientName.getText().toString());
                Log.d("textChange", models.get(holder.getBindingAdapterPosition()).getNameOfIngredient());
            }
        });
        if(model.getAmount()!=0){
            holder.amount.setText(String.valueOf( model.getAmount()));
        }
        holder.amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                models.get(holder.getBindingAdapterPosition()).setAmount(Double.parseDouble(holder.amount.getText().toString()));
                Log.d("textChange", String.valueOf(models.get(holder.getBindingAdapterPosition()).getAmount()));
            }
        });
        holder.unit.setText(model.getUnit());
        holder.unit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                models.get(holder.getBindingAdapterPosition()).setUnit( holder.unit.getText().toString());
                Log.d("textChange", models.get(holder.getBindingAdapterPosition()).getUnit());
            }
        });
        //TODO: make it a listview instead
    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    public class AddIngredientHolder extends RecyclerView.ViewHolder{
        TextInputEditText ingredientName;
        TextInputEditText amount;
        TextInputEditText unit;

        public AddIngredientHolder(@NonNull View itemView) {
            super(itemView);

            ingredientName = itemView.findViewById(R.id.ingredientName);
            amount = itemView.findViewById(R.id.amount);
            unit = itemView.findViewById(R.id.unit);
            itemView.findViewById(R.id.removeitem).setOnClickListener( v -> {
                removeItem(getLayoutPosition());
            });
        }
    }

    public void removeItem(int position){
        models.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, models.size());

    }
    public void addItem(@Nullable Ingredient item){
        if(item==null){
            models.add(new Ingredient());
        }else {
            models.add(item);
        }
        notifyItemInserted(models.size()-1);
    }
    public ArrayList<Ingredient> getModels(){
        return models;
    }

}
