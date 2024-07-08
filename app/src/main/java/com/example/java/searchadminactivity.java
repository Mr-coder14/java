package com.example.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.java.recyculer.searchadminadaptor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

        databaseReference = FirebaseDatabase.getInstance().getReference().child("tempadmin1");

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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    setupAdapter(databaseReference);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(searchadminactivity.this, "No admins found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(searchadminactivity.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
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
                holder.adminname.setText(model.getName());
                holder.adminemail.setText(model.getEmail());

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        String userid = model.getUserid();

                        new AlertDialog.Builder(searchadminactivity.this)
                                .setTitle("Delete Admin")
                                .setMessage("Are you sure you want to delete this admin?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        databaseReference.child(userid).removeValue().addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(searchadminactivity.this, "Admin deleted successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(searchadminactivity.this, "Failed to delete admin", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                        return true;
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(searchadminactivity.this, viewAdminProfile.class);
                        intent.putExtra("model", model.getUserid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public searchadminadaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchadmintemplate, parent, false);
                return new searchadminadaptor(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        progressBar.setVisibility(View.GONE);
    }

    private void searchAdmins(String searchText) {
        Query searchQuery = databaseReference.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
        setupAdapter(searchQuery);
        adapter.notifyDataSetChanged();
    }
}
