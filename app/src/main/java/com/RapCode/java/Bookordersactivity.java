package com.RapCode.java;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.RapCode.java.recyculer.BookOrderAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Bookordersactivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageButton btn;
    private BookOrderAdapter adapter;
    private List<BookModel> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookordersactivity);

        recyclerView = findViewById(R.id.recyulerordersbook);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btn=findViewById(R.id.backbtnbookorders);
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

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("books").child(uid);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookList.clear();
                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    BookModel book = bookSnapshot.getValue(BookModel.class);
                    if (book != null) {
                        bookList.add(book);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Bookordersactivity.this, "Failed to retrieve book orders.", Toast.LENGTH_LONG).show();
                Log.e("Bookordersactivity", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }
}
