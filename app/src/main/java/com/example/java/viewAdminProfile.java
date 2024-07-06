package com.example.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class viewAdminProfile extends AppCompatActivity {
    FirebaseAuth auth;
    private CircleImageView circleImageView;
    private ImageButton backtbn;
    TextView email, name, email1, name1, phno1;

    ProgressBar progressBar;
    ScrollView scrollView;
    FirebaseUser user;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_admin_profile);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        name = findViewById(R.id.profilename11);
        circleImageView=findViewById(R.id.shapeableImageViewq11);
        progressBar=findViewById(R.id.progressprofile11);
        scrollView=findViewById(R.id.profilevisible11);
        phno1 = findViewById(R.id.phno1111);
        email1 = findViewById(R.id.email1111);
        name1 = findViewById(R.id.name1111);
        email = findViewById(R.id.emailprofile11);
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        backtbn=findViewById(R.id.back_btnadminadminprofile1);
        String em=getIntent().getStringExtra("model");

        backtbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        usersRef = FirebaseDatabase.getInstance().getReference("tempadmin").child(em);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User userData = dataSnapshot.getValue(User.class);
                    if (userData != null) {
                        name.setText(userData.getName());
                        name1.setText(userData.getName());
                        phno1.setText(userData.getPhno());
                        email.setText(userData.getEmail());
                        usersRef.child("profileImageUrl").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()){
                                    String uri=task.getResult().getValue(String.class);
                                    if(this!=null){
                                        if(uri!=null){
                                            Glide.with(viewAdminProfile.this)
                                                    .load(uri)
                                                    .into(circleImageView);
                                        }else {
                                            Glide.with(viewAdminProfile.this)
                                                    .load(R.drawable.person3)
                                                    .into(circleImageView);
                                        }

                                    }

                                }
                            }
                        });
                        email1.setText(userData.getEmail());
                    }
                    progressBar.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }
        });




    }

}