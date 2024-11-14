package Tempadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.RapCode.java.Fileinmodel;
import com.RapCode.java.R;
import com.RapCode.java.tempadminmainactivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class Processorderactivity extends AppCompatActivity {
    
    private TextView gt,adresstxtt;
    private String orderid,grandtotal,userid;
    private List<Fileinmodel> fileinmodels;
    private boolean isActivityStarted = false;
    private TextView note,username,phno,date,orderidtxt,paymentmethod,totalpayment,balanceamount,paidamt;
    String notes;

    private Button btn;
    boolean dev=false,up=false;
    private LinearLayout layout;
    private ProgressDialog progressDialog;
    private String screenshotUrl;
    private boolean isUserDataLoaded = false;
    private boolean isOrderDataLoaded = false;
    private ScrollView scrollView;
    private boolean delivered;
    private ImageButton backbtn;
    private ImageView screensht;
    private DatabaseReference databaseReference,newchild,userref,scrrenshot,db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processorderactivity);
        
        btn=findViewById(R.id.OrderConfirmedbtn);
        backbtn=findViewById(R.id.back_btnadmin12);
        gt=findViewById(R.id.gtt);
        note=findViewById(R.id.notesuserdisplay1);
        screensht=findViewById(R.id.paymentshots);
        username=findViewById(R.id.name11d);
        phno=findViewById(R.id.phnotxti);
        layout=findViewById(R.id.bottomviewww);
        scrollView=findViewById(R.id.scview);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading order details...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        layout.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);

        orderidtxt=findViewById(R.id.orderidtxt);
        date=findViewById(R.id.ordertimetxt);

        fileinmodels=new ArrayList<>();
        adresstxtt=findViewById(R.id.adresstxtt);
        totalpayment=findViewById(R.id.ttttxt);
        paymentmethod=findViewById(R.id.paymentmethod);
        balanceamount=findViewById(R.id.Balanceamtt);
        paidamt=findViewById(R.id.paidamtt);
        orderid=getIntent().getStringExtra("orderid2");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");
        newchild = FirebaseDatabase.getInstance().getReference().child("orderstempadmin");
        grandtotal=getIntent().getStringExtra("gt2");
        gt.setText("₹ "+grandtotal);
        orderidtxt.setText(orderid);

        scrrenshot=FirebaseDatabase.getInstance().getReference().child("uploadscreenshots").child(orderid);
        loadOrderData();







        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        if (orderSnapshot.getKey().equals(orderid)) {
                            for (DataSnapshot fileSnapshot : orderSnapshot.getChildren()) {
                                notes = fileSnapshot.child("notes").getValue(String.class);

                            }
                        }
                    }
                }
                if(notes!=null){
                    note.setText(notes!=""? notes:"N/A");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        screensht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog imageDialog = new ProgressDialog(Processorderactivity.this);
                imageDialog.setMessage("Loading image...");
                imageDialog.setCancelable(false);
                imageDialog.show();


                View imageDialogView = getLayoutInflater().inflate(R.layout.dialog_image, null);
                ImageView dialogImageView = imageDialogView.findViewById(R.id.dialog_image_view);


                Glide.with(Processorderactivity.this)
                        .load(screenshotUrl)
                        .into(dialogImageView);


                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Processorderactivity.this);
                builder.setView(imageDialogView);
                androidx.appcompat.app.AlertDialog dialog = builder.create();


                Glide.with(Processorderactivity.this)
                        .load(screenshotUrl)
                        .into(dialogImageView);

                imageDialog.dismiss();
                dialog.show();
            }

        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delivered = true;
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                                if (orderSnapshot.getKey().equals(orderid)) {
                                    for (DataSnapshot fileSnapshot : orderSnapshot.getChildren()) {
                                        databaseReference.child(userSnapshot.getKey())
                                                .child(orderid)
                                                .child(fileSnapshot.getKey())
                                                .child("delivered")
                                                .setValue(true)
                                                .addOnSuccessListener(aVoid -> {
                                                    dev = true;
                                                    checkAndStartMainActivity();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(Processorderactivity.this, "Failed To Add", Toast.LENGTH_SHORT).show();
                                                });

                                        Fileinmodel pdfFile = fileSnapshot.getValue(Fileinmodel.class);
                                        if (pdfFile != null) {
                                            fileinmodels.add(pdfFile);

                                            newchild.child(orderid).child(fileSnapshot.getKey()).setValue(pdfFile)
                                                    .addOnSuccessListener(aVoid -> {
                                                        newchild.child(orderid).child(fileSnapshot.getKey()).child("delivered")
                                                                .setValue(true)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        up = true;
                                                                        checkAndStartMainActivity();
                                                                    }
                                                                });
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        // Toast.makeText(Processorderactivity.this, "Added Failed", Toast.LENGTH_SHORT).show();
                                                    });
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Processorderactivity.this, "No files", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });









    }
    private void checkAndStartMainActivity() {
        // Ensure both dev and up are true before transitioning
        if (dev && up && !isActivityStarted) {
            isActivityStarted = true; // Prevent future attempts to start the activity
            startActivity(new Intent(Processorderactivity.this, tempadminmainactivity.class));
            Toast.makeText(this, "Order Added Successfully", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }
    }
    private void loadOrderData() {
        scrrenshot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String address = snapshot.child("address").getValue(String.class);
                    Float balanceAmountLong = snapshot.child("balanceAmount").getValue(Float.class);
                    Float totalAmountLong = snapshot.child("totalAmount").getValue(Float.class);
                    Float paidAmount = snapshot.child("paidAmount").getValue(Float.class);
                    boolean fullPayment = snapshot.child("fullPayment").getValue(Boolean.class);
                    screenshotUrl = snapshot.child("screenshotUrl").getValue(String.class);
                    userid = snapshot.child("userId").getValue(String.class);
                    Long ordereedby=snapshot.child("timestamp").getValue(Long.class);

                    if (ordereedby != null) {

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
                        Glide.with(Processorderactivity.this)
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
                        Toast.makeText(Processorderactivity.this, "User ID not found.", Toast.LENGTH_SHORT).show();
                        isUserDataLoaded = true;
                        checkLoadingComplete();
                    }
                } else {
                    Toast.makeText(Processorderactivity.this, "Order not found.", Toast.LENGTH_SHORT).show();
                    isOrderDataLoaded = true;
                    checkLoadingComplete();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Processorderactivity.this, "Failed to load order data.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Processorderactivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                }

                isUserDataLoaded = true;
                checkLoadingComplete();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Processorderactivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                isUserDataLoaded = true;
                checkLoadingComplete();
            }
        });
    }
    private void checkLoadingComplete() {
        if (isUserDataLoaded && isOrderDataLoaded) {
            progressDialog.dismiss();
            layout.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

}