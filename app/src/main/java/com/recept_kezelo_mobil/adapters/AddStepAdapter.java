package com.recept_kezelo_mobil.adapters;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.recept_kezelo_mobil.R;
import com.recept_kezelo_mobil.models.Step;

import java.util.ArrayList;

public class AddStepAdapter extends RecyclerView.Adapter<AddStepAdapter.AddStepHolder>{
    ArrayList<Step> models;

    public AddStepAdapter(ArrayList<Step> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public AddStepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.addstep_item, parent, false);
        AddStepHolder viewHolder= new AddStepHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddStepHolder holder, int position) {
        Step model = models.get(holder.getBindingAdapterPosition());

        holder.stepNumber.setText(String.valueOf(holder.getBindingAdapterPosition()+1));
        holder.stepDescription.setText(model.getStepDescription());
        holder.stepDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                models.get(holder.getBindingAdapterPosition()).setStepDescription( holder.stepDescription.getText().toString());
            }
        });
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.recview_anim);
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class AddStepHolder extends RecyclerView.ViewHolder{

        TextView stepNumber;
        TextInputEditText stepDescription;
        public AddStepHolder(@NonNull View itemView) {
            super(itemView);
            stepNumber = itemView.findViewById(R.id.stepNumber);
            stepDescription = itemView.findViewById(R.id.stepDescription);
            itemView.findViewById(R.id.removeitem).setOnClickListener( v -> {
                removeItem(itemView , getBindingAdapterPosition());
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
    public void addItem(@Nullable Step item){
        if(item==null){
            models.add(new Step());
        }else {
            models.add(item);
        }
        notifyItemInserted(models.size()-1);
    }
    public ArrayList<Step> getModels(){
        return models;
    }
}
