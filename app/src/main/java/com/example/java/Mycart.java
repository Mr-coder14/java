package com.example.java;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.recyculer.Itemlistadaptormycart;
import com.example.java.recyculer.ProductDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Mycart extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageButton back, deleteButton;
    private Itemlistadaptormycart adaptor;
    private ArrayList<ProductDetails> productDetailsList;
    private TextView emptymsg,subtotal, feedelivery, total;
    private DatabaseReference databaseReference,orderrefrence,userref;
    private ProgressBar progressBar;
    private Button btnq,addnotes;
    private FirebaseAuth auth;
    private String userid;
    private EditText notes;
    String note,username,phno;
    private int deliveryFee=15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycart);

        recyclerView = findViewById(R.id.recyculermycart);
        emptymsg = findViewById(R.id.empptytxt);
        subtotal = findViewById(R.id.subtotal);
        btnq=findViewById(R.id.checkoutbtn);
        feedelivery = findViewById(R.id.feedelivery);
        total = findViewById(R.id.total);
        addnotes=findViewById(R.id.addnotes);
        back = findViewById(R.id.backbtnmycart);
        notes=findViewById(R.id.notesmycart);
        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbarmycart);
        userid = auth.getCurrentUser().getUid();
        productDetailsList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                note=s.toString();

            }
        });

        addnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note=notes.getText().toString();
            }
        });


        if (userid != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("userscart").child(userid);
            orderrefrence=FirebaseDatabase.getInstance().getReference("userorders").child(userid);
            userref=FirebaseDatabase.getInstance().getReference().child("users").child(userid);
            fetchCartItems();
        }
        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userData = snapshot.getValue(User.class);
                if (userData != null) {
                    username=userData.getName();
                    phno=userData.getPhno();
                }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        deleteButton = findViewById(R.id.deletebuttonmycart);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adaptor.isSelectionMode) {
                    exitSelectionMode();
                } else {
                    finish();
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptor.deleteSelectedItems();
                exitSelectionMode();
                if (adaptor.getItemCount() == 0) {
                    updateEmptyCartMessage();
                }
            }
        });

        adaptor = new Itemlistadaptormycart(productDetailsList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);

        deleteButton.setVisibility(View.GONE);
    }
    private String generateOrderId() {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString().replaceAll("-", "");
        return uuidString.substring(0, Math.min(uuidString.length(), 10));
    }

    private void fetchCartItems() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productDetailsList.clear();
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ProductDetails productDetails = dataSnapshot.getValue(ProductDetails.class);
                        if (productDetails != null) {
                            productDetails.setKey(dataSnapshot.getKey());
                            productDetailsList.add(productDetails);
                        }
                    }
                }

                progressBar.setVisibility(View.GONE);
                adaptor.notifyDataSetChanged();
                updateEmptyCartMessage();
                calculateAndDisplayTotals();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Mycart.this, "Failed to load cart items", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                updateEmptyCartMessage();
            }
        });
        btnq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productDetailsList.isEmpty()) {
                    Toast.makeText(Mycart.this, "No Item To Checkout", Toast.LENGTH_SHORT).show();
                } else {

                    String orderId = generateOrderId();

                    if (orderId != null) {

                        DatabaseReference newOrderRef = orderrefrence.child(orderId);


                        Map<String, Object> orderItems = new HashMap<>();


                        for (ProductDetails item : productDetailsList) {
                            orderItems.put(item.getKey(), item);
                        }


                        orderItems.put("orderTotal", total.getText().toString());
                        orderItems.put("orderTimestamp", ServerValue.TIMESTAMP);
                        orderItems.put("username",username);
                        orderItems.put("phno",phno);
                        orderItems.put("notes",note);
                        orderItems.put("odered",true);
                        orderItems.put("delivered",false);


                        newOrderRef.setValue(orderItems).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    databaseReference.removeValue();
                                    productDetailsList.clear();
                                    adaptor.notifyDataSetChanged();
                                    updateEmptyCartMessage();

                                    startActivity(new Intent(Mycart.this, suceesanimation.class));
                                    finish();
                                } else {
                                    Toast.makeText(Mycart.this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(Mycart.this, "Failed to generate order ID. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void calculateAndDisplayTotals() {
        int subTotalAmount = 0;
        for (ProductDetails item : productDetailsList) {
            int a=Integer.parseInt(item.getProductamt());
            subTotalAmount += item.getQty() * a;
        }

        int totalAmount = subTotalAmount + deliveryFee;

        subtotal.setText("₹" + subTotalAmount);
        feedelivery.setText("₹" + deliveryFee);
        total.setText("₹" + totalAmount);
    }

    private void updateEmptyCartMessage() {
        if (productDetailsList.isEmpty()) {
            deliveryFee=0;
            calculateAndDisplayTotals();
            emptymsg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            emptymsg.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void onSelectionChanged(int selectedCount) {
        deleteButton.setVisibility(selectedCount > 0 ? View.VISIBLE : View.GONE);
    }

    private void exitSelectionMode() {
        adaptor.selectedItems.clear();
        adaptor.isSelectionMode = false;
        adaptor.notifyDataSetChanged();
        deleteButton.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (adaptor.isSelectionMode) {
            exitSelectionMode();
        } else {
            super.onBackPressed();
        }
    }
}
