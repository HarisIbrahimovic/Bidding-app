package com.example.bidingapplication.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bidingapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ratingsActivity extends AppCompatActivity {
    private FloatingActionButton one,two,three,four,five;
    private Button submitButton;
    private EditText description;
    private int ratingScore;
    private String id;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);
        configWidgets();
        getIncomingIntent();
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingScore=1;
                Toast.makeText(getApplicationContext(),"Score: "+ratingScore+"/5",Toast.LENGTH_SHORT).show();
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingScore=2;
                Toast.makeText(getApplicationContext(),"Score: "+ratingScore+"/5",Toast.LENGTH_SHORT).show();
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingScore=3;
                Toast.makeText(getApplicationContext(),"Score: "+ratingScore+"/5",Toast.LENGTH_SHORT).show();
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingScore=4;
                Toast.makeText(getApplicationContext(),"Score: "+ratingScore+"/5",Toast.LENGTH_SHORT).show();
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingScore=5;
                Toast.makeText(getApplicationContext(),"Score: "+ratingScore+"/5",Toast.LENGTH_SHORT).show();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = description.getText().toString();
                if(TextUtils.isEmpty(desc)){
                    Toast.makeText(getApplicationContext(), "Add description.", Toast.LENGTH_SHORT).show();
                    return;
                }
                HashMap<String, String> newRating = new HashMap<>();
                newRating.put("stars",String.valueOf(ratingScore));
                newRating.put("desc",desc);
                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(id).child("Ratings");
                databaseReference.push().setValue(newRating);

            }
        });
    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("id"))id = getIntent().getStringExtra("id");
    }

    private void configWidgets() {
        one= findViewById(R.id.starOne);
        two = findViewById(R.id.starTwo);
        three = findViewById(R.id.starThree);
        four = findViewById(R.id.starFour);
        five = findViewById(R.id.starFive);
        submitButton = findViewById(R.id.submitRating);
        description = findViewById(R.id.ratingDescription);
    }
}