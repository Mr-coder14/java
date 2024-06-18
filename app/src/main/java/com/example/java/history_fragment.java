package com.example.java;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class history_fragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private DatabaseReference usersRef;
    private FirebaseAuth auth;
    private Query query;
    private ProgressBar progressBar;
    private FirebaseUser user;

    private User userData;
    private FirebaseRecyclerAdapter<Fileinmodel, RetrivepdfAdaptorhomeadmin> adapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");
        recyclerView = view.findViewById(R.id.recyclerView);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());


        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userData = dataSnapshot.getValue(User.class);
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        query = databaseReference.orderByChild("userID").equalTo(currentUserId);



        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    progressBar.setVisibility(View.GONE);
                    displaypdfs(query);
                } else {
                    progressBar.setVisibility(View.GONE);
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "No pdf found", Toast.LENGTH_SHORT).show();
                    }
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



    private void displaypdfs(Query query) {
        FirebaseRecyclerOptions<Fileinmodel> options =
                new FirebaseRecyclerOptions.Builder<Fileinmodel>()
                        .setQuery(query, Fileinmodel.class)
                        .build();

        FirebaseRecyclerAdapter<Fileinmodel, RetrivepdfAdaptorhomeadmin> adapter =
                new FirebaseRecyclerAdapter<Fileinmodel, RetrivepdfAdaptorhomeadmin>(options) {


                    @Override
                    protected void onBindViewHolder(@NonNull RetrivepdfAdaptorhomeadmin holder, int position, @NonNull Fileinmodel model) {
                        holder.pdffilename1.setText(model.getName());
                        Uri pdfUri=Uri.parse(model.getUri());


                        holder.orderid.setText(model.getOrderid());
                        String orderID=holder.orderid.getText().toString();


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
                                if (getContext() != null) {
                                    Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String pdfUri = model.getUri();

                                Intent intent=new Intent(getActivity(),OrderdDetailsuser.class);
                                intent.setData(Uri.parse(pdfUri));
                                intent.putExtra("Orderid",orderID);
                                startActivity(intent);
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


