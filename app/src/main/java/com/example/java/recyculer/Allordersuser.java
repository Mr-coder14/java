package com.example.java.recyculer;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.Fileinmodel;
import com.example.java.R;
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

public class Allordersuser extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private DatabaseReference pdfsRef;
    private FirebaseAuth auth;
    private Query query;
    private ProgressBar progressBar;
    private FirebaseUser user;
    private String orderid;
    private HashMap<String, Fileinmodel> uniqueOrders = new HashMap<>();
    private boolean delevireid;
    private String userid;
    private EditText searchEditText;
    private  allordersadaptor adapter;
    private TextView txt;
    private Handler debounceHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allordersuser);
        recyclerView = findViewById(R.id.recyclerhometadminhis00);
        auth = FirebaseAuth.getInstance();
        txt=findViewById(R.id.allordersempadmin12300);

        user = auth.getCurrentUser();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        progressBar = findViewById(R.id.progressbarhometadminhis00);
        progressBar.setVisibility(View.VISIBLE);
        txt.setVisibility(View.GONE);
        searchEditText=findViewById(R.id.search_edit_texttadminhish00);
        userid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs");
        query = databaseReference;

        Drawable searchIcon = ContextCompat.getDrawable(this, R.drawable.baseline_search_24);
        if (searchIcon != null) {
            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 34, getResources().getDisplayMetrics());
            searchIcon.setBounds(0, 0, size, size);
            searchEditText.setCompoundDrawables(searchIcon, null, null, null);
        }
        pdfsRef = FirebaseDatabase.getInstance().getReference().child("pdfs");
        pdfsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot uniqueIdSnapshot : snapshot.getChildren()) {
                    if(uniqueIdSnapshot.getKey().equals(userid)){
                    for(DataSnapshot ui:uniqueIdSnapshot.getChildren()){
                        for (DataSnapshot fileSnapshot : ui.getChildren()) {
                            String name = fileSnapshot.child("name0").getValue(String.class);
                            String uri = fileSnapshot.child("uri0").getValue(String.class);
                            String grandTotal = fileSnapshot.child("grandTotal0").getValue(String.class);
                            orderid = fileSnapshot.child("orderid0").getValue(String.class);
                            delevireid=fileSnapshot.child("delivered").getValue(boolean.class);
                            String username=fileSnapshot.child("username").getValue(String.class);
                            if(!uniqueOrders.containsKey(orderid)){
                                Fileinmodel pdfFile = new Fileinmodel(name, uri, grandTotal, orderid, delevireid,username);
                                uniqueOrders.put(orderid, pdfFile);
                            }
                        }
                    }}}
                updateUI();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Allordersuser.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (debounceHandler != null && searchRunnable != null) {
                    debounceHandler.removeCallbacks(searchRunnable);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchRunnable = () -> filterOrders(s.toString());
                debounceHandler.postDelayed(searchRunnable, 300);
            }
        });
    }
    private void updateUI() {
        progressBar.setVisibility(View.GONE);

        if (uniqueOrders.isEmpty()) {
            txt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            displaypdfs();
        }
    }

    private void displaypdfs() {
        List<Fileinmodel> uniqueOrdersList = new ArrayList<>(uniqueOrders.values());
        adapter = new allordersadaptor(uniqueOrdersList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
    private void filterOrders(String searchText) {
        List<Fileinmodel> filteredList = new ArrayList<>();
        for (Fileinmodel order : uniqueOrders.values()) {
            if (order.getOrderid0().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(order);
            }
        }
        if (filteredList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            txt.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            txt.setVisibility(View.GONE);
        }

        adapter.updateList(filteredList);
    }
}