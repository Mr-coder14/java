package com.example.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import javax.annotation.Nullable;

public class Paymentactivity extends AppCompatActivity {
    private TextView textView;
    private Orderconfirmuseractivity orderconfirmuseractivity;
    private final static int UPI_PAYMENT_REQUEST_CODE=1;
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPI_PAYMENT_REQUEST_CODE) {
            if (data != null) {
                String response = data.getStringExtra("response");
                if (response == null) response = "discard";

                String status = "";
                String approvalRefNo = "";
                String[] responseArray = response.split("&");
                for (String res : responseArray) {
                    String[] equalStr = res.split("=");
                    if (equalStr.length >= 2) {
                        if (equalStr[0].equalsIgnoreCase("Status")) {
                            status = equalStr[1].toLowerCase();
                        } else if (equalStr[0].equalsIgnoreCase("ApprovalRefNo") || equalStr[0].equalsIgnoreCase("txnRef")) {
                            approvalRefNo = equalStr[1];
                        }
                    }
                }

                if (status.equals("success")) {
                    // Payment was successful, open SuccessAnimation activity
                    Intent intent = new Intent(Paymentactivity.this, suceesanimation.class);
                    startActivity(intent);
                } else {
                    // Payment failed or was canceled, handle accordingly
                    //showError("Transaction failed or was canceled.");
                }
            } else {
                //showError("Transaction failed or was canceled.");
            }
        }
    }

}