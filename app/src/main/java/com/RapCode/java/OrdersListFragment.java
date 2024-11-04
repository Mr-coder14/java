package com.RapCode.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.RapCode.java.recyculer.Allordersuser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import Tempadmin.AllOrderstempadmin;

public class OrdersListFragment extends Fragment {
    private ConstraintLayout c1, c2, c3, c4;
    private ImageButton c;
    private FirebaseUser user;
    private ArrayList<String> admins = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_orderslist, container, false);

        c1 = view.findViewById(R.id.xeroxorders);
        c4 = view.findViewById(R.id.productorders);
        c2 = view.findViewById(R.id.stationryorders);
        c = view.findViewById(R.id.backbtnordersuser11);
        c3 = view.findViewById(R.id.bookorders);
        user = FirebaseAuth.getInstance().getCurrentUser();
        admins.add("abcd1234@gmail.com");
        admins.add("saleem1712005@gmail.com");
        admins.add("jayaraman00143@gmail.com");

        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (admins.contains(user.getEmail())) {
                    startActivity(new Intent(getActivity(), productsordersactivityadmin.class));
                } else {
                    startActivity(new Intent(getActivity(), Productordersactivity.class));
                }
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (admins.contains(user.getEmail())) {
                    startActivity(new Intent(getActivity(), bookordersactivityadmin.class));
                } else {
                    startActivity(new Intent(getActivity(), Bookordersactivity.class));
                }
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (admins.contains(user.getEmail())) {
                    startActivity(new Intent(getActivity(), ordeersactivityadmin.class));
                } else {
                    startActivity(new Intent(getActivity(), OrdersActivity.class));
                }
            }
        });

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (admins.contains(user.getEmail())) {
                    startActivity(new Intent(getActivity(), AllOrderstempadmin.class));
                } else {
                    startActivity(new Intent(getActivity(), Allordersuser.class));
                }
            }
        });

        return view;
    }
}
