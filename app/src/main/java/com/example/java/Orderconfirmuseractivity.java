package com.example.java;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.recyculer.pdflratelistApadtor;

import java.util.ArrayList;

public class Orderconfirmuseractivity extends AppCompatActivity {
    private ArrayList<String> fileNames;
    private RecyclerView recyclerView1;
    private pdflratelistApadtor apadtor;
    private float Deliverycharge;
    private  ArrayList<Uri> uris;
    private Activity activity;
    private ImageButton back;
    private preview_orderActivity activity1;
    private ArrayList<PDFDetails> pdfDetails;
    public  Float gt;
    private String notesst;
    private String orderid;
    private EditText notes;
    private Button Confirmorder;
    private TextView grandamt,subtotal, feedelivery, total;


    public  Orderconfirmuseractivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderconfirmuseractivity);

        recyclerView1 =findViewById(R.id.recyculerviewpdfs);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        activity=this;
        activity1=new preview_orderActivity();
        back=findViewById(R.id.back_btnuser12);
        subtotal = findViewById(R.id.subtotal1);
        feedelivery = findViewById(R.id.feedelivery1);
        total = findViewById(R.id.total1);

        Confirmorder=findViewById(R.id.orderbtnuser12);
        grandamt=findViewById(R.id.grandamt);
        Deliverycharge=15.0f;
        AppData appData = AppData.getInstance();
        uris = appData.getUris();
        fileNames = appData.getFileNames();
        orderid = appData.getOrderid();
        pdfDetails = appData.getPdfDetails();
        notes=findViewById(R.id.notesuser);
        notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                notesst=s.toString();
                if(apadtor!=null){
                    apadtor.updateNotes(notesst);
                }
            }
        });

        apadtor=new pdflratelistApadtor(activity,uris,fileNames,orderid,pdfDetails,notesst);
        recyclerView1.setAdapter(apadtor);
        gt= apadtor.getGrandtotal();
        subtotal.setText(String.format("₹ %.2f", gt));
        feedelivery.setText(String.format("₹ %.2f", Deliverycharge));
        Float tt=gt;//Deliverycharge;
        total.setText(String.format("₹ %.2f", tt));
        grandamt.setText(String.format("₹ %.2f", tt));




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Confirmorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadPdfFiles();
                Intent intent=new Intent(Orderconfirmuseractivity.this,Paymentactivity.class);
                intent.putExtra("gt",tt);
                startActivity(intent);


            }
        });
    }



    public void uploadPdfFiles() {


        for (int i = 0; i < uris.size(); i++) {

            RecyclerView.ViewHolder holder = recyclerView1.findViewHolderForAdapterPosition(i);
            if (holder instanceof pdflratelistApadtor.ViewHolder) {

                pdflratelistApadtor.ViewHolder viewHolder = (pdflratelistApadtor.ViewHolder) holder;


                viewHolder.uploadPdf();

            }
        }
    }

}