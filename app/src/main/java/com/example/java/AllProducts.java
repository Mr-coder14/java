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
        productDetails.add(new ProductDetails("Casio FX-991ES Plus Second Edition Scientific Calculator","750",R.drawable.calculatorr));
        productDetails.add(new ProductDetails("GRAPH NOTE BOOK - Practice Map 100 PAGES - A4 SIZE","60",R.drawable.graphh));
        productDetails.add(new ProductDetails("Stylish X3|Blue Pen|0.7mm ","10",R.drawable.stylishpen));
        productDetails.add(new ProductDetails("A3 Sketch Book/Art Book/Drawing Book","300",R.drawable.athreenotee));
        productDetails.add(new ProductDetails("UNI MITSUBISHI PENCIL, 0.5mm Mechanical Pencil uni KURU TOGA High Grade Model ","100",R.drawable.tippencil));
        productDetails.add(new ProductDetails("ORFORX Mini Drafter with Heavy Mild Steel Rod & Shatterproof Scale for Engineering Student With Protective Cover (Blue)","800",R.drawable.drafter1));
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