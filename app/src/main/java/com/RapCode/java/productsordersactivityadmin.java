package com.RapCode.java;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.RapCode.java.recyculer.ProductorderAdaptor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class productsordersactivityadmin extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageButton btn;
    private ProgressBar progressBar;
    private TextView textView;
    private ProductorderAdaptor adapter;
    private List<ProjectProduct> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productsordersactivityadmin);
        recyclerView = findViewById(R.id.recyulerordersproduct1);
        textView=findViewById(R.id.nordersmyordersadmin);
        progressBar=findViewById(R.id.progreessbarr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btn=findViewById(R.id.backbtnproductsorders1);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bookList = new ArrayList<>();
        adapter = new ProductorderAdaptor(bookList);
        recyclerView.setAdapter(adapter);

        loadBookOrders();

    }
    private void loadBookOrders() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("projectsproducts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookList.clear();
                for(DataSnapshot book1:dataSnapshot.getChildren()) {
                    for (DataSnapshot bookSnapshot : book1.getChildren()) {
                        ProjectProduct book = bookSnapshot.getValue(ProjectProduct.class);
                        if (book != null) {
                            bookList.add(book);
                        }
                    }
                }
                progressBar.setVisibility(View.GONE);
                if (bookList.isEmpty()) {
                    // Show "No Book Orders" message
                    textView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    // Display RecyclerView and hide "No Book Orders" message
                    textView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(productsordersactivityadmin.this, "Failed to retrieve book orders.", Toast.LENGTH_LONG).show();
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                Log.e("Bookordersactivity", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }
}