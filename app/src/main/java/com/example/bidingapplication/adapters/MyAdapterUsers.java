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
import com.example.bidingapplication.objects.User;

import java.util.ArrayList;

public class MyAdapterUsers extends RecyclerView.Adapter<MyAdapterUsers.MyViewHolder> {
    private ArrayList<User> users;
    private Context context;
    private touchListener TouchListener;

    public MyAdapterUsers(ArrayList<User> users, Context context, touchListener touchListener) {
        this.users = users;
        this.context = context;
        TouchListener = touchListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);

        return new MyViewHolder(view,TouchListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = users.get(position);
        Glide.with(context).load(user.getImageUrl()).into(holder.imageUrl);
        holder.username.setText(user.getUsername());

    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageUrl = itemView.findViewById(R.id.userProfilePicMyMess);
        TextView username = itemView.findViewById(R.id.userNameProfilePicMyMess);
        public MyViewHolder(@NonNull View itemView, touchListener touchListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            TouchListener = touchListener;
        }



        @Override
        public void onClick(View v) {
            TouchListener.onNoteClick(getAdapterPosition());
        }
    }
        public interface touchListener{
            void onNoteClick(int position);

        }
}
