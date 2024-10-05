package com.example.java;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.recyculer.ProductorderAdaptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Productordersactivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageButton btn;
    private ProductorderAdaptor adapter;
    private List<ProjectProduct> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productordersactivity);
        recyclerView = findViewById(R.id.recyulerordersbook7);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btn=findViewById(R.id.backbtnbookorders7);
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

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("projectsproducts").child(uid);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookList.clear();
                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    ProjectProduct book = bookSnapshot.getValue(ProjectProduct.class);
                    if (book != null) {
                        bookList.add(book);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Productordersactivity.this, "Failed to retrieve book orders.", Toast.LENGTH_LONG).show();
                Log.e("Bookordersactivity", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }
}