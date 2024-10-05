package com.example.java;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class projectorderactivityuser extends AppCompatActivity {
    private ImageButton btn;

    private TextView productname, modelno, price, descriptionTextView,time,date,name,phno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectorderactivityuser);
        productname=findViewById(R.id.productname0);
        modelno=findViewById(R.id.modelno);
        price=findViewById(R.id.Price0);
        descriptionTextView=findViewById(R.id.Description0);
        time=findViewById(R.id.orderedtime0);
        date=findViewById(R.id.Ordereddate0);
        btn=findViewById(R.id.backbtnbookorders11);
        name=findViewById(R.id.nameuser0);
        phno=findViewById(R.id.phnouser0);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        String ProductName = getIntent().getStringExtra("productName");
        String modelno1 = getIntent().getStringExtra("modelno");
        String price2 = getIntent().getStringExtra("price");
        String description = getIntent().getStringExtra("description");
        String orderdate =getIntent().getStringExtra("orderdate");
        String ordertime =getIntent().getStringExtra("ordertime");
        String name3=getIntent().getStringExtra("name");
        String phno3=getIntent().getStringExtra("phno");


        productname.setText(ProductName);
        modelno.setText(modelno1);
        price.setText(price2);
        descriptionTextView.setText(description);
        time.setText(ordertime);
        date.setText(orderdate);
        name.setText(name3);
        phno.setText(phno3);
    }
}