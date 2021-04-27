package com.example.bidingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bidingapplication.adapters.MyAdapterItems;
import com.example.bidingapplication.itemStuff.createItemActivity;
import com.example.bidingapplication.itemStuff.detailedItemActivity;
import com.example.bidingapplication.objects.User;
import com.example.bidingapplication.objects.item;
import com.example.bidingapplication.profile.myProfileActivity;
import com.example.bidingapplication.signup.loginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends AppCompatActivity implements MyAdapterItems.onNoteListener {
    private FirebaseAuth auth;
    private FloatingActionButton addItem, logout, myProfile;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private MyAdapterItems myAdapterItems;
    private ArrayList<item> items;
    private User currentUser;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configWidgets();
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()==null){
            startActivity(new Intent(getApplicationContext(),loginActivity.class));
            finish();
        }
        items = new ArrayList<>();
        myAdapterItems = new MyAdapterItems(items,getApplicationContext(),this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapterItems);
        databaseReference = FirebaseDatabase.getInstance().getReference("Items");
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    if(snapshot1.getKey().equals(auth.getCurrentUser().getUid())){
                        currentUser= snapshot1.getValue(User.class);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                        item Item = snapshot1.getValue(item.class);
                        items.add(Item);
                    }
                myAdapterItems.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getApplicationContext(),loginActivity.class));
                finish();
            }
        });
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),createItemActivity.class);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("username",currentUser.getUsername());
                startActivity(intent);
            }
        });
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), myProfileActivity.class);
                intent.putExtra("username",currentUser.getUsername());
                intent.putExtra("imageUrl",currentUser.getImageUrl());
                intent.putExtra("email",currentUser.getEmail());
                intent.putExtra("password",currentUser.getPassword());
                intent.putExtra("id",auth.getCurrentUser().getUid());
                startActivity(intent);
            }
        });
    }



    private void configWidgets() {
        addItem = findViewById(R.id.addItem);
        logout = findViewById(R.id.logoutButton);
        myProfile = findViewById(R.id.myProfile);
        recyclerView = findViewById(R.id.itemRecyclerView);
        myAdapterItems = new MyAdapterItems(items,getApplicationContext(),this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapterItems);
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(getApplicationContext(),detailedItemActivity.class);
        intent.putExtra("name",items.get(position).getName());
        intent.putExtra("desc",items.get(position).getDesc());
        intent.putExtra("imageUrl",items.get(position).getImageUrl());
        intent.putExtra("username",items.get(position).getUsername());
        intent.putExtra("price",items.get(position).getPrice());
        intent.putExtra("id",items.get(position).getId());
        intent.putExtra("currentUserName",currentUser.getUsername());
        intent.putExtra("curentBestBid",items.get(position).getBestBid());
        intent.putExtra("ownerId",items.get(position).getOwnerId());
        startActivity(intent);  }
}