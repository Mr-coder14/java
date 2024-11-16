package com.RapCode.java;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.RapCode.java.recyculer.ProductDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ordeersactivityadmin extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<Order> orderList;
    private ProgressBar progressBar;
    private TextView emptyOrdersText;
    private DatabaseReference ordersRef;
    private ImageButton backtbn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordeersactivityadmin);
        recyclerView = findViewById(R.id.ordersRecyclerViews);
        progressBar = findViewById(R.id.ordersProgressBars);
        backtbn=findViewById(R.id.backbtnordersusers);
        emptyOrdersText = findViewById(R.id.emptyOrdersTexts);

        auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        ordersRef = FirebaseDatabase.getInstance().getReference("userorders");
        backtbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        orderList = new ArrayList<>();
        adapter = new OrderAdapter(orderList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchOrders();
    }
    private void fetchOrders() {
        progressBar.setVisibility(View.VISIBLE);
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();


                if (!dataSnapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    emptyOrdersText.setVisibility(View.VISIBLE);
                    return;
                }

                for (DataSnapshot ui : dataSnapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : ui.getChildren()) {
                        String orderId = orderSnapshot.getKey();
                        String orderTotal = orderSnapshot.child("orderTotal").getValue(String.class);
                        Long orderTimestamp = orderSnapshot.child("orderTimestamp").getValue(Long.class);
                        String username = orderSnapshot.child("username").getValue(String.class);
                        String phno = orderSnapshot.child("phno").getValue(String.class);
                        String notes = orderSnapshot.child("notes").getValue(String.class);
                        Boolean ordred = orderSnapshot.child("odered").getValue(Boolean.class);
                        Boolean delivered = orderSnapshot.child("delivered").getValue(Boolean.class);
                        String add = orderSnapshot.child("address").getValue(String.class);

                        List<ProductDetails> products = new ArrayList<>();
                        for (DataSnapshot productSnapshot : orderSnapshot.getChildren()) {
                            if (!productSnapshot.getKey().equals("orderTotal") &&
                                    !productSnapshot.getKey().equals("orderTimestamp") &&
                                    !productSnapshot.getKey().equals("username") &&
                                    !productSnapshot.getKey().equals("phno") &&
                                    !productSnapshot.getKey().equals("notes") &&
                                    !productSnapshot.getKey().equals("odered") &&
                                    !productSnapshot.getKey().equals("delivered") &&
                                    !productSnapshot.getKey().equals("address")) {
                                ProductDetails product = productSnapshot.getValue(ProductDetails.class);
                                products.add(product);
                            }
                        }

                        Order order = new Order(orderId, orderTotal, orderTimestamp, products, username, phno, notes, ordred, delivered, add);
                        orderList.add(order);
                    }
                }

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);


                if (orderList.isEmpty()) {
                    emptyOrdersText.setVisibility(View.VISIBLE);
                } else {
                    emptyOrdersText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ordeersactivityadmin.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

}