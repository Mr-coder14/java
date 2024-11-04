package com.RapCode.java;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Paymentactivity extends AppCompatActivity {
    private TextView textView,address;
    private Orderconfirmuseractivity orderconfirmuseractivity;
    private DatabaseReference usersRef;
    private FirebaseUser user;
    private Button btn;
    private ImageButton btnw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentactivity);
        address=findViewById(R.id.address);
        textView=findViewById(R.id.grandamty);
        btnw=findViewById(R.id.backbtnpayment);
        user= FirebaseAuth.getInstance().getCurrentUser();
        Float t=getIntent().getExtras().getFloat("tt");
        orderconfirmuseractivity=new Orderconfirmuseractivity();
        btn=findViewById(R.id.orderbtnuser124);
        textView.setText("₹ "+String.valueOf(t));
        btnw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                update(String.valueOf(s));

            }
        });
        usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User userData = dataSnapshot.getValue(User.class);
                    if(userData.getAddress()!=null){
                        address.setText(userData.getAddress());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newAddress = address.getText().toString().trim();
                updateAddressInDatabase(newAddress);
                startActivity(new Intent(Paymentactivity.this,suceesanimation.class));
                finish();
            }
        });

    }

    public void update(String s){

    }
    private void updateAddressInDatabase(String newAddress) {
        if (newAddress.isEmpty()) {
            address.setError("Address cannot be empty");
            address.requestFocus();
            return;
        }

        // Update the address in the Firebase Database
        usersRef.child("address").setValue(newAddress).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Address updated successfully
                // You can show a Toast message here if needed
            } else {
                // Failed to update address
                // You can handle the error here
            }
        });
    }



}