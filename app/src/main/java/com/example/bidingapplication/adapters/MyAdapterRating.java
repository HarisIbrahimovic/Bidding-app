package com.example.bidingapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bidingapplication.R;
import com.example.bidingapplication.objects.rating;

import java.util.ArrayList;

public class MyAdapterRating extends RecyclerView.Adapter<MyAdapterRating.MyViewHolder> {
    Context context;
    ArrayList<rating> ratings;

    public MyAdapterRating(Context context, ArrayList<rating> ratings) {
        this.context = context;
        this.ratings = ratings;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rating_design,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ratingDesc.setText(ratings.get(position).getDesc());
        holder.ratingScore.setText(ratings.get(position).getStars()+"/5");
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ratingScore = itemView.findViewById(R.id.userRatingView);
        TextView ratingDesc = itemView.findViewById(R.id.descriptionView);
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
