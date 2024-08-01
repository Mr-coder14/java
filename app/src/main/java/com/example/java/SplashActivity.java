package com.example.java;

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
    private String adminEmail = "abcd1234@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashactivity);

        logoImageView = findViewById(R.id.logoImageViewsplash);
        progressBar = findViewById(R.id.progressBarsplash);

        auth = FirebaseAuth.getInstance();
        tempadminsref = FirebaseDatabase.getInstance().getReference().child("tempadmin");


        loadTempAdmins();


        new Handler().postDelayed(this::checkUserAndRedirect, SPLASH_DURATION);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUserAndRedirect() {
        FirebaseUser currentUser = auth.getCurrentUser();
        Intent intent = null;
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                if (userEmail.equals(adminEmail)) {
                    intent=new Intent(SplashActivity.this, Adminactivity.class);
                } else if (tempadmins.contains(userEmail)) {
                    intent=new Intent(SplashActivity.this, tempadminmainactivity.class);
                } else {

                    intent=new Intent(SplashActivity.this, MainActivity.class);
                }
            }
        } else {
            intent=new Intent(SplashActivity.this, loginactivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}