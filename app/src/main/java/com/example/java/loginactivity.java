package com.example.java;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class loginactivity extends AppCompatActivity {
    private ArrayList<String> admins=new ArrayList<>();

    private final ArrayList<String> tempadmins = new ArrayList<>();
    TextView rgbuttontxt;
    EditText emaillg, passlg;

    Button login;
    TextView forgotPassword;
    private DatabaseReference tempadminsref1;
    FirebaseAuth auth;
    private boolean isUserLoggedIn = false;
    private ProgressDialog progressDialog;
    private String userType = "";

    @Override
    protected void onStart() {
        super.onStart();
        admins.add("abcd1234@gmail.com");
        admins.add("saleem1712005@gmail.com");

        tempadminsref1 = FirebaseDatabase.getInstance().getReference().child("tempadmin1");
        tempadminsref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tempadmins.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String tepadminemail = dataSnapshot.child("email").getValue(String.class);
                        if (tepadminemail != null) {
                            tempadmins.add(tepadminemail);
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(loginactivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void redirectLoggedInUser() {
        if (isUserLoggedIn) {
            Intent intent;
            switch (userType) {
                case "admin":
                    intent = new Intent(loginactivity.this, Adminactivity.class);
                    break;
                case "tempadmin":
                    intent = new Intent(loginactivity.this, tempadminmainactivity.class);
                    break;
                default:
                    intent = new Intent(loginactivity.this, UsermainActivity.class);
                    break;
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            progressDialog.dismiss();
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);
        rgbuttontxt = findViewById(R.id.rgbuttontxt);
        emaillg = findViewById(R.id.email);
        passlg = findViewById(R.id.Password);
        auth = FirebaseAuth.getInstance();
        login = findViewById(R.id.btnlg);
        forgotPassword = findViewById(R.id.forget_password);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in, please wait...");
        progressDialog.setCancelable(false);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1 = emaillg.getText().toString();
                String pass1 = passlg.getText().toString();
                if (TextUtils.isEmpty(email1) || TextUtils.isEmpty(pass1)) {
                    Toast.makeText(loginactivity.this, "Enter All details", Toast.LENGTH_SHORT).show();
                } else if (pass1.length() < 6) {
                    Toast.makeText(loginactivity.this, "Incorrect Password ", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(email1, pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(loginactivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                                isUserLoggedIn = true;
                                if (admins.contains(email1)) {
                                    userType = "admin";
                                } else if (tempadmins.contains(email1)) {
                                    userType = "tempadmin";
                                } else {
                                    userType = "user";
                                }
                                redirectLoggedInUser();
                            } else {

                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    progressDialog.dismiss();
                                    Toast.makeText(loginactivity.this, "Invalid password or Email. Please try again.", Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(loginactivity.this, "Login failed " , Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });

        rgbuttontxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginactivity.this, signupactivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(loginactivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
                EditText emailBox = dialogView.findViewById(R.id.emailBox);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString();
                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                            Toast.makeText(loginactivity.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(loginactivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(loginactivity.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });
    }
}
