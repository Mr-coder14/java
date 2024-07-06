package com.example.java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class tempadminmainactivity extends AppCompatActivity {
    Button lgbtn;
    FirebaseAuth auth;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempadminmainactivity);
        auth=FirebaseAuth.getInstance();
        userid=auth.getCurrentUser().getUid();
        lgbtn=findViewById(R.id.logoutbtntempadmin);
        lgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userid!=null){
                    auth.signOut();
                    startActivity(new Intent(tempadminmainactivity.this, loginactivity.class));
                    finish();

                }
            }
        });



    }
}