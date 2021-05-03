package com.example.bidingapplication.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bidingapplication.R;
import com.example.bidingapplication.signup.loginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class configProfileActivity extends AppCompatActivity {

    private String UserName;
    private String UserId;
    private String UserEmail;
    private String UserPassword;
    private String UserPicture;
    private ImageView image;
    private Button updateButton;
    private EditText name, email, password;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private Uri imageUri;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String newImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_profile);
        getIncomingIntent();
        configWidgets();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosePicture();
            }
        });
    }

    private void updateUser() {
        String newEmail = email.getText().toString();
        String newName = name.getText().toString();
        String newPass = password.getText().toString();
        if(TextUtils.isEmpty(newEmail)||TextUtils.isEmpty(newPass)||TextUtils.isEmpty(newName)){
            Toast.makeText(getApplicationContext(),"Fill in the fields",Toast.LENGTH_SHORT).show();
            return;
        }
        databaseReference.child(auth.getCurrentUser().getUid()).child("email").setValue(newEmail);
        databaseReference.child(auth.getCurrentUser().getUid()).child("password").setValue(newPass);
        databaseReference.child(auth.getCurrentUser().getUid()).child("username").setValue(newName);
        databaseReference.child(auth.getCurrentUser().getUid()).child("imageUrl").setValue(newImageUrl);
        auth.getCurrentUser().updateEmail(newEmail);
        auth.getCurrentUser().updatePassword(newPass);
        Toast.makeText(getApplicationContext(),"Succesfully updated..",Toast.LENGTH_SHORT).show();
    }

    private void configWidgets() {
        updateButton = findViewById(R.id.configUpdateButton);
        name = findViewById(R.id.configUserName);
        email = findViewById(R.id.configUserEmail);
        password = findViewById(R.id.configUserPass);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference =firebaseStorage.getReference();
        name.setText(UserName);
        email.setText(UserEmail);
        password.setText(UserPassword);
        image = findViewById(R.id.configProfilePicture);
        Glide.with(getApplicationContext()).load(UserPicture).into(image);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
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
            image.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        final String randomKey = UUID.randomUUID().toString();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading..");

        StorageReference ref = storageReference.child("images/"+randomKey);

        progressDialog.show();
        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Done upload..",Toast.LENGTH_SHORT).show();
                Glide.with(getApplicationContext()).load(newImageUrl).into(image);
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        newImageUrl = uri.toString();
                    }
                });
            }
        });
    }
}