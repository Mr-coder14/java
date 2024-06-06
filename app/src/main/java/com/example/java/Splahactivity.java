package com.example.java;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.java.Adminactivity;
import com.example.java.MainActivity;
import com.example.java.R;
import com.example.java.loginactivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splahactivity extends AppCompatActivity {
    String email = "abcd1234@gmail.com";
    FirebaseAuth auth;
    ProgressBar progressBar;
    private static final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splahactivity);
        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBarsplash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUserAndNavigate();
            }
        }, SPLASH_TIME_OUT);
    }

    private void checkUserAndNavigate() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            if (user.getEmail().equals(email)) {
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(Splahactivity.this, Adminactivity.class));
            } else {
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(Splahactivity.this, MainActivity.class));
            }
        } else {
            progressBar.setVisibility(View.GONE);
            startActivity(new Intent(Splahactivity.this, loginactivity.class));
        }
        finish();
    }
}
