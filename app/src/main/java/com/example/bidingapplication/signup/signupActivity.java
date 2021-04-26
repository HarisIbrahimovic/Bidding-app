package com.example.bidingapplication.signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bidingapplication.MainActivity;
import com.example.bidingapplication.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class signupActivity extends AppCompatActivity {
    private EditText email,password,username;
    private Button signUpButton;
    private TextView text;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private ImageView profileImage;
    private Uri imageUri;
    private String imageUrl;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email = findViewById(R.id.signuEmail);
        password = findViewById(R.id.signupPassword);
        signUpButton = findViewById(R.id.signUpButton);
        text = findViewById(R.id.signUpText);
        username = findViewById(R.id.signUpUsername);
        profileImage = findViewById(R.id.profilePic);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference =firebaseStorage.getReference();
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosePicture();
            }
        });
        progressDialog = new ProgressDialog(this);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),loginActivity.class));
                finish();
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Processing..");
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                String name = username.getText().toString();
                if(TextUtils.isEmpty(Email)||TextUtils.isEmpty(Password)||TextUtils.isEmpty(name)){
                    Toast.makeText(getApplicationContext(),"Fill in the fields.",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseAuth auth = FirebaseAuth.getInstance();

                            HashMap<String, String> newUser = new HashMap<>();
                            newUser.put("username",name);
                            newUser.put("email",Email);
                            newUser.put("password",Password);
                            newUser.put("imageUrl",imageUrl);
                            databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                            databaseReference.child(auth.getCurrentUser().getUid()).setValue(newUser);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            progressDialog.dismiss();
                        finish();}else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Problems",Toast.LENGTH_SHORT).show();
                        }}
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
            profileImage.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        final String randomKey = UUID.randomUUID().toString();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading..");

        StorageReference ref = storageReference.child("images/"+randomKey);
        imageUrl = imageUri.toString();
        progressDialog.show();
        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Done upload..",Toast.LENGTH_SHORT).show();
            }
        });
    }
}