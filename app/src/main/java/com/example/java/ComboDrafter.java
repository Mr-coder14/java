package com.example.java;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.recyculer.ProductDetails;
import com.example.java.recyculer.mylistcombopenadaptor;

import java.util.ArrayList;

public class ComboDrafter extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageButton back;
    private ProductDetails cpmbofferpen;
    private mylistcombopenadaptor adaptor;
    private ArrayList<ProductDetails> productDetailsList;
    private TextView emptymsg;
    private Button addtocart;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo_drafter);
        recyclerView = findViewById(R.id.recyculercombopen1);
        emptymsg = findViewById(R.id.empptytxtcombopen1);
        back = findViewById(R.id.backbtncombopen1);
        progressBar = findViewById(R.id.progressbarcombopen1);
        productDetailsList = new ArrayList<>();
        addtocart=findViewById(R.id.addtocartcombopen1);
        progressBar.setVisibility(View.VISIBLE);
        emptymsg.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        productDetailsList.add(new ProductDetails("Drafter ","330",R.drawable.drafte1,1));
        productDetailsList.add(new ProductDetails("A3 Note(40 pages)","60",R.drawable.athreenotee,1));
        cpmbofferpen=new ProductDetails("Combo Offer  Drafter,A3 Note(40 pages)","390",R.drawable.draftercombo,1);
        updateEmptyCartMessage();

        adaptor = new mylistcombopenadaptor(productDetailsList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.getInstance().addItem(cpmbofferpen, new cart.CartAddCallback() {
                    @Override
                    public void onItemAlreadyExists() {
                        Toast.makeText(ComboDrafter.this, "Item is already in the cart", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemAdded() {
                        Toast.makeText(ComboDrafter.this, "Product added to cart", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemAddFailed() {
                        Toast.makeText(ComboDrafter.this, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void updateEmptyCartMessage() {
        if (productDetailsList.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            emptymsg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            emptymsg.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}