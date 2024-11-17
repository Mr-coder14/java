package com.RapCode.java;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.RapCode.java.recyculer.ProductDetails;
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

public class Paymentcart extends AppCompatActivity {
    private Button btn;
    private ImageButton btnw;
    private ArrayList<ProductDetails> productDetailsList;
    private RadioButton addre,payment;
    private TextView Gtt,address;
    private int totalAmount;

    private String userid,note,username,phno,ad;

    private DatabaseReference usersRef,orderrefrence,databaseReference;
    private Mycart mycart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentcart);
        Gtt=findViewById(R.id.grandamty);
        address=findViewById(R.id.address);
        userid = FirebaseAuth.getInstance().getUid();
        note=getIntent().getStringExtra("notes");
        addre=findViewById(R.id.ratiobtnaddress);
        payment=findViewById(R.id.radioButton1full);
        btnw=findViewById(R.id.backbtnpayment);
        Intent intent=getIntent();
        productDetailsList = (ArrayList<ProductDetails>) intent.getSerializableExtra("productList");
        totalAmount =Integer.parseInt(getIntent().getStringExtra("gt"));
        btn=findViewById(R.id.orderbtnuser124);
        Gtt.setText("₹ "+ String.valueOf(totalAmount));
        mycart=new Mycart();
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ad=s.toString();

            }
        });
        if (userid != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("userscart").child(userid);
            orderrefrence=FirebaseDatabase.getInstance().getReference("userorders").child(userid);
            usersRef = FirebaseDatabase.getInstance().getReference("users").child(userid);

        }

        btnw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userData = snapshot.getValue(User.class);
                if (userData != null) {
                    username=userData.getName();
                    phno=userData.getPhno();
                    address.setText(userData.getAddress());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vlaidate()){
                    uploadorders();
                }
            }
        });
    }
    private String generateOrderId() {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString().replaceAll("-", "");
        return uuidString.substring(0, Math.min(uuidString.length(), 10));
    }
    public void uploadorders( ){
        if (productDetailsList.isEmpty()) {
            Toast.makeText(Paymentcart.this, "No Item To Checkout", Toast.LENGTH_SHORT).show();
        } else {
            if (totalAmount >= 50) {

                String orderId = generateOrderId();

                if (orderId != null) {
                    DatabaseReference newOrderRef = orderrefrence.child(orderId);
                    Map<String, Object> orderItems = new HashMap<>();
                    for (ProductDetails item : productDetailsList) {
                        orderItems.put(item.getKey(), item);
                    }
                    orderItems.put("orderTotal", String.valueOf(totalAmount));
                    orderItems.put("orderTimestamp", ServerValue.TIMESTAMP);
                    orderItems.put("username", username);
                    orderItems.put("phno", phno);
                    orderItems.put("notes", note);
                    orderItems.put("odered", true);
                    orderItems.put("delivered", false);
                    orderItems.put("address",ad);

                    newOrderRef.setValue(orderItems).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                databaseReference.removeValue();
                                productDetailsList.clear();


                                startActivity(new Intent(Paymentcart.this, suceesanimation.class));
                                finish();
                            } else {
                                Toast.makeText(Paymentcart.this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Paymentcart.this, "Failed to generate order ID. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(Paymentcart.this, "The Minimum Order Should be 50 Rupees", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean vlaidate() {
        if (address.getText().toString().isEmpty()) {
            Toast.makeText(this, "Address cannot be empty", Toast.LENGTH_SHORT).show();
            address.setError("Address cannot be empty");
            address.requestFocus();
            return false;
        }

        if (!addre.isChecked()) {
            Toast.makeText(this, "Please select the address", Toast.LENGTH_SHORT).show();
            addre.setError("Please select the address");
            addre.requestFocus();
            return false;
        }

        if(!payment.isChecked()){
            Toast.makeText(this, "Please select the Payment Method", Toast.LENGTH_SHORT).show();
            addre.setError("Please select the Payment Method");
            addre.requestFocus();
            return false;
        }
        return true;

    }
}