package com.example.java;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Adminactivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    BottomNavigationView bottomNavigationView;
    Fragment fragment=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminactivity);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        bottomNavigationView=findViewById(R.id.bottomappbaradmin);
        fragment=new homeadmin();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containeradmin,fragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();
                if(id==R.id.Homebottomadmin){
                    fragment=new homeadmin();

                }
                if(id==R.id.ordersadmin){
                    fragment=new orders_activity_admin();

                }
                if(fragment!=null)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containeradmin,fragment).commit();
                    return true;
                }
                else {
                    return false;
                }
            }
        });

    }
}