package Tempadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.java.Fileinmodel;
import com.example.java.OrderdDetailsuser;
import com.example.java.R;
import com.example.java.RetrivepdfAdaptorhomeadmin;
import com.example.java.recyculer.orderadaptor;
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
import java.util.HashMap;
import java.util.List;

public class tempadminhomefragment extends Fragment {

    private static final String TAG = "hometempadmin";

    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String orderid;
    private EditText editText;
    private DatabaseReference databaseReference;
    private HashMap<String, Fileinmodel> uniqueOrders = new HashMap<>();
    private FirebaseRecyclerAdapter<Fileinmodel, RetrivepdfAdaptorhomeadmin> adapter;
    private Query query;
    private ProgressBar progressBar;
    private Handler debounceHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    public tempadminhomefragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tempadminhomefragment, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");
        recyclerView = view.findViewById(R.id.recyclerhometadmin);
        recyclerView.setHasFixedSize(true);
        progressBar = view.findViewById(R.id.progressbarhometadmin);
        progressBar.setVisibility(View.VISIBLE);
        editText = view.findViewById(R.id.search_edit_texttadmin);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        query = databaseReference;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uniqueOrders.clear();
                for (DataSnapshot uniqueIdSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot ui : uniqueIdSnapshot.getChildren()) {
                        for (DataSnapshot fileSnapshot : ui.getChildren()) {
                            String name = fileSnapshot.child("name0").getValue(String.class);
                            String uri = fileSnapshot.child("uri0").getValue(String.class);
                            String grandTotal = fileSnapshot.child("grandTotal0").getValue(String.class);
                            orderid = fileSnapshot.child("orderid0").getValue(String.class);


                            if (!uniqueOrders.containsKey(orderid)) {
                                Fileinmodel pdfFile = new Fileinmodel(name, uri, grandTotal, orderid);
                                uniqueOrders.put(orderid, pdfFile);
                                Log.d(TAG, "Added unique PDF File: " + pdfFile.getOrderid0());
                            }
                        }
                    }
                }
                Log.d(TAG, "Total unique PDFs: " + uniqueOrders.size());
                progressBar.setVisibility(View.GONE);
                displaypdfs();
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
        List<Fileinmodel> uniqueOrdersList = new ArrayList<>(uniqueOrders.values());
        orderadaptor adapter = new orderadaptor(uniqueOrdersList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}
