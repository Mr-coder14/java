package com.example.java;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Paymentactivity extends AppCompatActivity {
    private TextView textView;
    private Orderconfirmuseractivity orderconfirmuseractivity;
    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentactivity);
        textView=findViewById(R.id.grandamty);
        Float t=getIntent().getExtras().getFloat("gt");
        orderconfirmuseractivity=new Orderconfirmuseractivity();
        btn=findViewById(R.id.orderbtnuser124);
        textView.setText("₹ "+String.valueOf(t));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderconfirmuseractivity.uploadPdfFiles();
            }
        });

    }

}