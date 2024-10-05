package com.example.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


public class shopchoosingactivity extends Fragment {
    private ConstraintLayout c1,c2,c3;
    public shopchoosingactivity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_shopchoosingactivity, container, false);
        c1=view.findViewById(R.id.xeroxorderso);
        c2=view.findViewById(R.id.stationryorderso);
        c3=view.findViewById(R.id.bookorderso);

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(getContext(),BookFormApplication.class));
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(getContext(),StationaryActivity.class));
            }
        });

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(getContext(), Projectproductsfrormapplication.class));

            }
        });
        return view;
    }
}