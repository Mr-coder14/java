package com.example.java;

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

import com.example.java.recyculer.Itemlistadaptormycart;
import com.example.java.recyculer.ProductDetails;
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
    private ArrayList<ProductDetails> productDetailsList;
    private TextView emptyCartMessage;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycart);

        recyclerView = findViewById(R.id.recyculermycart);
        emptyCartMessage = findViewById(R.id.empptytxt);
        back = findViewById(R.id.backbtnmycart);
        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbarmycart);
        userid = auth.getCurrentUser().getUid();
        productDetailsList = new ArrayList<>();
        emptyCartMessage.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);


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

        adaptor = new Itemlistadaptormycart(productDetailsList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);

        deleteButton.setVisibility(View.GONE);
        updateEmptyCartMessage();
    }

    private void fetchCartItems() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productDetailsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ProductDetails productDetails = dataSnapshot.getValue(ProductDetails.class);
                    if (productDetails != null) {
                        productDetails.setKey(dataSnapshot.getKey());
                        productDetailsList.add(productDetails);
                    }
                }
                progressBar.setVisibility(View.GONE);
                adaptor.notifyDataSetChanged();
                updateEmptyCartMessage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Mycart.this, "Failed to load cart items", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                updateEmptyCartMessage();
            }
        });
    }

    private void updateEmptyCartMessage() {
        if (adaptor.getItemCount() == 0) {
            emptyCartMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyCartMessage.setVisibility(View.GONE);
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
