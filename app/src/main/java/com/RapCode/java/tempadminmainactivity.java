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

import java.util.concurrent.TimeUnit;

import Tempadmin.Myorderstempadminfragment;
import Tempadmin.tempadminhomefragment;
import Tempadmin.tempadminprofilefragment;

public class tempadminmainactivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempadminmainactivity);
        auth=FirebaseAuth.getInstance();
        userid=auth.getCurrentUser().getUid();
        bottomNavigationView=findViewById(R.id.tadminbottom);
        PeriodicWorkRequest cleanupRequest =
                new PeriodicWorkRequest.Builder(CleanupWorker.class, 1, TimeUnit.DAYS)
                        .build();
        WorkManager.getInstance(this).enqueue(cleanupRequest);
        fragment=new tempadminhomefragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcpntainertadmin,fragment).commit();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.tadminhome){
                    fragment=new tempadminhomefragment();
                }
                if(id==R.id.tadminorders){
                    fragment=new Myorderstempadminfragment();
                }
                if(id==R.id.tadminprofile)
                {
                    fragment = new tempadminprofilefragment();
                }
                if(fragment!=null)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcpntainertadmin,fragment).commit();
                    return true;
                }
                else {
                    return false;
                }
            }
        });



    }
}