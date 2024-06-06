package com.example.java;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class editdetails extends AppCompatActivity {
    private EditText editTextName, editTextPhno, editTextEmail;
    private Button buttonSave;
    private FirebaseAuth auth;
    private DatabaseReference usersRef;

    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_detailsactivity);
        editTextName=findViewById(R.id.editdetailsname);
        editTextEmail=findViewById(R.id.editdetailsemail);
        editTextPhno=findViewById(R.id.editdetailsphno);
        buttonSave=findViewById(R.id.savechanges);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        usersRef.child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    String name=task.getResult().getValue(String.class);
                    editTextName.setText(name);
                }
            }
        });
        usersRef.child("email").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    String email=task.getResult().getValue(String.class);
                    editTextEmail.setText(email);
                }
            }
        });
        usersRef.child("phno").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String phno = task.getResult().getValue(String.class);
                    editTextPhno.setText(phno);
                }
            }
        });
    }

    private void saveProfile() {
        String name = editTextName.getText().toString().trim();
        String phno = editTextPhno.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phno) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }


        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        if (currentUser != null) {


            currentUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(editdetails.this, "Updated email", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(editdetails.this, "Unable to change", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


        usersRef.child("name").setValue(name);
        usersRef.child("email").setValue(email);
        usersRef.child("phno").setValue(phno);
        Toast.makeText(editdetails.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }


}
