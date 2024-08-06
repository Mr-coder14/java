package com.example.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class oderspreviewadmin extends AppCompatActivity {
    private TextView username,phno,total,orderid,date,ntotes,total1;
    private ImageView orderStatusImage, deliveredStatusImage;
    private RecyclerView productRecyclerView;
    private ImageButton backButton;
    private boolean delivered=false;
    private DatabaseReference databaseReference,newchild;
    private Button markasdelivered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oderspreviewadmin);
        username=findViewById(R.id.orderedusername1);
        phno=findViewById(R.id.ordereduserphno1);
        total=findViewById(R.id.totalpriceorderpreview1);
        orderid=findViewById(R.id.orderidorderpreview1);
        date=findViewById(R.id.orderdateorderpreview1);
        ntotes=findViewById(R.id.notesorderpreview1);
        total1=findViewById(R.id.totalorderpreview11);
        backButton = findViewById(R.id.backbtnorderpreview1);
        orderStatusImage = findViewById(R.id.handleimageorderorderpreview1);
        deliveredStatusImage = findViewById(R.id.handleimagedeliveredorderpreview1);
        productRecyclerView = findViewById(R.id.recyculervieworderpreview1);
        markasdelivered=findViewById(R.id.markasdelivered);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("userorders");
        newchild = FirebaseDatabase.getInstance().getReference().child("deliveredordersadmin");
        Order order = getIntent().getParcelableExtra("order");
        if (order != null) {
            displayOrderDetails(order);
        } else {

            Toast.makeText(this, "Error: Order details not found", Toast.LENGTH_SHORT).show();
            finish();
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        markasdelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(order!=null){
                    delivered=true;
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                                    if (orderSnapshot.getKey().equals(order.getOrderId())) {
                                        databaseReference.child(userSnapshot.getKey()).child(order.getOrderId()).child("delivered").setValue(delivered);

                                        newchild.child(order.getOrderId()).setValue(orderSnapshot.getValue()).addOnSuccessListener(aVoid -> {

                                            startActivity(new Intent(oderspreviewadmin.this, Adminactivity.class));
                                            finishAffinity();
                                        }).addOnFailureListener(e -> {

                                        });

                                        return;




                                    }}}
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
            }
        });


    }

    private void displayOrderDetails(Order order) {
        if (order!=null) {
            username.setText(order.getUsername());
            phno.setText(order.getPhno());
            total.setText(order.getOrderTotal());
            orderid.setText(order.getOrderId());
            date.setText(formatDate(order.getOrderTimestamp()));
            String notes = order.getNotes();
            ntotes.setText(notes != null && !notes.isEmpty() ? notes : "No Notes");
            total1.setText("Total: " + order.getOrderTotal());

            ProductAdapter productAdapter = new ProductAdapter(order.getProducts(),this);
            productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            productRecyclerView.setAdapter(productAdapter);
        }else {
            Toast.makeText(this, "Error: Order details not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}