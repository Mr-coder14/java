package com.RapCode.java;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Proccessactivityallorders extends AppCompatActivity {
    private TextView gt, note, timee;
    private String orderid, grandtotal;
    private boolean delivered;
    private DatabaseReference databaseReference;
    private ImageButton backbtn;
    private ImageView deliveredImage;
    private String notes, userid;
    private Long time;
    private TextView username, phno, date, orderidtxt, totalpayment, balanceamount, paidamt, adresstxtt;
    private ProgressDialog progressDialog;
    private boolean isUserDataLoaded = false;
    private boolean isOrderDataLoaded = false;
    private ScrollView scrollView;
    private DatabaseReference userRef, screenshotRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proccessactivityallorders);

        // Initialize UI components
        backbtn = findViewById(R.id.back_btnadmin1219);
        gt = findViewById(R.id.gtt19);
        timee = findViewById(R.id.uploadtime);
        note = findViewById(R.id.notesuserdisplay9);
        orderidtxt = findViewById(R.id.orderidtxt);
        deliveredImage = findViewById(R.id.handleimagedelivered19);
        adresstxtt = findViewById(R.id.adresstxtt);
        totalpayment = findViewById(R.id.ttttxt);

        balanceamount = findViewById(R.id.Balanceamtt);
        paidamt = findViewById(R.id.paidamtt);
        username = findViewById(R.id.name11d);
        phno = findViewById(R.id.phnotxti);
        date = findViewById(R.id.ordertimetxt);
        scrollView = findViewById(R.id.scview);

        // Initialize variables
        orderid = getIntent().getStringExtra("orderid2");
        grandtotal = getIntent().getStringExtra("gt2");
        gt.setText("₹ " + grandtotal);
        orderidtxt.setText(orderid);

        // Firebase references
        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");
        screenshotRef = FirebaseDatabase.getInstance().getReference().child("uploadscreenshots").child(orderid);

        // Show progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading order details...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        scrollView.setVisibility(View.GONE);

        // Load order data
        loadOrderData();

        // Retrieve additional data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ui : snapshot.getChildren()) {
                    for (DataSnapshot order : ui.getChildren()) {
                        if (order.getKey().equals(orderid)) {
                            for (DataSnapshot su : order.getChildren()) {
                                Boolean deliveredValue = su.child("delivered").getValue(Boolean.class);
                                String notesValue = su.child("notes").getValue(String.class);
                                time = su.child("uploadTime").getValue(Long.class);

                                delivered = deliveredValue != null && deliveredValue;
                                notes = notesValue != null ? notesValue : "N/A";

                                break;
                            }
                            break;
                        }
                    }
                }

                note.setText(notes);
                if (time != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(time);
                    calendar.add(Calendar.DAY_OF_MONTH, 1); // Add one day
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                    timee.setText(dateFormat.format(calendar.getTime()));
                }
                if (delivered) {
                    deliveredImage.setImageResource(R.drawable.tick);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Proccessactivityallorders.this, "Failed to retrieve additional data.", Toast.LENGTH_SHORT).show();
            }
        });

        // Back button
        backbtn.setOnClickListener(v -> finish());
    }

    private void loadOrderData() {
        screenshotRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String address = snapshot.child("address").getValue(String.class);
                    Float balanceAmount = snapshot.child("balanceAmount").getValue(Float.class);
                    Float totalAmount = snapshot.child("totalAmount").getValue(Float.class);
                    Float paidAmount = snapshot.child("paidAmount").getValue(Float.class);

                    userid = snapshot.child("userId").getValue(String.class);
                    Long orderedTimestamp = snapshot.child("timestamp").getValue(Long.class);

                    if (orderedTimestamp != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy (hh:mm a)", Locale.getDefault());
                        date.setText(sdf.format(new Date(orderedTimestamp)));
                    } else {
                        date.setText("N/A");
                    }

                    adresstxtt.setText(address != null ? address : "N/A");
                    totalpayment.setText(String.format("₹ %.2f", totalAmount != null ? totalAmount : 0.0));
                    balanceamount.setText(String.format("₹ %.2f", balanceAmount != null ? balanceAmount : 0.0));

                    paidamt.setText(String.format("₹ %.2f", paidAmount != null ? paidAmount : 0.0));

                    isOrderDataLoaded = true;

                    if (userid != null) {
                        fetchUserData();
                    } else {
                        Toast.makeText(Proccessactivityallorders.this, "User ID not found.", Toast.LENGTH_SHORT).show();
                        isUserDataLoaded = true;
                        checkLoadingComplete();
                    }
                } else {
                    Toast.makeText(Proccessactivityallorders.this, "Order not found.", Toast.LENGTH_SHORT).show();
                    isOrderDataLoaded = true;
                    checkLoadingComplete();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Proccessactivityallorders.this, "Failed to load order data.", Toast.LENGTH_SHORT).show();
                isOrderDataLoaded = true;
                checkLoadingComplete();
            }
        });
    }

    private void fetchUserData() {
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    username.setText(snapshot.child("name").getValue(String.class) != null ? snapshot.child("name").getValue(String.class) : "N/A");
                    phno.setText(snapshot.child("phno").getValue(String.class) != null ? snapshot.child("phno").getValue(String.class) : "N/A");
                } else {
                    Toast.makeText(Proccessactivityallorders.this, "User not found.", Toast.LENGTH_SHORT).show();
                }

                isUserDataLoaded = true;
                checkLoadingComplete();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                isUserDataLoaded = true;
                checkLoadingComplete();
            }
        });
    }

    private void checkLoadingComplete() {
        if (isUserDataLoaded && isOrderDataLoaded) {
            progressDialog.dismiss();
            scrollView.setVisibility(View.VISIBLE);
        }
    }
}
