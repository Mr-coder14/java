package com.RapCode.java;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Projectproductsfrormapplication extends AppCompatActivity {
    private EditText pname,pmodel,pprice,pdescription;
    private Button btn;
    private  String username,phno,userid;
    private ImageButton img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectproductsfrormapplication);
        pname=findViewById(R.id.editTextBookName1);
        pmodel=findViewById(R.id.editTextAuthorName1);
        pprice=findViewById(R.id.editTextPrice1);
        img=findViewById(R.id.backbtnbook1);
        btn=findViewById(R.id.buttonSubmitg1);
        pdescription=findViewById(R.id.editTextDescription1);
        userid=FirebaseAuth.getInstance().getUid();
        DatabaseReference d=FirebaseDatabase.getInstance().getReference().child("users").child(userid);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        d.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user =snapshot.getValue(User.class);
                    username=user.getName();
                    phno=user.getPhno();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    submitForm();
                }
            }
        });
    }
    private boolean validateForm() {
        boolean isValid = true;
        if (pname.getText().toString().trim().isEmpty()) {
            pname.setError("Product name is required");
            isValid = false;
        }
        if (pmodel.getText().toString().trim().isEmpty()) {
            pmodel.setError("Product Model is required");
            isValid = false;
        }
        return isValid;
    }
    private void submitForm() {
        String name = pname.getText().toString().trim();
        String model = pmodel.getText().toString().trim();
        String price = pprice.getText().toString().trim();
        String description = pdescription.getText().toString().trim();

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String orderDate = sdfDate.format(new Date());
        String orderTime = sdfTime.format(new Date());




        ProjectProduct product = new ProjectProduct(name,model,price, description, orderDate, orderTime,username,phno);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("projectsproducts");


        databaseReference.child(uid).child(name).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Projectproductsfrormapplication.this, "Book submitted successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Projectproductsfrormapplication.this,suceesanimation.class));
                    finish();
                } else {
                    Toast.makeText(Projectproductsfrormapplication.this, "Failed to submit book. Try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}