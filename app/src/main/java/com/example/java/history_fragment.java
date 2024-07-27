package com.example.java;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.List;

public class history_fragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private DatabaseReference pdfsRef,df;
    private FirebaseAuth auth;
    private Query query;
    private ProgressBar progressBar;
    private FirebaseUser user;
    private List<Fileinmodel> fl;
    private String orderid;
    private String userid,username;
    private boolean delevired;
    private FirebaseRecyclerAdapter<Fileinmodel, RetrivepdfAdaptorhomeadmin> adapter;

    public history_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        return view;
    }
}
