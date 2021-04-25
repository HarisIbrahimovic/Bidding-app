package com.example.bidingapplication.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bidingapplication.MainActivity;
import com.example.bidingapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signupActivity extends AppCompatActivity {
    private EditText email,password;
    private Button signUpButton;
    private TextView text;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email = findViewById(R.id.signuEmail);
        password = findViewById(R.id.signupPassword);
        signUpButton = findViewById(R.id.signUpButton);
        text = findViewById(R.id.signUpText);
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
                if(TextUtils.isEmpty(Email)||TextUtils.isEmpty(Password)){
                    Toast.makeText(getApplicationContext(),"Fill in the fields.",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        progressDialog.dismiss();
                        finish();}else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Problems",Toast.LENGTH_SHORT).show();
                        }}                });
            }
        });
    }
}