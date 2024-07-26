package com.example.java;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.recyculer.orderadaptormyorders;
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

public class ordersfragment_adminmain extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private DatabaseReference pdfsRef;
    private FirebaseAuth auth;
    private orderadaptormyorders adapter;
    private TextView txt;
    private Query query;
    private ProgressBar progressBar;
    private EditText editText;
    private FirebaseUser user;
    private HashMap<String, Fileinmodel> uniqueOrders = new HashMap<>();
    private String orderid;
    private boolean deleivired;
    private String userid;
    private Handler debounceHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.oders_activity_adminadmin, container, false);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("orderstempadmin");
        recyclerView = view.findViewById(R.id.recyclerhometadminhis1);
        auth = FirebaseAuth.getInstance();
        txt=view.findViewById(R.id.nordersmyordersadmin1);
        editText=view.findViewById(R.id.search_edit_texttadminhis1);
        user = auth.getCurrentUser();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        progressBar = view.findViewById(R.id.progressbarhometadminhis1);
        progressBar.setVisibility(View.VISIBLE);
        txt.setVisibility(View.GONE);
        userid = user.getUid();

        Drawable searchIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_search_24);
        if (searchIcon != null) {
            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 34, getResources().getDisplayMetrics());
            searchIcon.setBounds(0, 0, size, size);
            editText.setCompoundDrawables(searchIcon, null, null, null);
        }
        query = databaseReference;


        pdfsRef = FirebaseDatabase.getInstance().getReference().child("orderstempadmin");
        pdfsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ui:snapshot.getChildren()){
                    for (DataSnapshot fileSnapshot : ui.getChildren()) {
                        String name = fileSnapshot.child("name0").getValue(String.class);
                        String uri = fileSnapshot.child("uri0").getValue(String.class);
                        String grandTotal = fileSnapshot.child("grandTotal0").getValue(String.class);
                        orderid = fileSnapshot.child("orderid0").getValue(String.class);
                        String username=fileSnapshot.child("username").getValue(String.class);
                        deleivired=fileSnapshot.child("delivered").getValue(boolean.class);
                        if(deleivired==true && !uniqueOrders.containsKey(orderid)){
                            Fileinmodel pdfFile = new Fileinmodel(name, uri, grandTotal, orderid,deleivired,username);
                            uniqueOrders.put(orderid, pdfFile);
                        }

                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                txt.setVisibility(View.VISIBLE);
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
                    progressBar.setVisibility(View.GONE);
                    txt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

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



        return view;
    }
    private void setupAdapter() {
        List<Fileinmodel> uniqueOrdersList = new ArrayList<>(uniqueOrders.values());
        adapter = new orderadaptormyorders(uniqueOrdersList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
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
        adapter.updateList(filteredList);

    }





}
