package com.example.bidingapplication.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;

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

public class myRatingsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapterRating myAdapterRating;
    private ArrayList<rating> ratings;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ratings);
        ratings = new ArrayList<>();
        recyclerView = findViewById(R.id.myRatingsRecView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapterRating = new MyAdapterRating(getApplicationContext(),ratings);
        recyclerView.setAdapter(myAdapterRating);
        auth= FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid());
        databaseReference.child("Ratings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rating Raiting;
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Raiting = snapshot1.getValue(rating.class);
                    ratings.add(Raiting);
                }
                myAdapterRating.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}