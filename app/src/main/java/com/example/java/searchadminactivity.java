package com.example.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.java.recyculer.searchadminadaptor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class searchadminactivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private EditText searchadmin;
    private ImageButton addadmin, backbtn;
    private FirebaseRecyclerAdapter<User, searchadminadaptor> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchadminactivity);

        addadmin = findViewById(R.id.addadmin);
        backbtn = findViewById(R.id.back_btnadmin);
        searchadmin = findViewById(R.id.searchadmin);
        recyclerView = findViewById(R.id.search_recycular_view);
        progressBar = findViewById(R.id.progressadminsearch);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("admin");

        addadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(searchadminactivity.this, addadminactivity.class));
            }
        });

        searchadmin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAdmins(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupAdapter(databaseReference);

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void setupAdapter(Query query) {
        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<User, searchadminadaptor>(options) {
            @Override
            protected void onBindViewHolder(@NonNull searchadminadaptor holder, int position, @NonNull User model) {
                progressBar.setVisibility(View.GONE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(searchadminactivity.this, viewAdminProfile.class);
                        intent.putExtra("model",model.getEmail().replace(".", ","));
                        startActivity(intent);
                    }
                });

                holder.adminname.setText(model.getName());
                holder.adminemail.setText(model.getEmail());
            }

            @NonNull
            @Override
            public searchadminadaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchadmintemplate, parent, false);
                return new searchadminadaptor(view);
            }
        };

        adapter.startListening();
    }

    private void searchAdmins(String searchText) {
        Query searchQuery = databaseReference.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
        setupAdapter(searchQuery);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
