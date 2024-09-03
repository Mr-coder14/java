package com.example.java;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class signupactivity extends AppCompatActivity {
    String adminEmail = "abcd1234@gmail.com";
    private ArrayList<String> tempadmins = new ArrayList<>();
    private ProgressDialog progressDialog;
    CheckBox termsCheckBox;
    String tempadminemail;
    private String adres="Paavai Engineering College,Pachal,Tamilnadu,637018";
    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference tempadminsref;
        tempadminsref = FirebaseDatabase.getInstance().getReference().child("tempadmin");
        tempadminsref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        tempadminemail = dataSnapshot.child("email").getValue(String.class);
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
            } else if (tempadmins.contains(currentUser.getEmail())) {
                startActivity(new Intent(signupactivity.this, tempadminmainactivity.class));
                finish();
            } else {
                startActivity(new Intent(signupactivity.this, UsermainActivity.class));
                finish();
            }

        }
    }

    EditText email, pass, confirmPass, name, phone;
    Button register;
    private DatabaseReference tempadminsref1;
    FirebaseAuth auth;
    DatabaseReference usersRef, adminsref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupactivity);

        email = findViewById(R.id.editTextemail);
        pass = findViewById(R.id.editTextpassword);
        phone = findViewById(R.id.phno);
        phone.setFilters(new InputFilter[]{phoneNumberFilter()});
        confirmPass = findViewById(R.id.editTextconfirmPassword);
        register = findViewById(R.id.signup);
        name = findViewById(R.id.editTextname);

        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        termsCheckBox = findViewById(R.id.texttermsandcontions);
        adminsref = FirebaseDatabase.getInstance().getReference().child("admins");
        tempadminsref1 = FirebaseDatabase.getInstance().getReference().child("tempadmin1");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(true);



        termsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 // Enable the button if the checkbox is checked
            }
        });
        termsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTermsAndConditionsDialog();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!termsCheckBox.isChecked()) {
                    Toast.makeText(signupactivity.this, "Please accept the terms and conditions to proceed.", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                } else {
                    progressDialog.show();
                    if (userEmail.equals(adminEmail)) {
                        createUser(adminEmail, userPass, userName, userPhone,"", true, false);
                    } else if (tempadmins.contains(userEmail)) {
                        createUser(userEmail, userPass, userName, userPhone,"", false, true);
                    } else {
                        createUser(userEmail, userPass, userName, userPhone,adres, false, false);
                    }
                }
            }
        });
    }

    private void createUser(final String email, String password, final String name, final String phone,String adres, final boolean isAdmin, final boolean istempadmin) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(signupactivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        String userId = user.getUid();
                        User newUser = new User(name, email, phone, userId,adres);
                        Toast.makeText(signupactivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent;
                        if (isAdmin) {
                            adminsref.child(userId).setValue(newUser);
                            intent = new Intent(signupactivity.this, Adminactivity.class);
                        } else if (istempadmin) {
                            tempadminsref1.child(userId).setValue(newUser);
                            intent = new Intent(signupactivity.this, tempadminmainactivity.class);
                        } else {
                            usersRef.child(userId).setValue(newUser);
                            intent = new Intent(signupactivity.this, UsermainActivity.class);
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(signupactivity.this, "An account with this email already exists. Please log in instead.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(signupactivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void showTermsAndConditionsDialog() {
        startActivity(new Intent(signupactivity.this,Termscondtions.class));
    }

    private InputFilter phoneNumberFilter() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                StringBuilder builder = new StringBuilder(dest);
                builder.replace(dstart, dend, source.subSequence(start, end).toString());
                if (!builder.toString().matches("^\\d{0,10}$")) {
                    if (source.length() == 0) {
                        return dest.subSequence(dstart, dend);
                    }
                    return "";
                }
                return null;
            }
        };
    }
}
