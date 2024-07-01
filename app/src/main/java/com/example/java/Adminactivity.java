package com.example.java;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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
        fragment=new homeadminmain();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containeradmin,fragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id=item.getItemId();
                if(id==R.id.Homebottomadmin){
                    fragment=new homeadminmain();

                }
                if(id==R.id.ordersadmin){
                    fragment=new ordersfragment_adminmain();

                }
                if(id==R.id.profileadmin){
                    fragment=new profileadminfragment();
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