package com.recept_kezelo_mobil.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.recept_kezelo_mobil.R;
import com.recept_kezelo_mobil.models.Name;
import com.recept_kezelo_mobil.models.Review;
import com.recept_kezelo_mobil.models.User;
import com.recept_kezelo_mobil.serverhandlers.ServerUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder>{
    ArrayList<Review> models;

    public ReviewAdapter(ArrayList<Review> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        ReviewHolder viewHolder =  new ReviewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        Review model = models.get(position);

        new ServerUtil().putNameInView(model.getReviewer(), holder.reviewer);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        holder.date.setText(dateFormat.format(model.getDate()));
        holder.reviewText.setText(model.getText());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder{
        TextView reviewer;
        TextView date;
        TextView reviewText;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            reviewer=itemView.findViewById(R.id.reviewer);
            date=itemView.findViewById(R.id.date);
            reviewText=itemView.findViewById(R.id.reviewText);
        }
    }
}
