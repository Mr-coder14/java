package com.example.java;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileadminfragment extends Fragment {
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
        View view = inflater.inflate(R.layout.profileadminfragmentadmin, container, false);
        btnlout = view.findViewById(R.id.imageButtonlogoutadminmain);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        name = view.findViewById(R.id.profilenameadminmain);
        circleImageView=view.findViewById(R.id.shapeableImageViewqadminmain);
        progressBar=view.findViewById(R.id.progressprofileadminmain);
        scrollView=view.findViewById(R.id.profilevisibleadminmain);
        phno1 = view.findViewById(R.id.phno11adminmain);
        constraintLayout = view.findViewById(R.id.editdetailsadminmain);
        email1 = view.findViewById(R.id.email11adminmain);
        name1 = view.findViewById(R.id.name11adminmain);
        email = view.findViewById(R.id.emailprofileadminmain);
        usersRef = FirebaseDatabase.getInstance().getReference("mainadmins").child(user.getUid());

        return view;
    }
}
