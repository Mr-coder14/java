package com.example.java;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signupactivity extends AppCompatActivity {
    String adminEmail = "abcd1234@gmail.com";

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            if (currentUser.getEmail().equals(adminEmail)) {
                startActivity(new Intent(signupactivity.this, Adminactivity.class));
                finish();
            } else {
                startActivity(new Intent(signupactivity.this, MainActivity.class));
                finish();
            }

        }
    }

    EditText email, pass, confirmPass, name, phone;
    Button register;
    FirebaseAuth auth;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupactivity);

        email = findViewById(R.id.editTextemail);
        pass = findViewById(R.id.editTextpassword);
        phone = findViewById(R.id.phno);
        confirmPass = findViewById(R.id.editTextconfirmPassword);
        register = findViewById(R.id.signup);
        name = findViewById(R.id.editTextname);

        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = name.getText().toString();
                final String userEmail = email.getText().toString();
                String userPass = pass.getText().toString();
                String userPhone = phone.getText().toString();
                String userConfirmPass = confirmPass.getText().toString();

                if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPass) || TextUtils.isEmpty(userConfirmPass) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPhone)) {
                    Toast.makeText(signupactivity.this, "Enter all details", Toast.LENGTH_SHORT).show();
                } else if (userPass.length() < 6) {
                    Toast.makeText(signupactivity.this, "Password should be minimum 6 characters", Toast.LENGTH_SHORT).show();
                } else if (!userPass.equals(userConfirmPass)) {
                    Toast.makeText(signupactivity.this, "Enter same passwords", Toast.LENGTH_SHORT).show();
                } else if (userEmail.equals(adminEmail)) {
                    createUser(adminEmail, userPass, userName, userPhone, true);
                } else {
                    createUser(userEmail, userPass, userName, userPhone, false);
                }
            }
        });
    }

    private void createUser(final String email, String password, final String name, final String phone, final boolean isAdmin) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(signupactivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        String userId = user.getUid();
                        User newUser = new User(name, email, phone);
                        usersRef.child(userId).setValue(newUser);
                    }
                    Toast.makeText(signupactivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                    Intent intent;
                    if (isAdmin) {
                        intent = new Intent(signupactivity.this, Adminactivity.class);
                    } else {
                        intent = new Intent(signupactivity.this, MainActivity.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(signupactivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
