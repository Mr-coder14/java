package com.RapCode.java;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

public class Adminactivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminactivity);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        PeriodicWorkRequest cleanupRequest =
                new PeriodicWorkRequest.Builder(CleanupWorker.class, 1, TimeUnit.DAYS)
                        .build();
        WorkManager.getInstance(this).enqueue(cleanupRequest);
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