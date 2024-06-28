package com.example.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.java.recyculer.searchadminadaptor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class searchadminactivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private ImageButton addadmin,backbtn;
    private FirebaseRecyclerAdapter<User, searchadminadaptor> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchadminactivity);
        addadmin=findViewById(R.id.addadmin);
        backbtn=findViewById(R.id.back_btnadmin);

        recyclerView = findViewById(R.id.search_recycular_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar=findViewById(R.id.progressadminsearch);
        progressBar.setVisibility(View.VISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        addadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(searchadminactivity.this, addadminactivity.class));

            }
        });

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(databaseReference, User.class)
                        .build();

        adapter=new FirebaseRecyclerAdapter<User, searchadminadaptor>(options) {
            @Override
            protected void onBindViewHolder(@NonNull searchadminadaptor holder, int position, @NonNull User model) {
                progressBar.setVisibility(View.GONE);

                holder.adminname.setText(model.getName());
                holder.adminemail.setText(model.getEmail());

            }

            @NonNull
            @Override
            public searchadminadaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.searchadmintemplate,parent,false);
                return new searchadminadaptor(view);
            }
        };
        recyclerView.setAdapter(adapter);


    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

}