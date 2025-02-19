package com.RapCode.java;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 5000;
    private TextView textView;

    private FirebaseAuth auth;
    private LinearLayout layout;
    private DatabaseReference tempadminsref;
    private LottieAnimationView lottieAnimationView, lottieAnimationView1;
    private ArrayList<String> tempadmins = new ArrayList<>();
    private ArrayList<String> admins = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashactivity);


        lottieAnimationView1 = findViewById(R.id.shopss);
        layout=findViewById(R.id.rapcde1);
        textView = findViewById(R.id.jasatxt);
        lottieAnimationView = findViewById(R.id.shopdesign);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://rapcodetechsolutions.netlify.app/"));
                startActivity(browserIntent);
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        admins.add("saleem1712005@gmail.com");
        admins.add("jayaraman00143@gmail.com");

        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        textView.post(() -> textView.startAnimation(slideIn));
        lottieAnimationView1.post(() -> lottieAnimationView1.setAnimation(slideIn));

        lottieAnimationView.setAnimation(R.raw.shopdesign);
        lottieAnimationView1.setAnimation(R.raw.shops);
        lottieAnimationView1.playAnimation();
        lottieAnimationView.playAnimation();


        lottieAnimationView1.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                lottieAnimationView1.playAnimation();
            }
        });

        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                lottieAnimationView.playAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        auth = FirebaseAuth.getInstance();
        tempadminsref = FirebaseDatabase.getInstance().getReference().child("tempadmin");

        // Delay loading temp admins and checking user status
        new Handler().postDelayed(this::loadTempAdmins, SPLASH_DURATION);
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
                checkUserAndRedirect();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                checkUserAndRedirect();
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
