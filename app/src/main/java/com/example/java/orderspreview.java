package com.example.java;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class orderspreview extends AppCompatActivity {
    private TextView username,phno,total,orderid,date,ntotes,total1;
    private ImageView orderStatusImage, deliveredStatusImage;
    private RecyclerView productRecyclerView;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderspreview);
        username=findViewById(R.id.orderedusername);
        phno=findViewById(R.id.ordereduserphno);
        total=findViewById(R.id.totalpriceorderpreview);
        orderid=findViewById(R.id.orderidorderpreview);
        date=findViewById(R.id.orderdateorderpreview);
        ntotes=findViewById(R.id.notesorderpreview);
        total1=findViewById(R.id.totalorderpreview1);
        backButton = findViewById(R.id.backbtnorderpreview);
        orderStatusImage = findViewById(R.id.handleimageorderorderpreview);
        deliveredStatusImage = findViewById(R.id.handleimagedeliveredorderpreview);
        productRecyclerView = findViewById(R.id.recyculervieworderpreview);

        Order order = getIntent().getParcelableExtra("order");
        if (order != null) {
            displayOrderDetails(order);
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    private void displayOrderDetails(Order order) {
        username.setText(order.getUsername());
        phno.setText(order.getPhno());
        total.setText(order.getOrderTotal());
        orderid.setText(order.getOrderId());
        date.setText(formatDate(order.getOrderTimestamp()));
        ntotes.setText(order.getNotes().isEmpty() ? "No Notes" : order.getNotes());
        total1.setText("Total: " + order.getOrderTotal());

        ProductAdapter productAdapter = new ProductAdapter(order.getProducts());
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setAdapter(productAdapter);



    }
    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

}