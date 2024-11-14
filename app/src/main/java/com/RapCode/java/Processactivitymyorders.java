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

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Processactivitymyorders extends AppCompatActivity {
    private TextView gt,note;
    private String orderid,grandtotal;
    private List<Fileinmodel> fileinmodels;
    private boolean delivered;
    private DatabaseReference databaseReference;
    private ImageButton backbtn;
    private ImageView deleviredimage;
    private String notes,userid;
    //jk
    private TextView username,phno,date,orderidtxt,paymentmethod,totalpayment,balanceamount,paidamt,adresstxtt;
    private ProgressDialog progressDialog;
    private String screenshotUrl;
    private boolean isUserDataLoaded = false;
    private boolean isOrderDataLoaded = false;
    private ScrollView scrollView;


    private ImageView screensht;
    private DatabaseReference userref,scrrenshot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processactivityuser);
        adresstxtt=findViewById(R.id.adresstxtt);
        totalpayment=findViewById(R.id.ttttxt);
        paymentmethod=findViewById(R.id.paymentmethod);
        balanceamount=findViewById(R.id.Balanceamtt);
        paidamt=findViewById(R.id.paidamtt);
        orderidtxt=findViewById(R.id.orderidtxt);

        backbtn=findViewById(R.id.back_btnadmin121);
        gt=findViewById(R.id.gtt1);
        screensht=findViewById(R.id.paymentshots);
        username=findViewById(R.id.name11d);
        phno=findViewById(R.id.phnotxti);
        scrollView=findViewById(R.id.scview);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading order details...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        date=findViewById(R.id.ordertimetxt);
        scrollView.setVisibility(View.GONE);
        note=findViewById(R.id.notesuserdisplay);
        fileinmodels=new ArrayList<>();
        orderid=getIntent().getStringExtra("orderid2");
        grandtotal=getIntent().getStringExtra("gt2");
        gt.setText("₹ "+grandtotal);
        deleviredimage=findViewById(R.id.handleimagedelivered1);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("orderstempadmin").child(orderid);
        scrrenshot=FirebaseDatabase.getInstance().getReference().child("uploadscreenshots").child(orderid);
        orderidtxt.setText(orderid);
        loadOrderData();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot su:snapshot.getChildren()){
                    delivered=su.child("delivered").getValue(Boolean.class);
                    notes=su.child("notes").getValue(String.class);
                }
                if(notes!=null){
                    note.setText(notes!=""? notes:"N/A");
                }
                if(delivered) {
                    deleviredimage.setImageResource(R.drawable.tick);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        screensht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog imageDialog = new ProgressDialog(Processactivitymyorders.this);
                imageDialog.setMessage("Loading image...");
                imageDialog.setCancelable(false);
                imageDialog.show();


                View imageDialogView = getLayoutInflater().inflate(R.layout.dialog_image, null);
                ImageView dialogImageView = imageDialogView.findViewById(R.id.dialog_image_view);


                Glide.with(Processactivitymyorders.this)
                        .load(screenshotUrl)
                        .into(dialogImageView);


                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Processactivitymyorders.this);
                builder.setView(imageDialogView);
                androidx.appcompat.app.AlertDialog dialog = builder.create();


                Glide.with(Processactivitymyorders.this)
                        .load(screenshotUrl)
                        .into(dialogImageView);

                imageDialog.dismiss();
                dialog.show();
            }

        });

    }
    private void loadOrderData() {
        scrrenshot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retrieve and set all necessary fields with error handling
                    String address = snapshot.child("address").getValue(String.class);
                    Float balanceAmountLong = snapshot.child("balanceAmount").getValue(Float.class);
                    Float totalAmountLong = snapshot.child("totalAmount").getValue(Float.class);
                    Float paidAmount = snapshot.child("paidAmount").getValue(Float.class);
                    boolean fullPayment = snapshot.child("fullPayment").getValue(Boolean.class);
                    screenshotUrl = snapshot.child("screenshotUrl").getValue(String.class);
                    userid = snapshot.child("userId").getValue(String.class);
                    Long ordereedby=snapshot.child("timestamp").getValue(Long.class);

                    if (ordereedby != null) {
                        // Format the timestamp to a date string
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy (hh:mm a)", Locale.getDefault());
                        String formattedDate = sdf.format(new Date(ordereedby));
                        date.setText(formattedDate);
                    } else {
                        date.setText("N/A");
                    }


                    adresstxtt.setText(address != null ? address : "N/A");
                    totalpayment.setText(String.format("₹ %.2f", totalAmountLong != null ? totalAmountLong : 0.0));
                    balanceamount.setText(String.format("₹ %.2f", balanceAmountLong != null ? balanceAmountLong : 0.0));
                    paymentmethod.setText(fullPayment ? "Full Payment" : "50% Payment");
                    paidamt.setText(String.format("₹ %.2f", paidAmount != null ? paidAmount : 0.0));

                    if (screenshotUrl != null) {
                        Glide.with(Processactivitymyorders.this)
                                .load(screenshotUrl)
                                .placeholder(R.drawable.laodingpng)
                                .into(screensht);

                        isOrderDataLoaded = true;
                    } else {
                        // Set isOrderDataLoaded to true if there's no screenshot URL
                        isOrderDataLoaded = true;
                    }

                    // Ensure userid is not null before fetching user data
                    if (userid != null) {
                        fetchUserData();

                    } else {
                        Toast.makeText(Processactivitymyorders.this, "User ID not found.", Toast.LENGTH_SHORT).show();
                        isUserDataLoaded = true;
                        checkLoadingComplete();
                    }
                } else {
                    Toast.makeText(Processactivitymyorders.this, "Order not found.", Toast.LENGTH_SHORT).show();
                    isOrderDataLoaded = true;
                    checkLoadingComplete();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Processactivitymyorders.this, "Failed to load order data.", Toast.LENGTH_SHORT).show();
                isOrderDataLoaded = true;
                checkLoadingComplete();
            }
        });
    }

    private void fetchUserData() {
        if (userid == null) {
            isUserDataLoaded = true;
            checkLoadingComplete();
            return;
        }

        userref = FirebaseDatabase.getInstance().getReference().child("users").child(userid);
        userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String userName = snapshot.child("name").getValue(String.class);
                    String userPhone = snapshot.child("phno").getValue(String.class);

                    username.setText(userName != null ? userName : "N/A");
                    phno.setText(userPhone != null ? userPhone : "N/A");
                } else {
                    Toast.makeText(Processactivitymyorders.this, "User not found.", Toast.LENGTH_SHORT).show();
                }

                isUserDataLoaded = true;
                checkLoadingComplete();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Processactivitymyorders.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
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
