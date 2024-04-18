package com.example.java;
import android.annotation.SuppressLint;
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
public class signupactivity extends AppCompatActivity {
    EditText email,pass,confirm_pass,Name;
    Button register;
    FirebaseAuth auth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupactivity);
        email=findViewById(R.id.editTextemail);
        pass=findViewById(R.id.editTextpassword);
        confirm_pass=findViewById(R.id.editTextconfirmPassword);
        register=findViewById(R.id.signup);
        Name=findViewById(R.id.editTextname);
        auth=FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=Name.getText().toString();
                String email1=email.getText().toString();
                String pass1=pass.getText().toString();
                String passconfirm1=confirm_pass.getText().toString();
                if(TextUtils.isEmpty(email1) || TextUtils.isEmpty(pass1) || TextUtils.isEmpty(passconfirm1) || TextUtils.isEmpty(name)){
                    Toast.makeText(signupactivity.this, "Enter All details", Toast.LENGTH_SHORT).show();
                } else if (pass1.length()<7) {
                    Toast.makeText(signupactivity.this, "Password should be Eight Characters ", Toast.LENGTH_SHORT).show();
                }
                else if(!pass1.equals(passconfirm1)){
                    Toast.makeText(signupactivity.this, "Enter Same Passwords", Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.createUserWithEmailAndPassword(email1,pass1).addOnCompleteListener(signupactivity.this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(signupactivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(signupactivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


            }
        });

    }
}
