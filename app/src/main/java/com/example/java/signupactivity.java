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
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(signupactivity.this, MainActivity.class));
            finish();
        }
    }

    EditText email, pass, confirm_pass, Name,phno;
    Button register;
    FirebaseAuth auth;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupactivity);
        email = findViewById(R.id.editTextemail);
        pass = findViewById(R.id.editTextpassword);
        phno=findViewById(R.id.phno);
        confirm_pass = findViewById(R.id.editTextconfirmPassword);
        register = findViewById(R.id.signup);
        Name = findViewById(R.id.editTextname);
        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = Name.getText().toString();
                final String email1 = email.getText().toString();
                String pass1 = pass.getText().toString();
                String phnoo=phno.getText().toString();
                String passconfirm1 = confirm_pass.getText().toString();
                if (TextUtils.isEmpty(email1) || TextUtils.isEmpty(pass1) || TextUtils.isEmpty(passconfirm1) || TextUtils.isEmpty(name) || TextUtils.isEmpty(phnoo)) {
                    Toast.makeText(signupactivity.this, "Enter All details", Toast.LENGTH_SHORT).show();
                } else if (pass1.length() < 6) {
                    Toast.makeText(signupactivity.this, "Password should be Minimum 6 Characters ", Toast.LENGTH_SHORT).show();
                } else if (!pass1.equals(passconfirm1)) {
                    Toast.makeText(signupactivity.this, "Enter Same Passwords", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(email1, pass1).addOnCompleteListener(signupactivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();
                                if (user != null) {
                                    String userId = user.getUid();
                                    User newUser = new User(name, email1,phnoo);
                                    usersRef.child(userId).setValue(newUser);
                                }
                                Toast.makeText(signupactivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(signupactivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(signupactivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}