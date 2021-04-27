package com.example.bidingapplication.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bidingapplication.R;
import com.example.bidingapplication.adapters.MyAdapterMyItems;
import com.example.bidingapplication.itemStuff.itemOptionsActivity;
import com.example.bidingapplication.objects.User;
import com.example.bidingapplication.objects.item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class myProfileActivity extends AppCompatActivity implements  MyAdapterMyItems.onNoteListener2{

    private FloatingActionButton settings, ratingsButton;
    private ImageView profileImage;
    private TextView userName;
    private String UserName, UserId, UserPicture, UserEmail, UserPassword;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private MyAdapterMyItems myAdapterMyItems;
    private ArrayList<item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        configWidget();
        getIncomingIntent();
        items= new ArrayList<>();
        userName.setText(UserName);
        Glide.with(getApplicationContext()).load(UserPicture).into(profileImage);
        databaseReference = FirebaseDatabase.getInstance().getReference("Items");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapterMyItems = new MyAdapterMyItems(items,getApplicationContext(),this);
        recyclerView.setAdapter(myAdapterMyItems);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    item Item = snapshot1.getValue(item.class);
                    if(Item.getUsername().equals(UserName)){
                        items.add(Item);
                    }

                }
                myAdapterMyItems.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getIncomingIntent() {
        if(getIntent().hasExtra("username")&&getIntent().hasExtra("id")&&getIntent().hasExtra("email")&&getIntent().hasExtra("password")&&getIntent().hasExtra("imageUrl")){
            UserName = getIntent().getStringExtra("username");
            UserId = getIntent().getStringExtra("id");
            UserEmail = getIntent().getStringExtra("email");
            UserPassword = getIntent().getStringExtra("passwrod");
            UserPicture = getIntent().getStringExtra("imageUrl");
        }
    }

    private void configWidget() {
        settings = findViewById(R.id.opetionsButton);
        ratingsButton = findViewById(R.id.ratingsButton);
        profileImage = findViewById(R.id.profileImageOptions);
        userName = findViewById(R.id.optionsUsername);
        recyclerView = findViewById(R.id.myItemsRecView);

    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(getApplicationContext(), itemOptionsActivity.class);
        intent.putExtra("id",items.get(position).getId());
        intent.putExtra("imageUrl",items.get(position).getImageUrl());
        intent.putExtra("desc",items.get(position).getDesc());
        intent.putExtra("price",items.get(position).getPrice());
        intent.putExtra("name",items.get(position).getName());
        startActivity(intent);
    }
}