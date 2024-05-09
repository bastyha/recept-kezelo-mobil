package com.recept_kezelo_mobil.adapters;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.recept_kezelo_mobil.R;
import com.recept_kezelo_mobil.models.Ingredient;

import java.util.ArrayList;

public class AddIngredientAdapter extends RecyclerView.Adapter<AddIngredientAdapter.AddIngredientHolder>{

    ArrayList<Ingredient> models;
    Context mContext;

    public AddIngredientAdapter(Context context,  ArrayList<Ingredient> models) {
        this.models = models;
        mContext=context;
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
            }
        });

        holder.amount.setText(String.valueOf( model.getAmount()));

        holder.amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                models.get(holder.getBindingAdapterPosition()).setAmount(!holder.amount.getText().toString().equals("") ? Double.parseDouble(holder.amount.getText().toString()):0);
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
            }
        });
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.recview_anim);
        holder.itemView.startAnimation(animation);
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
                removeItem(itemView, getLayoutPosition());
            });
        }
    }

    public void removeItem(View v, int position){

        Animation anim = AnimationUtils.loadAnimation(v.getContext(), R.anim.recviewdel_anim);
        anim.setDuration(500);
        v.startAnimation(anim);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                models.remove(position);
                notifyItemRemoved( position);
                notifyItemRangeChanged(position, models.size());
            }
        }, anim.getDuration());

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
