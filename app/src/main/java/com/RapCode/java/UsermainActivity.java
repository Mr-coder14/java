package com.RapCode.java;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.TimeUnit;

public class UsermainActivity extends AppCompatActivity {
    private int PERMISSION_REQUEST_CODE=100;
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    private DatabaseReference ordersRef;
    private StorageReference storageReference;
    private String userid;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermain);
        bottomNavigationView=findViewById(R.id.bottomappbarm);
        storageReference= FirebaseStorage.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        PeriodicWorkRequest cleanupRequest =
                new PeriodicWorkRequest.Builder(CleanupWorker.class, 1, TimeUnit.DAYS)
                        .build();
        WorkManager.getInstance(this).enqueue(cleanupRequest);
        fragment=new StationaryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerm,fragment).commit();
        checkPermissions();
        userid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        ordersRef= FirebaseDatabase.getInstance().getReference().child("pdfs");
        checkOrderPaidStatus();

        FirebaseApp.initializeApp(/*context=*/ this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());




        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.Xeroxbottom){
                    fragment=new home_fragment();

                }

                if(id==R.id.Shopbottom){
                    fragment=new StationaryFragment();

                }
                if(id==R.id.profilebottom)
                {
                    fragment = new profile_fragment();

                }
                if(id==R.id.Bookandproductbottom){
                    fragment=new BookProductFragment();
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
                if (snapshot.exists()) {
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot pdfSnapshot : orderSnapshot.getChildren()) {
                            for (DataSnapshot fileSnapshot : pdfSnapshot.getChildren()) {
                                String orderId = fileSnapshot.child("orderid0").getValue(String.class);
                                Boolean paid = fileSnapshot.child("paid").getValue(Boolean.class);

                                if (paid != null && !paid) {
                                    fileSnapshot.getRef().removeValue();


                                    StorageReference orderRef = storageReference.child("pdfs/" + orderId);

                                    orderRef.listAll().addOnSuccessListener(listResult -> {
                                        for (StorageReference fileRef : listResult.getItems()) {
                                            fileRef.delete().addOnSuccessListener(aVoid -> {


                                            }).addOnFailureListener(e -> {

                                            });
                                        }
                                    }).addOnFailureListener(e -> {

                                    });
                                }
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
