package com.RapCode.java;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000;
    private ImageView logoImageView;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private DatabaseReference tempadminsref;
    private ArrayList<String> tempadmins = new ArrayList<>();
    private ArrayList<String> admins = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashactivity);

        logoImageView = findViewById(R.id.logoImageViewsplash);
        progressBar = findViewById(R.id.progressBarsplash);
        admins.add("abcd1234@gmail.com");
        admins.add("saleem1712005@gmail.com");
        admins.add("jayaraman00143@gmail.com");

        auth = FirebaseAuth.getInstance();
        tempadminsref = FirebaseDatabase.getInstance().getReference().child("tempadmin");

        loadTempAdmins();
    }

    private void loadTempAdmins() {
        tempadminsref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tempadmins.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String tempAdminEmail = dataSnapshot.child("email").getValue(String.class);
                        if (tempAdminEmail != null) {
                            tempadmins.add(tempAdminEmail);
                        }
                    }
                }

                new Handler().postDelayed(() -> checkUserAndRedirect(), SPLASH_DURATION);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
                // Still proceed to check user and redirect
                new Handler().postDelayed(() -> checkUserAndRedirect(), SPLASH_DURATION);
            }
        });
    }

    private void checkUserAndRedirect() {
        FirebaseUser currentUser = auth.getCurrentUser();
        Intent intent;
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                if (admins.contains(userEmail)) {
                    intent = new Intent(SplashActivity.this, Adminactivity.class);
                } else if (tempadmins.contains(userEmail)) {
                    intent = new Intent(SplashActivity.this, tempadminmainactivity.class);
                } else {

                    intent = new Intent(SplashActivity.this, UsermainActivity.class);

                }
            } else {
                intent = new Intent(SplashActivity.this, loginactivity.class);
            }
        } else {
            intent = new Intent(SplashActivity.this, loginactivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}