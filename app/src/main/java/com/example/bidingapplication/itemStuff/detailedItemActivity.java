package com.example.bidingapplication.itemStuff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.bidingapplication.R;
import com.example.bidingapplication.objects.item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.ALL;

public class detailedItemActivity extends AppCompatActivity {
    private String incomingName, incomingPrice, incomingDesc, incomingUsername, incomingPicture, incomingId,incomingCurrentUsername;
    private double price;
    private EditText newPrice;
    private TextView description;
    private TextView itemname;
    private TextView itemOwner;
    private TextView itemPrice;
    private Button submit;
    private ImageView itemImage;
    private item curentItem;
    private TextView currentBestBid;
    private DatabaseReference databaseReference;
    private String currentBestBidString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_item);
        getIntentConfig();
        configWidgets();
        getCurrentItem();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPriceInput = newPrice.getText().toString();
                if(TextUtils.isEmpty(newPriceInput)){
                    Toast.makeText(getApplicationContext(), "Enter price", Toast.LENGTH_SHORT).show();
                    return;
                }
                double checkPrice = Double.parseDouble(newPriceInput);
                if(checkPrice<price){
                    Toast.makeText(getApplicationContext(),"You cannot bid with lower price.",Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseReference changeItem = FirebaseDatabase.getInstance().getReference("Items").child(incomingId);
                changeItem.child("price").setValue(newPriceInput);
                changeItem.child("bestBid").setValue(incomingCurrentUsername);
                Toast.makeText(getApplicationContext(), "Bid placed", Toast.LENGTH_SHORT).show();
                currentBestBid.setText("Current best bid: "+ incomingCurrentUsername);
                itemPrice.setText(newPriceInput);
            }
        });
    }
    private void getCurrentItem() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Items");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    if(snapshot1.getKey().equals(incomingId)){
                        curentItem = snapshot1.getValue(item.class);
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
        newPrice = findViewById(R.id.newBid);
        submit = findViewById(R.id.submitNewPrice);
        description = findViewById(R.id.detailItemDescription);
        itemname = findViewById(R.id.detailedItemName);
        itemOwner = findViewById(R.id.detailedItemUserName);
        itemImage = findViewById(R.id.detailedItemImage);
        itemPrice = findViewById(R.id.currentPrice);
        itemImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Glide.with(getApplicationContext()).
        load(incomingPicture).into(itemImage);
        description.setText(incomingDesc);
        itemname.setText(incomingName);
        itemOwner.setText(incomingUsername);
        itemPrice.setText(incomingPrice);
        currentBestBid = findViewById(R.id.currentBestBid);
        currentBestBid.setText("Current best bid: "+ currentBestBidString);
    }
    private void getIntentConfig() {
        if(getIntent().hasExtra("name")&&getIntent().hasExtra("desc")&&getIntent().hasExtra("imageUrl")&&getIntent().hasExtra("currentUserName")
                &&getIntent().hasExtra("username")&&getIntent().hasExtra("price")&&getIntent().hasExtra("id")&&getIntent().hasExtra("curentBestBid")){
            incomingName=getIntent().getStringExtra("name");
            incomingDesc=getIntent().getStringExtra("desc");
            incomingPicture = getIntent().getStringExtra("imageUrl");
            incomingUsername = getIntent().getStringExtra("username");
            incomingPrice = getIntent().getStringExtra("price");
            incomingCurrentUsername = getIntent().getStringExtra("currentUserName");
            incomingId = getIntent().getStringExtra("id");
            price = Double.parseDouble(incomingPrice);
            currentBestBidString = getIntent().getStringExtra("curentBestBid");
        }
    }
}