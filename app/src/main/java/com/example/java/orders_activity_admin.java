package com.example.java;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.java.recyculer.RetrivepdfAdaptor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class orders_activity_admin extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private Query query;
    private ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.oders_activity_admin,container,false);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("pdfs");
        recyclerView=view.findViewById(R.id.recyclerVieworders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        progressBar=view.findViewById(R.id.progress_baradmin);
        progressBar.setVisibility(View.VISIBLE);
        query=databaseReference.orderByChild("timestamp");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    displaypdfs();

                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), ":", Toast.LENGTH_SHORT).show();
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

        FirebaseRecyclerAdapter<Fileinmodel, RetrivepdfAdaptor> adapter =
                new FirebaseRecyclerAdapter<Fileinmodel, RetrivepdfAdaptor>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RetrivepdfAdaptor holder, int position, @NonNull Fileinmodel model) {
                        progressBar.setVisibility(View.GONE);
                        holder.pdffilename.setText(model.getName());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (model.getUri() != null && !model.getUri().isEmpty()) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setType("application/pdf");
                                    intent.setData(Uri.parse(model.getUri()));
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), "PDF URI is not valid", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public RetrivepdfAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_item_layout,parent,false);
                        RetrivepdfAdaptor holder=new RetrivepdfAdaptor(view);
                        return holder;
                    }
                };
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
