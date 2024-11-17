package com.RapCode.java;

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

import com.RapCode.java.recyculer.Itemlistadaptormycart;
import com.RapCode.java.recyculer.ProductDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Mycart extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageButton back, deleteButton;
    private Itemlistadaptormycart adaptor;
    private int totalAmount;
    private ArrayList<ProductDetails> productDetailsList;
    private TextView emptymsg,subtotal, feedelivery, total;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private Button btnq,addnotes;
    private FirebaseAuth auth;
    private String userid;
    private EditText notes;
    private String note;
    private int deliveryFee=0;
    public Mycart(){

    }

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
            public void onClick(View v)
            {
                note=notes.getText().toString();
                Toast.makeText(Mycart.this, "Notes Added SuccessFully", Toast.LENGTH_SHORT).show();
            }
        });


        if (userid != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("userscart").child(userid);


            fetchCartItems();
        }



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
        btnq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Mycart.this,Paymentcart.class);
                intent.putExtra("productList", productDetailsList);
                intent.putExtra("gt",String.valueOf(totalAmount));
                intent.putExtra("notes",note);
                startActivity(new Intent(intent));
            }
        });

        adaptor = new Itemlistadaptormycart(productDetailsList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);

        deleteButton.setVisibility(View.GONE);
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

    }



    public void calculateAndDisplayTotals() {
        int subTotalAmount = 0;
        for (ProductDetails item : productDetailsList) {
            int a=Integer.parseInt(item.getProductamt());
            subTotalAmount += item.getQty() * a;
        }

        totalAmount = subTotalAmount + deliveryFee;

        subtotal.setText("₹" + subTotalAmount);
        feedelivery.setText("Free Delivery");
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
