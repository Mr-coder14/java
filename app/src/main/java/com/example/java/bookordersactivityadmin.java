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

import com.example.java.recyculer.BookOrderAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class bookordersactivityadmin extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageButton btn;
    private BookOrderAdapter adapter;
    private List<BookModel> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookordersactivityadmin);
        recyclerView = findViewById(R.id.recyulerordersbook1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btn=findViewById(R.id.backbtnbookorders1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bookList = new ArrayList<>();
        adapter = new BookOrderAdapter(bookList);
        recyclerView.setAdapter(adapter);

        loadBookOrders();
    }
    private void loadBookOrders() {




        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("books");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookList.clear();
                for(DataSnapshot book1:dataSnapshot.getChildren())
                for (DataSnapshot bookSnapshot : book1.getChildren()) {
                    BookModel book = bookSnapshot.getValue(BookModel.class);
                    if (book != null) {
                        bookList.add(book);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(bookordersactivityadmin.this, "Failed to retrieve book orders.", Toast.LENGTH_LONG).show();
                Log.e("Bookordersactivity", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }
}