package com.example.bidingapplication.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bidingapplication.R;
import com.example.bidingapplication.adapters.MyAdapterItems;
import com.example.bidingapplication.adapters.MyAdapterMyItems;
import com.example.bidingapplication.itemStuff.detailedItemActivity;
import com.example.bidingapplication.messaging.messagingActivity;
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

public class lookOtherProfileActivity extends AppCompatActivity implements MyAdapterItems.onNoteListener {
    private String id;
    private User user;
    private DatabaseReference databaseReference;
    private DatabaseReference findItems;
    private ArrayList<item> items;
    private ImageView image;
    private TextView username;
    private FloatingActionButton ratePersonButton, messagesButton;
    private RecyclerView personItemsRec;
    private TextView personRatings;
    private MyAdapterItems myAdapterItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_other_profile);
        getIncomingIntent();
        configWidgets();
        items = new ArrayList<>();
        personItemsRec.setHasFixedSize(true);
        personItemsRec.setLayoutManager(new LinearLayoutManager(this));
        myAdapterItems = new MyAdapterItems(items,getApplicationContext(),this);
        personItemsRec.setAdapter(myAdapterItems);
        findItems = FirebaseDatabase.getInstance().getReference("Items");
        findItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    item Item = snapshot1.getValue(item.class);
                    if(Item.getOwnerId().equals(id)){
                        items.add(Item);
                    }
                    myAdapterItems.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    user = snapshot1.getValue(User.class);
                    if(user.getId().equals(id)){
                        Glide.with(getApplicationContext()).load(user.getImageUrl()).into(image);
                        username.setText(user.getUsername());
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        personRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),seeOthersRaitingActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);

            }
        });

        ratePersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ratingsActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), messagingActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
    }

    private void configWidgets() {
        image = findViewById(R.id.personPicture);
        username = findViewById(R.id.personUsername);
        personRatings = findViewById(R.id.personRatings);
        ratePersonButton = findViewById(R.id.ratePerson);
        personItemsRec = findViewById(R.id.personItemsRec);
        messagesButton = findViewById(R.id.messagePerson);
    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("id")){
            id = getIntent().getStringExtra("id");
        }
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(getApplicationContext(), detailedItemActivity.class);
        intent.putExtra("name",items.get(position).getName());
        intent.putExtra("desc",items.get(position).getDesc());
        intent.putExtra("imageUrl",items.get(position).getImageUrl());
        intent.putExtra("username",items.get(position).getUsername());
        intent.putExtra("price",items.get(position).getPrice());
        intent.putExtra("id",items.get(position).getId());
        intent.putExtra("currentUserName","test");
        intent.putExtra("curentBestBid",items.get(position).getBestBid());
        intent.putExtra("ownerId",items.get(position).getOwnerId());
        startActivity(intent);

    }
}