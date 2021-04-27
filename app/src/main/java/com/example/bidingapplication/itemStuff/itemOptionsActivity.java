package com.example.bidingapplication.itemStuff;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bidingapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.nio.channels.Pipe;
import java.util.HashMap;
import java.util.UUID;

public class itemOptionsActivity extends AppCompatActivity {
    private String Name, Id, Desc, Price, ImageUrl;
    private EditText name, desc,  price;
    private ImageView image;
    private Button deleteButton, updateButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_options);
        getIncomingIntent();
        configWidgets();
        databaseReference = FirebaseDatabase.getInstance().getReference("Items").child(Id);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = name.getText().toString();
                String newDesc = desc.getText().toString();
                String newPrice= price.getText().toString();
                if(TextUtils.isEmpty(newName)||TextUtils.isEmpty(newDesc)||TextUtils.isEmpty(newPrice)){
                    Toast.makeText(getApplicationContext(),"Fill in the fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                databaseReference.child("name").setValue(newName);
                databaseReference.child("desc").setValue(newDesc);
                databaseReference.child("price").setValue(newPrice);
                Toast.makeText(getApplicationContext(),"Succesfully updated.",Toast.LENGTH_SHORT).show();
                }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.removeValue();
                Toast.makeText(getApplicationContext(), "Succesfully deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void configWidgets() {
        name = findViewById(R.id.upadteItemName);
        price = findViewById(R.id.updateItemPrice);
        image = findViewById(R.id.updateItemPicture);
        desc = findViewById(R.id.updateItemDesc);
        name.setText(Name);
        price.setText(Price);
        desc.setText(Desc);
        deleteButton = findViewById(R.id.deleteBUtton);
        updateButton = findViewById(R.id.updateButton);
        Glide.with(getApplicationContext()).load(ImageUrl).into(image);

    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("name")&&getIntent().hasExtra("id")&&getIntent().hasExtra("desc")&&getIntent().hasExtra("price")&&getIntent().hasExtra("imageUrl")){
            Name = getIntent().getStringExtra("name");
            Id = getIntent().getStringExtra("id");
            Desc = getIntent().getStringExtra("desc");
            Price = getIntent().getStringExtra("price");
            ImageUrl = getIntent().getStringExtra("imageUrl");
        }

    }



}