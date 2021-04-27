package com.example.bidingapplication.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bidingapplication.R;
import com.example.bidingapplication.objects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class lookOtherProfileActivity extends AppCompatActivity {
    private String id;
    private User user;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private ImageView image;
    private TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_other_profile);
        getIncomingIntent();
        configWidgets();
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
    }

    private void configWidgets() {
        image = findViewById(R.id.personPicture);
        username = findViewById(R.id.personUsername);
    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("id")){
            id = getIntent().getStringExtra("id");
        }
    }
}