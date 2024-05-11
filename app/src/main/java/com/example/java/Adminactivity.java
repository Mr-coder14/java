package com.example.java;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Adminactivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    Button btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminactivity);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        btn=findViewById(R.id.adminlgout);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user!=null){
                    auth.signOut();
                    startActivity(new Intent(Adminactivity.this, loginactivity.class));
                    finish();
                }
            }
        });


    }
}