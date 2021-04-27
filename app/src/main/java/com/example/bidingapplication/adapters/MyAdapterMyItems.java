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
import java.util.zip.Inflater;

public class MyAdapterMyItems extends RecyclerView.Adapter<MyAdapterMyItems.MyViewHolder> {

    private ArrayList<item> items;
    Context context;
    private MyAdapterMyItems.onNoteListener2 onNoteListener2;

    public MyAdapterMyItems(ArrayList<item> items, Context context, onNoteListener2 onNoteListener2) {
        this.items = items;
        this.context = context;
        this.onNoteListener2 = onNoteListener2;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
        return new MyViewHolder(view,onNoteListener2);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.price.setText(items.get(position).getPrice());
        holder.name.setText(items.get(position).getName());
        holder.user.setText(items.get(position).getUsername());
        Glide.with(context).load(items.get(position).getImageUrl()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name = itemView.findViewById(R.id.itemNameView);
        TextView price = itemView.findViewById(R.id.itemPriceView);
        TextView user = itemView.findViewById(R.id.itemOwner);
        ImageView imageView = itemView.findViewById(R.id.imageItemView);
        MyAdapterMyItems.onNoteListener2 onNoteListener2;

        public MyViewHolder(@NonNull View itemView, onNoteListener2 onNoteListener2) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.onNoteListener2 = onNoteListener2;
        }

        @Override
        public void onClick(View v) {
            onNoteListener2.onNoteClick(getAdapterPosition());

        }
    }
    public interface  onNoteListener2{
        void onNoteClick(int position);
    }
}
