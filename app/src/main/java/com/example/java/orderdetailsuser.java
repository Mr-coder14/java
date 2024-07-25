package com.example.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.java.recyculer.orederpreviewadaptor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import Tempadmin.Processorderactivity;

public class orderdetailsuser extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    private static final String TAG = "OrderDetailsUser";
    private RecyclerView recyclerView;
    private ImageButton backbtn;
    private String orderid,userid;
    private DatabaseReference databaseReference,pdfsRef;

    private String grandtotal;
    private Button dbtn;
    private ProgressBar progressBar;
    private ArrayList<Fileinmodel>fileinmodels;
    private  TextView gt;
    private FirebaseAuth mAuth;
    private Query query;
    private ImageButton orderinfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordereddetailsuser);
        backbtn = findViewById(R.id.backuser1);
        dbtn=findViewById(R.id.downloadbtnuser1);
        orderinfo=findViewById(R.id.orderinfouser1);
        mAuth=FirebaseAuth.getInstance();
        userid=mAuth.getCurrentUser().getUid();

        recyclerView=findViewById(R.id.recyculerviewuser1);
        progressBar=findViewById(R.id.progressadmin11user);
        progressBar.setVisibility(View.VISIBLE);
        gt=findViewById(R.id.gtuser1);
        fileinmodels=new ArrayList<>();
        userid=mAuth.getCurrentUser().getUid();




        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        orderid=getIntent().getStringExtra("orderid");
        grandtotal=getIntent().getStringExtra("gt");
        gt.setText("₹ "+grandtotal);



        databaseReference=FirebaseDatabase.getInstance().getReference().child("pdfs");

        query=databaseReference;



        pdfsRef = FirebaseDatabase.getInstance().getReference().child("pdfs");
        pdfsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        if (orderSnapshot.getKey().equals(orderid)) {
                            for (DataSnapshot fileSnapshot : orderSnapshot.getChildren()) {
                                Fileinmodel pdfFile = fileSnapshot.getValue(Fileinmodel.class);
                                if (pdfFile != null) {
                                    fileinmodels.add(pdfFile);
                                }
                            }
                            break;
                        }}
                }
                displaypdfs();

                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(orderdetailsuser.this, "No files", Toast.LENGTH_SHORT).show();
            }
        });

        orderinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(orderdetailsuser.this, Processactivityuser.class);
                intent.putExtra("orderid2",orderid);
                intent.putExtra("gt2",grandtotal);
                startActivity(intent);
            }
        });









        /*dbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  downloadPdf(pdfuri.toString());
            }
        });*/

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    private void displaypdfs() {
        if(fileinmodels==null){
            Toast.makeText(this, "No files", Toast.LENGTH_SHORT).show();

        }else {
            orederpreviewadaptor adapter = new orederpreviewadaptor(fileinmodels,this);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }




}