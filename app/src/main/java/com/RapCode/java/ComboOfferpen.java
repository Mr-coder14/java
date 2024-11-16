package com.RapCode.java;

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

import com.RapCode.java.recyculer.ProductDetails;
import com.RapCode.java.recyculer.mylistcombopenadaptor;

import java.util.ArrayList;

public class ComboOfferpen extends AppCompatActivity {
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
        setContentView(R.layout.activity_combo_offerpen);
        recyclerView = findViewById(R.id.recyculercombopen);
        emptymsg = findViewById(R.id.empptytxtcombopen);
        back = findViewById(R.id.backbtncombopen);
        progressBar = findViewById(R.id.progressbarcombopen);
        productDetailsList = new ArrayList<>();
        addtocart=findViewById(R.id.addtocartcombopen);
        progressBar.setVisibility(View.VISIBLE);
        emptymsg.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();

            }
        });

        productDetailsList.add(new ProductDetails("Hauser XO Ball Pen Blue INK","25",R.drawable.xoblue,3,"\n" +
                "Brand: \tGeneric\n" +
                "Ink Colour: \tBlue\n" +
                "Net Quantity: \t10.0 count\n" +
                "Body Shape: \tRound\n" +
                "Hand Orientation: \tAmbidextrous"));
        productDetailsList.add(new ProductDetails("Hauser XO Ball Pen Black INK","10",R.drawable.xooblack,1,"\n" +
                "Brand: \tGeneric\n" +
                "Ink Colour: \tBlack\n" +
                "Net Quantity: \t10.0 count\n" +
                "Body Shape: \tRound\n" +
                "Hand Orientation: \tAmbidextrous"));
        cpmbofferpen=new ProductDetails("Combo Offer  Blue Pen-3,Black-1","40",R.drawable.pencombo,1);
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
                        Toast.makeText(ComboOfferpen.this, "Item is already in the cart", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemAdded() {
                        Toast.makeText(ComboOfferpen.this, "Product added to cart", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemAddFailed() {
                        Toast.makeText(ComboOfferpen.this, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
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