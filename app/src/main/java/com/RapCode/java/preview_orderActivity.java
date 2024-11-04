package com.RapCode.java;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.RapCode.java.recyculer.PreviewAdapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.Toast;

import java.util.ArrayList;


public class preview_orderActivity extends AppCompatActivity   {
    private ImageButton backbtn;
    private Button btn;

    private RecyclerView recyclerView;
    private  ArrayList<Uri> uris;
    private PreviewAdapter adapter;
    private ArrayList<PDFDetails> arrayList;
    private ArrayList<String> fileNames;
    private Activity activity;
    private float gt;
    private String orderid;
    private AppData appData = AppData.getInstance();
    public preview_orderActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_order);
        backbtn=findViewById(R.id.backadmin);

        recyclerView = findViewById(R.id.recyclerpreviewactivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btn=findViewById(R.id.orderbtnadmin);
        activity=preview_orderActivity.this;




        uris = getIntent().getParcelableArrayListExtra("uris");
        fileNames = getIntent().getStringArrayListExtra("fileNames");

        if (uris != null && fileNames != null) {

            Uri[] uriArray = uris.toArray(new Uri[0]);
            String[] fileNameArray = fileNames.toArray(new String[0]);
            adapter = new PreviewAdapter(this,uriArray, fileNameArray);
            orderid = adapter.getOrderid();
            arrayList=adapter.getPdfDetailsList();

            appData.setUris(uris);
            appData.setFileNames(fileNames);
            appData.setOrderid(orderid);
            appData.setPdfDetails(arrayList);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Failed to load files.", Toast.LENGTH_SHORT).show();
        }

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitConfirmationDialog();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(preview_orderActivity.this, Orderconfirmuseractivity.class);
                startActivity(intent);

            }
        });
    }







    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to quit?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }
        if (adapter != null) {
            adapter.cancelPendingTasks();
        }
    }



}
