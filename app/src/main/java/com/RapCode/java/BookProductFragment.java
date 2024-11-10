package com.RapCode.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class BookProductFragment extends Fragment {
    private ConstraintLayout  c3, c4;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookproductfragment, container, false);


        c4 = view.findViewById(R.id.productorders);


        c3 = view.findViewById(R.id.bookorders);


        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Projectproductsfrormapplication.class));
            }
        });



        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(getContext(), BookFormApplication.class));
            }
        });



        return view;
    }
}
