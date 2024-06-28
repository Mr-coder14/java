package com.example.java;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile_fragment extends Fragment {
    ImageButton constraintLayout;
    ImageButton btnlout;
    FirebaseAuth auth;
    private CircleImageView circleImageView;
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
        circleImageView=view.findViewById(R.id.shapeableImageViewq);
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
                        usersRef.child("profileImageUrl").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()){
                                    String uri=task.getResult().getValue(String.class);
                                    if(getContext()!=null){
                                        if(uri!=null){
                                            Glide.with(getContext())
                                                    .load(uri)
                                                    .into(circleImageView);
                                        }else {
                                            Glide.with(getContext())
                                                    .load(R.drawable.person3)
                                                    .into(circleImageView);
                                        }

                                    }

                                }
                            }
                        });
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
                showLogoutDialog();
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

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (user != null) {
                    auth.signOut();
                    Intent intent = new Intent(getContext(), loginactivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}
