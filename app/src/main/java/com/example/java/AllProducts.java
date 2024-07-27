package com.example.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.recyculer.ProductDetails;
import com.example.java.recyculer.ProductlistAdaptor;

import java.util.ArrayList;

public class AllProducts extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductlistAdaptor adaptor;
    private ArrayList<ProductDetails> productDetails;
    private ImageView cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        recyclerView=findViewById(R.id.recyculerviewallproducts);
        productDetails= new ArrayList<>();
        cart=findViewById(R.id.mycartalldetails);
        productDetails.add(new ProductDetails("Calculator","750",R.drawable.calculator));
        productDetails.add(new ProductDetails("Graph","750",R.drawable.geaph));
        productDetails.add(new ProductDetails("Pencil","100",R.drawable.tippencil));
        productDetails.add(new ProductDetails("pen","100",R.drawable.pen));
        productDetails.add(new ProductDetails("A3 Note","500",R.drawable.athreenote));
        productDetails.add(new ProductDetails("Drafter","1000",R.drawable.drafter));
        adaptor=new ProductlistAdaptor(productDetails,this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adaptor);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllProducts.this, Mycart.class));
            }
        });


    }
}