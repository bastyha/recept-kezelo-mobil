package com.recept_kezelo_mobil.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.recept_kezelo_mobil.R;
import com.recept_kezelo_mobil.models.Step;

import java.util.ArrayList;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {

    ArrayList<Step> models;

    public StepAdapter(ArrayList<Step> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public StepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.step_item, parent, false);
        StepHolder viewHolder = new StepHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepHolder holder, int position) {
        Step model = models.get(position);
        holder.stepNumber.setText(position+1+".");
        holder.stepDescription.setText(model.getStepDescription());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class StepHolder extends RecyclerView.ViewHolder{
        TextView stepNumber;
        TextView stepDescription;
        public StepHolder(@NonNull View itemView) {
            super(itemView);
            stepNumber = itemView.findViewById(R.id.stepNumber);
            stepDescription = itemView.findViewById(R.id.stepDescription);
        }
    }
}
