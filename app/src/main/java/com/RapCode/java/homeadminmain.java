package com.RapCode.java;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.RapCode.java.recyculer.OrderAdaptormadmin;
import com.RapCode.java.recyculer.ProductDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class homeadminmain extends Fragment {
    private static final String TAG = "homeadmin";

    private RecyclerView recyclerView;
    FirebaseAuth auth;
    private OrderAdaptormadmin adapter;
    private List<Order> fullOrderList;
    private List<Order> orderList;
    FirebaseUser user;
    private EditText editText;
    private DatabaseReference databaseReference;
    private Query query;
    private ProgressBar progressBar;
    private TextView emptyOrdersText;
    private Handler debounceHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homeadminmain, container, false);

        databaseReference =  FirebaseDatabase.getInstance().getReference("userorders");
        recyclerView = view.findViewById(R.id.recyclerhomeadmin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        progressBar = view.findViewById(R.id.progressbarhomeadmin);
        emptyOrdersText = view.findViewById(R.id.emptyOrdersTextm);
        debounceHandler.removeCallbacks(searchRunnable);
        progressBar.setVisibility(View.VISIBLE);
        editText=view.findViewById(R.id.search_edit_textadmin);
        fullOrderList=new ArrayList<>();


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        query = databaseReference.orderByChild("timestamp");
        orderList = new ArrayList<>();
        adapter = new OrderAdaptormadmin(orderList,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);





        Drawable drawable = ContextCompat.getDrawable(getContext(), com.android.car.ui.R.drawable.car_ui_icon_search);
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 31, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 31, getResources().getDisplayMetrics());
        drawable.setBounds(0, 0, width, height);
        editText.setCompoundDrawables(drawable, null, null, null);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                debounceHandler.removeCallbacks(searchRunnable);


                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        String searchText = charSequence.toString().trim().toLowerCase();
                        filterOrders(searchText);
                    }
                };

                debounceHandler.postDelayed(searchRunnable, 300);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        fetchOrders();

        return view;
    }
    private void filterOrders(String searchText) {
        List<Order> filteredList = new ArrayList<>();

        for (Order order : orderList) {
            if (order.getOrderId().toLowerCase().contains(searchText)) {
                filteredList.add(order);
            }
        }

        adapter.updateList(filteredList);

        if (filteredList.isEmpty()) {
            emptyOrdersText.setVisibility(View.VISIBLE);
            emptyOrdersText.setText("No matching orders found");
        } else {
            emptyOrdersText.setVisibility(View.GONE);
        }
    }
    private void fetchOrders() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for(DataSnapshot ui : dataSnapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : ui.getChildren()) {
                        Boolean delivered = orderSnapshot.child("delivered").getValue(Boolean.class);


                        if (delivered != null && !delivered) {
                            String orderId = orderSnapshot.getKey();
                            String orderTotal = orderSnapshot.child("orderTotal").getValue(String.class);
                            Long orderTimestamp = orderSnapshot.child("orderTimestamp").getValue(Long.class);
                            String username = orderSnapshot.child("username").getValue(String.class);
                            String phno = orderSnapshot.child("phno").getValue(String.class);
                            String notes = orderSnapshot.child("notes").getValue(String.class);
                            Boolean ordered = orderSnapshot.child("odered").getValue(Boolean.class);
                            String add=orderSnapshot.child("address").getValue(String.class);
                            List<ProductDetails> products = new ArrayList<>();
                            for (DataSnapshot productSnapshot : orderSnapshot.getChildren()) {
                                if (!productSnapshot.getKey().equals("orderTotal") &&
                                        !productSnapshot.getKey().equals("orderTimestamp") &&
                                        !productSnapshot.getKey().equals("username") &&
                                        !productSnapshot.getKey().equals("phno") &&
                                        !productSnapshot.getKey().equals("notes") &&
                                        !productSnapshot.getKey().equals("address") &&
                                        !productSnapshot.getKey().equals("odered") &&
                                        !productSnapshot.getKey().equals("delivered")) {
                                    ProductDetails product = productSnapshot.getValue(ProductDetails.class);
                                    if (product != null) {
                                        products.add(product);
                                    }
                                }
                            }

                            Order order = new Order(orderId, orderTotal, orderTimestamp, products, username, phno, notes, ordered, delivered,add);
                            orderList.add(order);
                            fullOrderList.add(order);
                        }
                    }
                }
                orderList = new ArrayList<>(fullOrderList); // Create a copy for the adapter
                adapter.updateList(orderList);
                progressBar.setVisibility(View.GONE);

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
                Toast.makeText(getContext(), "Failed to load orders", Toast.LENGTH_SHORT).show();
            }
        });
    }


    }



