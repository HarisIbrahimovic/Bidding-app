package com.example.bidingapplication.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.number.NumberRangeFormatter;
import android.os.Bundle;
import android.widget.TextView;

import com.example.bidingapplication.R;
import com.example.bidingapplication.adapters.MyAdapterRating;
import com.example.bidingapplication.objects.rating;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class seeOthersRaitingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<rating> ratings;
    private MyAdapterRating myAdapterRating;
    private String id;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_others_raiting);
        getIncomingIntent();
        ratings = new ArrayList<>();
        recyclerView = findViewById(R.id.otherPersonRecView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapterRating = new MyAdapterRating(getApplicationContext(),ratings);
        recyclerView.setAdapter(myAdapterRating);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(id).child("Ratings");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rating Rating;
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Rating = snapshot1.getValue(rating.class);
                    ratings.add(Rating);
                }
                myAdapterRating.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("id")) {
            id = getIntent().getStringExtra("id");
        }
    }
}