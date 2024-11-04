package com.RapCode.java;

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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class orderspreview extends AppCompatActivity {
    private TextView username,phno,total,orderid,date,ntotes,total1,gh;
    private ImageView orderStatusImage, deliveredStatusImage;
    private RecyclerView productRecyclerView;
    private ImageButton backButton;
    private Button review;
    private Boolean delivered;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderspreview);
        username=findViewById(R.id.orderedusername);
        gh=findViewById(R.id.deliveryby);
        phno=findViewById(R.id.ordereduserphno);
        total=findViewById(R.id.totalpriceorderpreview);
        orderid=findViewById(R.id.orderidorderpreview);
        date=findViewById(R.id.orderdateorderpreview);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("userorders");
        ntotes=findViewById(R.id.notesorderpreview);
        total1=findViewById(R.id.totalorderpreview1);
        backButton = findViewById(R.id.backbtnorderpreview);
        review=findViewById(R.id.reviewform);
        orderStatusImage = findViewById(R.id.handleimageorderorderpreview);
        deliveredStatusImage = findViewById(R.id.handleimagedeliveredorderpreview);
        productRecyclerView = findViewById(R.id.recyculervieworderpreview);


        Order order = getIntent().getParcelableExtra("order");
        if (order != null) {
            displayOrderDetails(order);
        } else {

            Toast.makeText(this, "Error: Order details not found", Toast.LENGTH_SHORT).show();
            finish();
        }
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(orderspreview.this, ReviewForm.class);
                intent.putExtra("order", order);
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        for(DataSnapshot qw:snapshot1.getChildren()){
                            if(qw.getKey().equals(order.getOrderId())){
                                delivered=qw.child("delivered").getValue(Boolean.class);
                                if (delivered != null && delivered) {
                                        deliveredStatusImage.setImageResource(R.drawable.tick);
                                    }

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(order.getOrderTimestamp());

            calendar.add(Calendar.DAY_OF_MONTH, 1);


            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String newDate = dateFormat.format(calendar.getTime());
            gh.setText(newDate);
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