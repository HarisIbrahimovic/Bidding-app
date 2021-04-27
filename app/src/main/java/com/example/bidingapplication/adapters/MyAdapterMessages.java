package com.example.bidingapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bidingapplication.R;
import com.example.bidingapplication.objects.message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MyAdapterMessages extends  RecyclerView.Adapter<MyAdapterMessages.MyViewHolder>{
    ArrayList<message> messages;
    Context context;
    private FirebaseAuth auth;
    public MyAdapterMessages(ArrayList<message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }
    public static final int msg_left=0;
    public static final int msg_right=1;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(msg_right==viewType){
            View view = LayoutInflater.from(context).inflate(R.layout.mess_right,parent,false);
            return new MyViewHolder(view);}
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.mess_left,parent,false);
            return new MyViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        message Message = messages.get(position);
        holder.content.setText(Message.getContent());
    }
    @Override
    public int getItemCount() {
        return messages.size();
    }
    public static class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView content = itemView.findViewById(R.id.RightMessage);
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    @Override
    public int getItemViewType(int position) {
        auth = FirebaseAuth.getInstance();
        if(messages.get(position).getSenderId().equals(auth.getCurrentUser().getUid())){
            return msg_right;
        }else return msg_left;
    }
}
