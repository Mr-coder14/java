package com.example.java;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.recyculer.Itemlistadaptormycart;
import com.example.java.recyculer.ProductDetails;

import java.util.ArrayList;

public class Mycart extends AppCompatActivity {
    private RecyclerView recyclerView;
    ImageButton back;
    private ArrayList<ProductDetails> productDetails;
    private Itemlistadaptormycart adaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycart);

        recyclerView=findViewById(R.id.recyculermycart);
        back=findViewById(R.id.backbtnmycart);
        productDetails=new ArrayList<>();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        productDetails.add(new ProductDetails("Drafter","608",R.drawable.pencilcombo,45));
        productDetails.add(new ProductDetails("Graph","34343",R.drawable.pencombo,90));
        adaptor=new Itemlistadaptormycart(productDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);

    }
}