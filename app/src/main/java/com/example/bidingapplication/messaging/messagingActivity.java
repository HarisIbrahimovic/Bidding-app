package com.example.bidingapplication.messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bidingapplication.R;
import com.example.bidingapplication.adapters.MyAdapterMessages;
import com.example.bidingapplication.objects.User;
import com.example.bidingapplication.objects.message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class messagingActivity extends AppCompatActivity {
    private String reciverId;
    private String senderId;
    private String content;
    private EditText messageContent;
    private DatabaseReference databaseReference;
    private DatabaseReference findUser;
    private TextView  personName;
    private ImageButton sendMessage;
    private ArrayList<message> messages;
    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private DatabaseReference addMessages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        getIncomingIntent();
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Messages");
        findUser = FirebaseDatabase.getInstance().getReference("Users");
        findUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user;
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    if(snapshot1.getKey().equals(reciverId)){
                        user = snapshot1.getValue(User.class);
                        personName.setText(user.getUsername());
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        addMessages = FirebaseDatabase.getInstance().getReference("Messages");
        messages = new ArrayList<>();
        sendMessage = findViewById(R.id.sendButton);
        personName = findViewById(R.id.messReciverName);
        messageContent = findViewById(R.id.textSend);
        recyclerView = findViewById(R.id.recyclerViewMess);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        MyAdapterMessages myAdapterMessages = new MyAdapterMessages(messages,this);

        recyclerView.setAdapter(myAdapterMessages);
        addMessages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    message Message = snapshot1.getValue(message.class);
                    if(Message.getReciverId().equals(reciverId)||Message.getSenderId().equals(reciverId)||Message.getReciverId().equals(senderId)||Message.getSenderId().equals(senderId)){
                        messages.add(Message);
                    }
                    myAdapterMessages.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = messageContent.getText().toString();
                if(TextUtils.isEmpty(content)||content.equals("")){
                    return;
                }
                senderId = auth.getCurrentUser().getUid();
                HashMap<String,String> newMess = new HashMap<>();
                newMess.put("reciverId",reciverId);
                newMess.put("senderId",senderId);
                newMess.put("content",content);
                databaseReference.push().setValue(newMess);
                messageContent.setText("");
            }
        });
    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("id"))reciverId=getIntent().getStringExtra("id");
    }
}