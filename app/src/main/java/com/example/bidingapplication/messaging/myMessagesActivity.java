package com.example.bidingapplication.messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.bidingapplication.R;
import com.example.bidingapplication.adapters.MyAdapterUsers;
import com.example.bidingapplication.objects.User;
import com.example.bidingapplication.objects.message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class myMessagesActivity extends AppCompatActivity implements MyAdapterUsers.touchListener {
    private RecyclerView recyclerView;
    private MyAdapterUsers myAdapterUsers;
    private DatabaseReference databaseReference;
    private DatabaseReference messageReference;
    private ArrayList<User> users;
    private FirebaseAuth auth;
    private ArrayList<String> usersIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_messages);
        setUpStuff();
        //getMyMessagesfromFB
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    User user = snapshot1.getValue(User.class);
                    messageReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                message Message = dataSnapshot.getValue(message.class);
                                if(Message.getReciverId().equals(auth.getCurrentUser().getUid())&&Message.getSenderId().equals(user.getId())||
                                        Message.getReciverId().equals(user.getId())&&Message.getSenderId().equals(auth.getCurrentUser().getUid())
                                ) {
                                    if(user.getId().equals(auth.getCurrentUser().getUid())){

                                    }
                                    else {
                                        if(usersIds.contains(user.getId()))break;
                                        users.add(user);
                                        usersIds.add(user.getId());
                                        break;
                                    }
                                }
                            }
                            myAdapterUsers.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                myAdapterUsers.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });




    }

    private void setUpStuff() {
        recyclerView = findViewById(R.id.myMessRecView);
        users = new ArrayList<>();
        usersIds=new ArrayList<>();
        myAdapterUsers = new MyAdapterUsers(users,getApplicationContext(), this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapterUsers);
        auth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        messageReference = FirebaseDatabase.getInstance().getReference("Messages");

    }

    @Override
    public void onNoteClick(int position) {

        Intent intent = new Intent(getApplicationContext(),messagingActivity.class);
        intent.putExtra("id",users.get(position).getId());
        startActivity(intent);
    }
}