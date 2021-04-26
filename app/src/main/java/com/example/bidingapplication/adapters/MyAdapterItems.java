package com.example.bidingapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bidingapplication.R;
import com.example.bidingapplication.objects.item;

import java.util.ArrayList;

public class MyAdapterItems extends RecyclerView.Adapter<MyAdapterItems.MyViewHolder> {
    ArrayList<item> items;
    Context context;

    public MyAdapterItems(ArrayList<item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        item Item = items.get(position);
        Glide.with(context)
                .load(Item.getImageUrl())
                .into(holder.image);
        holder.name.setText(Item.getName());
        holder.price.setText(Item.getPrice());
        holder.itemOwner.setText(Item.getUsername());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name = itemView.findViewById(R.id.itemNameView);
        TextView price = itemView.findViewById(R.id.itemPriceView);
        ImageView image = itemView.findViewById(R.id.imageItemView);
        TextView itemOwner = itemView.findViewById(R.id.itemOwner);

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
