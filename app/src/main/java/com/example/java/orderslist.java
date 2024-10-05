package com.example.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.java.recyculer.Allordersuser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import Tempadmin.AllOrderstempadmin;

public class orderslist extends AppCompatActivity {
    private ConstraintLayout c1,c2,c3,c4;
    private ImageButton c;
    private FirebaseUser user;
    private ArrayList<String> admins=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderslist);
        c1=findViewById(R.id.xeroxorders);
        c4=findViewById(R.id.productorders);
        c2=findViewById(R.id.stationryorders);
        c=findViewById(R.id.backbtnordersuser11);

        c3=findViewById(R.id.bookorders);
        user= FirebaseAuth.getInstance().getCurrentUser();
        admins.add("abcd1234@gmail.com");
        admins.add("saleem1712005@gmail.com");
        admins.add("jayaraman00143@gmail.com");
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(admins.contains(user.getEmail())){
                    startActivity(new Intent(orderslist.this,productsordersactivityadmin.class));

                }else {
                    startActivity(new Intent(orderslist.this,Productordersactivity.class));
                }
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(admins.contains(user.getEmail())){
                    startActivity(new Intent(orderslist.this,bookordersactivityadmin.class));

                }else {
                    startActivity(new Intent(orderslist.this,Bookordersactivity.class));
                }
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(admins.contains(user.getEmail())){
                    startActivity(new Intent(orderslist.this,ordeersactivityadmin.class));

                }else {
                    startActivity(new Intent(orderslist.this,OrdersActivity.class));
                }

            }
        });

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(admins.contains(user.getEmail())){
                    startActivity(new Intent(orderslist.this, AllOrderstempadmin.class));

                }else {
                    startActivity(new Intent(orderslist.this, Allordersuser.class));
                }
            }
        });
    }
}