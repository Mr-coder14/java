package com.example.java;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class signupactivity extends AppCompatActivity {
    String adminEmail = "abcd1234@gmail.com";
    private ArrayList<String> tempadmins=new ArrayList<>();

    String tempadminemail;

    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference tempadminsref;
        tempadminsref =FirebaseDatabase.getInstance().getReference().child("tempadmin");
        tempadminsref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        tempadminemail =dataSnapshot.child("email").getValue(String.class);
                        tempadmins.add(tempadminemail);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            if (currentUser.getEmail().equals(adminEmail)) {
                startActivity(new Intent(signupactivity.this, Adminactivity.class));
                finish();
            }else if(tempadmins.contains(currentUser.getEmail())){
                startActivity(new Intent(signupactivity.this,tempadminmainactivity.class));
                finish();
            } else {
                startActivity(new Intent(signupactivity.this, MainActivity.class));
                finish();
            }

        }
    }

    EditText email, pass, confirmPass, name, phone;
    Spinner collegename;
    Button register;
    String clgnmae;
    private DatabaseReference tempadminsref1;
    FirebaseAuth auth;
    DatabaseReference usersRef,adminsref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupactivity);

        collegename=findViewById(R.id.clgname);
        email = findViewById(R.id.editTextemail);
        pass = findViewById(R.id.editTextpassword);
        phone = findViewById(R.id.phno);
        confirmPass = findViewById(R.id.editTextconfirmPassword);
        register = findViewById(R.id.signup);
        name = findViewById(R.id.editTextname);

        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        adminsref=FirebaseDatabase.getInstance().getReference().child("admins");
         tempadminsref1=FirebaseDatabase.getInstance().getReference().child("tempadmin1");

        collegename.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clgnmae=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = name.getText().toString();
                final String userEmail = email.getText().toString();
                String userPass = pass.getText().toString();
                String userPhone = phone.getText().toString();
                String userConfirmPass = confirmPass.getText().toString();
                String clgname1=clgnmae;

                if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPass) || TextUtils.isEmpty(userConfirmPass) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPhone) || TextUtils.isEmpty(clgname1)) {
                    Toast.makeText(signupactivity.this, "Enter all details", Toast.LENGTH_SHORT).show();
                } else if (userPass.length() < 6) {
                    Toast.makeText(signupactivity.this, "Password should be minimum 6 characters", Toast.LENGTH_SHORT).show();
                } else if (!userPass.equals(userConfirmPass)) {
                    Toast.makeText(signupactivity.this, "Enter same passwords", Toast.LENGTH_SHORT).show();
                } else if (userEmail.equals(adminEmail)) {
                    createUser(adminEmail, userPass, userName, userPhone, true,false,clgname1);
                }else if(tempadmins.contains(userEmail)){
                    createUser(userEmail,userPass,userName,userPhone,false,true,clgname1);
                }
                else {
                    createUser(userEmail, userPass, userName, userPhone, false,false,clgname1);
                }
            }
        });
    }

    private void createUser(final String email, String password, final String name, final String phone, final boolean isAdmin,final boolean istempadmin,String clganme) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(signupactivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        String userId = user.getUid();
                        User newUser = new User(name, email, phone, clganme);
                        Toast.makeText(signupactivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent;
                        if (isAdmin) {
                            adminsref.child(userId).setValue(newUser);
                            intent = new Intent(signupactivity.this, Adminactivity.class);
                        } else if(istempadmin) {
                            tempadminsref1.child(userId).setValue(newUser);
                            intent=new Intent(signupactivity.this, tempadminmainactivity.class);
                        }
                        else {
                            usersRef.child(userId).setValue(newUser);
                            intent = new Intent(signupactivity.this, MainActivity.class);
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(signupactivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
