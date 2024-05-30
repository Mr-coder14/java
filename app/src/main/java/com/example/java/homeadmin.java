package com.example.java;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class homeadmin extends Fragment {
    private static final String TAG = "homeadmin";

    private RecyclerView recyclerView;
    FirebaseAuth auth;
    FirebaseUser user;
    private DatabaseReference databaseReference;
    private Query query;
    private ProgressBar progressBar;
    private Button btn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homeadmin, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");
        recyclerView = view.findViewById(R.id.recyclerhomeadmin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        progressBar = view.findViewById(R.id.progressbarhomeadmin);
        progressBar.setVisibility(View.VISIBLE);
        btn = view.findViewById(R.id.adminlgout);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        query = databaseReference.orderByChild("timestamp");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    auth.signOut();
                    startActivity(new Intent(getContext(), loginactivity.class));
                    getActivity().finish();
                }
            }
        });

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    displaypdfs();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No PDFs found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void displaypdfs() {

        FirebaseRecyclerOptions<Fileinmodel> options =
                new FirebaseRecyclerOptions.Builder<Fileinmodel>()
                        .setQuery(query, Fileinmodel.class)
                        .build();

        FirebaseRecyclerAdapter<Fileinmodel, RetrivepdfAdaptorhomeadmin> adapter =
                new FirebaseRecyclerAdapter<Fileinmodel, RetrivepdfAdaptorhomeadmin>(options) {


                    @Override
                    protected void onBindViewHolder(@NonNull RetrivepdfAdaptorhomeadmin holder, int position, @NonNull Fileinmodel model) {
                        holder.pdffilename1.setText(model.getName());

                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(model.getuserID());
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    User user = snapshot.getValue(User.class);
                                    if (user != null) {
                                        holder.UserName1.setText(user.getName());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String pdfUri = model.getUri();

                                if (pdfUri != null && !pdfUri.isEmpty()) {
                                    Log.d("PDF_URI", "PDF URI: " + pdfUri);
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.parse(pdfUri), "application/pdf");
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Ensure you have read permission
                                    try {
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        Toast.makeText(getContext(), "No PDF viewer found", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.e("PDF_URI", "PDF URI is not valid: " + pdfUri);
                                    Toast.makeText(getContext(), "PDF URI is not valid", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
                    }

                    @NonNull
                    @Override
                    public RetrivepdfAdaptorhomeadmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_template, parent, false);
                        return new RetrivepdfAdaptorhomeadmin(view);
                    }
                };

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
