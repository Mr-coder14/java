package com.example.java;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.logging.FileHandler;

public class profile_fragment extends Fragment {
    ImageButton btnlout;
    FirebaseAuth auth;
    FirebaseUser user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.profile_fragment,container,false);
        btnlout=view.findViewById(R.id.imageButtonlogout);
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();

        btnlout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user!=null)
                {
                    auth.signOut();
                    startActivity(new Intent(getActivity(), loginactivity.class));
                    getActivity().finish();
                }
            }
        });
        return view;
    }
}
