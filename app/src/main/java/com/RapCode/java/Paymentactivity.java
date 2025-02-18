package com.RapCode.java;

import android.app.ProgressDialog;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Paymentactivity extends AppCompatActivity {

    private TextView textView,address;
    private Button btn;
    private ImageButton btnw;
    private Orderconfirmuseractivity orderconfirmuseractivity;
    private boolean isFullPayment = false;
    private float balanceAmount = 0.0f;
    private ProgressDialog progressDialog;
    private float totalAmount = 0.0f;
    private String paymentScreenshotUrl;
    private DatabaseReference usersRef,uploadscreenshotref,databaseReference;
    private StorageReference storageRef;
    private String orderid;
    private RadioButton Adress;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentactivity);
        address=findViewById(R.id.address);

        orderid=getIntent().getStringExtra("orderid");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs").child(orderid);

        textView=findViewById(R.id.grandamty);
        Adress=findViewById(R.id.ratiobtnaddress);
        btnw=findViewById(R.id.backbtnpayment);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.setCancelable(false);



        Float t=getIntent().getExtras().getFloat("tt");
        totalAmount=t;
        orderconfirmuseractivity=new Orderconfirmuseractivity();
        btn=findViewById(R.id.orderbtnuser124);
        textView.setText("₹ "+String.valueOf(t));
        btnw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Paymentactivity.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Do you want to go back to the main screen?");

                // Set up "Yes" button
                builder.setPositiveButton("Yes", (dialog, which) -> {

                    Intent intent = new Intent(Paymentactivity.this, UsermainActivity.class);
                    startActivity(intent);
                    finish();
                });

                // Set up "No" button
                builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

                // Show the dialog
                builder.create().show();

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    uploadScreenshotAndSaveOrder();

                }

            }
        });


        storageRef = FirebaseStorage.getInstance().getReference();



        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateAddressInDatabase(String.valueOf(s));

            }
        });
        usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        uploadscreenshotref= FirebaseDatabase.getInstance().getReference().child("uploadscreenshots");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User userData = dataSnapshot.getValue(User.class);
                    if(userData.getAddress()!=null){
                        address.setText(userData.getAddress());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



    private void uploadScreenshotAndSaveOrder() {
        if ( user != null) {
            // Show the progress dialog
            progressDialog.show();
                            saveOrderDetails();

    }}


    private void saveOrderDetails() {
        try {


            if (totalAmount <= 0) {
                Toast.makeText(this, "Invalid total amount", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }

            float paidAmount = 0.0f;
            balanceAmount = totalAmount ;

            OrderScreenshotDetails orderDetails = new OrderScreenshotDetails(
                    orderid,
                    user.getUid(),
                    address.getText().toString(),

                    totalAmount,
                    paidAmount,
                    balanceAmount,
                    System.currentTimeMillis()
            );

            uploadscreenshotref.child(orderid).setValue(orderDetails)
                    .addOnSuccessListener(aVoid -> {
                        updatePaidStatusForAllFiles();
                        Toast.makeText(Paymentactivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        startActivity(new Intent(Paymentactivity.this,suceesanimation.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(Paymentactivity.this, "Failed to save order details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } catch (Exception e) {
            progressDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(this, "Error in saving order details: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void updatePaidStatusForAllFiles() {
        DatabaseReference filesRef = FirebaseDatabase.getInstance().getReference().child("pdfs").child(user.getUid()).child(orderid);

        filesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                        fileSnapshot.getRef().child("paid").setValue(true)
                                .addOnSuccessListener(aVoid -> {
                                    // Success message for each file (optional logging)
                                })
                                .addOnFailureListener(e -> {
                                    // Failure message for each file
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Paymentactivity.this, "Failed to update payment status", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean validateInputs() {

        if (address.getText().toString().isEmpty()) {
            Toast.makeText(this, "Address cannot be empty", Toast.LENGTH_SHORT).show();
            address.setError("Address cannot be empty");
            address.requestFocus();
            return false;
        }

        if (!Adress.isChecked()) {
            Toast.makeText(this, "Please select the address", Toast.LENGTH_SHORT).show();
            Adress.setError("Please select the address");
            Adress.requestFocus();
            return false;
        }




        return true;
    }


    private void updateAddressInDatabase(String newAddress) {
        if (newAddress.isEmpty()) {
            address.setError("Address cannot be empty");
            address.requestFocus();
            return;
        }


        usersRef.child("address").setValue(newAddress).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

            } else {

            }
        });
    }



    public static class OrderScreenshotDetails {
        private String orderId;
        private String userId;
        private String address;

        private float totalAmount;
        private float paidAmount;
        private float balanceAmount;

        private long timestamp;

        public OrderScreenshotDetails() {
            // Required empty constructor for Firebase
        }

        public OrderScreenshotDetails(String orderId, String userId, String address,
                                       float totalAmount,
                                      float paidAmount, float balanceAmount,
                 long timestamp) {
            this.orderId = orderId;
            this.userId = userId;
            this.address = address;
            this.totalAmount = totalAmount;
            this.paidAmount = paidAmount;
            this.balanceAmount = balanceAmount;

            this.timestamp = timestamp;
        }

        // Getters and setters
        public String getOrderId() { return orderId; }
        public void setOrderId(String orderId) { this.orderId = orderId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public float getTotalAmount() { return totalAmount; }
        public void setTotalAmount(float totalAmount) { this.totalAmount = totalAmount; }
        public float getPaidAmount() { return paidAmount; }
        public void setPaidAmount(float paidAmount) { this.paidAmount = paidAmount; }
        public float getBalanceAmount() { return balanceAmount; }
        public void setBalanceAmount(float balanceAmount) { this.balanceAmount = balanceAmount; }

        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}
