package Tempadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.java.Fileinmodel;
import com.example.java.OrderdDetailsuser;
import com.example.java.R;
import com.example.java.RetrivepdfAdaptorhomeadmin;
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

import java.util.ArrayList;
import java.util.List;

public class tempadminhistoryfragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private DatabaseReference pdfsRef;
    private FirebaseAuth auth;
    private Query query;
    private ProgressBar progressBar;
    private FirebaseUser user;
    private List<Fileinmodel> fl;
    private String orderid;
    private String userid;
    private FirebaseRecyclerAdapter<Fileinmodel, RetrivepdfAdaptorhomeadmin> adapter;



    public tempadminhistoryfragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_tempadminhistoryfragment, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");
        recyclerView = view.findViewById(R.id.recyclerhometadminhis);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        progressBar = view.findViewById(R.id.progressbarhometadminhis);
        progressBar.setVisibility(View.VISIBLE);
        fl = new ArrayList<>();
        userid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");
        query = databaseReference;


        pdfsRef = FirebaseDatabase.getInstance().getReference().child("pdfs");
        pdfsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot uniqueIdSnapshot : snapshot.getChildren()) {
                    for(DataSnapshot ui:uniqueIdSnapshot.getChildren()){
                    for (DataSnapshot fileSnapshot : ui.getChildren()) {
                        String name = fileSnapshot.child("name0").getValue(String.class);
                        String uri = fileSnapshot.child("uri0").getValue(String.class);
                        String grandTotal = fileSnapshot.child("grandTotal0").getValue(String.class);
                        orderid = fileSnapshot.child("orderid0").getValue(String.class);
                        Fileinmodel pdfFile = new Fileinmodel(name, uri, grandTotal, orderid);
                        fl.add(pdfFile);
                    }
                }}



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    setupAdapter();
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(getContext(), "No pdf found", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    private void setupAdapter() {
        FirebaseRecyclerOptions<Fileinmodel> options = new FirebaseRecyclerOptions.Builder<Fileinmodel>()
                .setQuery(query, Fileinmodel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Fileinmodel, RetrivepdfAdaptorhomeadmin>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RetrivepdfAdaptorhomeadmin holder, int position, @NonNull Fileinmodel model) {

                Fileinmodel fg=fl.get(position);
                holder.orderid.setText(fg.getOrderid0());
                holder.Grandtotal.setText("₹ "+fg.getGrandTotal0());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getContext(), OrderdDetailsuser.class);
                        intent.putExtra("orderid",fg.getOrderid0());
                        intent.putExtra("gt",fg.getGrandTotal0());
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