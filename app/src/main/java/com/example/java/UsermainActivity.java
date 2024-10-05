package com.example.java;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsermainActivity extends AppCompatActivity {
    private int PERMISSION_REQUEST_CODE=100;
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    private DatabaseReference ordersRef;
    String userid;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermain);
        bottomNavigationView=findViewById(R.id.bottomappbarm);
        auth=FirebaseAuth.getInstance();
        fragment=new home_fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerm,fragment).commit();
        checkPermissions();
        userid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        ordersRef= FirebaseDatabase.getInstance().getReference().child("pdfs").child(userid);
        checkOrderPaidStatus();

        FirebaseApp.initializeApp(/*context=*/ this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());




        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.Homebottom){
                    fragment=new home_fragment();

                }

                if(id==R.id.Historybottom){
                    fragment=new shopchoosingactivity();

                }
                if(id==R.id.profilebottom)
                {
                    fragment = new profile_fragment();

                }
                if(fragment!=null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerm, fragment).commit();
                    return true;
                }
                else {
                    return false;
                }



            }
        });
    }
    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

    public void selectBottomNavItem(int historybottom) {
        bottomNavigationView.setSelectedItemId(historybottom);
    }
    private void checkOrderPaidStatus() {
       ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()){
                   for(DataSnapshot s:snapshot.getChildren()){
                       for(DataSnapshot q:s.getChildren()){
                           Boolean paid = q.child("paid").getValue(Boolean.class);
                           if (paid != null && !paid) {
                               q.getRef().removeValue();
                           }
                           else {

                           }
                       }
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }
    }
