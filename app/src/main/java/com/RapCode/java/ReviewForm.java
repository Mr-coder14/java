package com.RapCode.java;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.RapCode.java.recyculer.ReviewAdaptor;

public class ReviewForm extends AppCompatActivity {
    private ImageButton backbtn;
    private RecyclerView productRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_form);
        productRecyclerView = findViewById(R.id.reviewrecyculer);
        backbtn=findViewById(R.id.backbtnreview);
        Order order = getIntent().getParcelableExtra("order");
        ReviewAdaptor productAdapter = new ReviewAdaptor(order.getProducts(),this);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setAdapter(productAdapter);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}