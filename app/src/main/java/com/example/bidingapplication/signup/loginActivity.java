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

public class loginActivity extends AppCompatActivity {
    private EditText email,password;
    private Button loginButton;
    private TextView text;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configWidgets();
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),signupActivity.class));
                finish();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }


    private void loginUser() {
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        if(TextUtils.isEmpty(Email)||TextUtils.isEmpty(Password)){
            Toast.makeText(getApplicationContext(),"Fill in the fields.",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Welcome",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    progressDialog.dismiss();
                    finish();}
                else{
                    Toast.makeText(getApplicationContext(),"Problems..",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void configWidgets() {
        email= findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        text = findViewById(R.id.loginText);
        progressDialog = new ProgressDialog(this);
    }
}