package com.RapCode.java;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addadminactivity extends AppCompatActivity {
    private EditText editText, Username;
    private Button btn;
    private DatabaseReference Databaserefrence;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addadminactivity);

        editText= findViewById(R.id.adminemailadd);
        Username =findViewById(R.id.usernameaddadmin);
        btn=findViewById(R.id.btnadd);

        Databaserefrence=FirebaseDatabase.getInstance().getReference().child("tempadmin");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editText.getText().toString().trim();
                String username1= Username.getText().toString();
                if (!email.isEmpty() && !username1.isEmpty() ) {
                    progressDialog = new ProgressDialog(addadminactivity.this);
                    progressDialog.setMessage("Adding...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Databaserefrence.child(email.replace(".", ",")).child("email").setValue(email);
                            Databaserefrence.child(email.replace(".", ",")).child("name").setValue(username1);
                            progressDialog.dismiss();
                            editText.setText("");
                            Toast.makeText(addadminactivity.this, "Admin added successfully", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                }else {
                    Toast.makeText(addadminactivity.this, "Please enter an email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}