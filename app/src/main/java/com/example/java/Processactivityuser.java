package com.example.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Tempadmin.Processorderactivity;

public class Processactivityuser extends AppCompatActivity {
    private TextView gt;
    private String orderid,grandtotal;
    private List<Fileinmodel> fileinmodels;
    private boolean delivered;
    private ImageButton backbtn;
    DatabaseReference databaseReference,newchild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processactivityuser);

        backbtn=findViewById(R.id.back_btnadmin121);
        gt=findViewById(R.id.gtt1);
        fileinmodels=new ArrayList<>();
        orderid=getIntent().getStringExtra("orderid2");
        grandtotal=getIntent().getStringExtra("gt2");
        gt.setText("₹ "+grandtotal);


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    }
