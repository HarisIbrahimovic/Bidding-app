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
    private ArrayList<item> items;
    private Context context;
    private onNoteListener onNoteListener;

    public MyAdapterItems(ArrayList<item> items, Context context,onNoteListener onNoteListener) {
        this.items = items;
        this.context = context;
        this.onNoteListener = onNoteListener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
       return new MyViewHolder(view, onNoteListener);
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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name = itemView.findViewById(R.id.itemNameView);
        TextView price = itemView.findViewById(R.id.itemPriceView);
        ImageView image = itemView.findViewById(R.id.imageItemView);
        TextView itemOwner = itemView.findViewById(R.id.itemOwner);
        onNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView,onNoteListener onNoteListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.onNoteListener=onNoteListener;



        }




        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());

        }
    }
    public interface  onNoteListener{
            void onNoteClick(int position);
    }
}
