package com.example.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile_fragment extends Fragment {
    ImageButton constraintLayout;
    ImageButton btnlout;
    FirebaseAuth auth;
    TextView email, name, email1, name1, phno1;

    ProgressBar progressBar;
    ScrollView scrollView;
    FirebaseUser user;
    DatabaseReference usersRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        btnlout = view.findViewById(R.id.imageButtonlogout);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        name = view.findViewById(R.id.profilename);
        progressBar=view.findViewById(R.id.progressprofile);
        scrollView=view.findViewById(R.id.profilevisible);
        phno1 = view.findViewById(R.id.phno11);
        constraintLayout = view.findViewById(R.id.editdetails);
        email1 = view.findViewById(R.id.email11);
        name1 = view.findViewById(R.id.name11);
        email = view.findViewById(R.id.emailprofile);
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        usersRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User userData = dataSnapshot.getValue(User.class);
                    if (userData != null) {
                        name.setText(userData.getName());
                        name1.setText(userData.getName());
                        phno1.setText(userData.getPhno());
                        email.setText(userData.getEmail());
                        email1.setText(userData.getEmail());
                    }
                    progressBar.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }
        });

        btnlout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    auth.signOut();
                    Intent intent = new Intent(getActivity(), loginactivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), editdetails.class));
            }
        });

        return view;
    }
}
