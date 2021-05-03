package com.example.bidingapplication.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bidingapplication.R;
import com.example.bidingapplication.adapters.MyAdapterMyItems;
import com.example.bidingapplication.itemStuff.itemOptionsActivity;
import com.example.bidingapplication.messaging.myMessagesActivity;
import com.example.bidingapplication.objects.User;
import com.example.bidingapplication.objects.item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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
    private DatabaseReference databaseReference2;
    private MyAdapterMyItems myAdapterMyItems;
    private ArrayList<item> items;
    private User currentUser;
    private FirebaseAuth auth;
    private FloatingActionButton myMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        configWidget();
        getIncomingIntent();

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


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),configProfileActivity.class);
                intent.putExtra("username",UserName);
                intent.putExtra("imageUrl",UserPicture);
                intent.putExtra("email",UserEmail);
                intent.putExtra("password",UserPassword);
                intent.putExtra("id",UserId);
                startActivity(intent);
            }
        });

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    if(snapshot1.getKey().equals(auth.getCurrentUser().getUid())){
                        currentUser = snapshot1.getValue(User.class);
                        Glide.with(getApplicationContext()).load(currentUser.getImageUrl()).into(profileImage);
                        return;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        ratingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),myRatingsActivity.class));
            }
        });
        myMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), myMessagesActivity.class));

            }
        });

    }
    private void getIncomingIntent() {
        if(getIntent().hasExtra("username")&&getIntent().hasExtra("id")&&getIntent().hasExtra("email")&&getIntent().hasExtra("password")&&getIntent().hasExtra("imageUrl")){
            UserName = getIntent().getStringExtra("username");
            UserId = getIntent().getStringExtra("id");
            UserEmail = getIntent().getStringExtra("email");
            UserPassword = getIntent().getStringExtra("password");
            UserPicture = getIntent().getStringExtra("imageUrl");
        }
    }
    private void configWidget() {
        settings = findViewById(R.id.opetionsButton);
        ratingsButton = findViewById(R.id.ratingsButton);
        profileImage = findViewById(R.id.profileImageOptions);
        userName = findViewById(R.id.optionsUsername);
        recyclerView = findViewById(R.id.myItemsRecView);
        myMessages = findViewById(R.id.myMessages);
        items= new ArrayList<>();
        userName.setText(UserName);
        Glide.with(getApplicationContext()).load(UserPicture).into(profileImage);
        databaseReference = FirebaseDatabase.getInstance().getReference("Items");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapterMyItems = new MyAdapterMyItems(items,getApplicationContext(),this);
        recyclerView.setAdapter(myAdapterMyItems);
        auth = FirebaseAuth.getInstance();
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Users");
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