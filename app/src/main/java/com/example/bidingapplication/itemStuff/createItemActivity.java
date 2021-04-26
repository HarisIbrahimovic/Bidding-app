package com.example.bidingapplication.itemStuff;

import androidx.annotation.NonNull;
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

import com.example.bidingapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class createItemActivity extends AppCompatActivity {
    private EditText itemName;
    private EditText itemDesc;
    private Button commit;
    private EditText startPrice;
    private ImageView itemImage;
    private Uri imageUri;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String imageUrl;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialogOne;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        itemName = findViewById(R.id.createItemName);
        commit = findViewById(R.id.createItemButton);
        startPrice = findViewById(R.id.createItemPrice);
        itemImage = findViewById(R.id.createItemImage);
        itemDesc = findViewById(R.id.createItemDesc);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference =firebaseStorage.getReference();
        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosePicture();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = itemName.getText().toString();
                String desc = itemDesc.getText().toString();
                String price = startPrice.getText().toString();
                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(desc)||TextUtils.isEmpty(price)){
                    Toast.makeText(getApplicationContext(), "Fill in the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(imageUrl==null){
                    Toast.makeText(getApplicationContext(),"Select picture",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialogOne = new ProgressDialog(getApplicationContext());
                progressDialogOne.setMessage("Processing..");
                databaseReference = FirebaseDatabase.getInstance().getReference("Items");
                HashMap<String, String> item = new HashMap<>();
                item.put("name",name);
                item.put("desc",desc);
                item.put("price",price);
                item.put("imageUrl",imageUrl);
                databaseReference.push().setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Succesfully added", Toast.LENGTH_SHORT).show();
                            progressDialogOne.dismiss();
                            finish();
                        }
                        else Toast.makeText(getApplicationContext(), "Problems..", Toast.LENGTH_SHORT).show();
                        progressDialogOne.dismiss();
                    }
                });
            }
        });
    }
    private void chosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 &&resultCode ==RESULT_OK && data!=null &&data.getData()!=null){
            imageUri = data.getData();
            itemImage.setImageURI(imageUri);
            uploadPicture();
        }
    }
    private void uploadPicture() {
        final String randomKey = UUID.randomUUID().toString();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading..");
        progressDialog.show();
        StorageReference ref = storageReference.child("images/"+randomKey);
        imageUrl = ref.toString();
        Toast.makeText(getApplicationContext(),ref.toString(),Toast.LENGTH_LONG).show();
        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Done upload..",Toast.LENGTH_SHORT).show();
            }
        });
    }
}