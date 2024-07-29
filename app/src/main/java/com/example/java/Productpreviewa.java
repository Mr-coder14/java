package com.example.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.java.recyculer.ProductDetails;

public class Productpreviewa extends AppCompatActivity {
    private ImageButton back,mycart;
    private ImageView iamge;
    private ImageButton minus,plus;
    private TextView name,amt;
    private Button addtocart;
    private TextView qty,discription;
    private ProductDetails currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productpreviewa);
        back=findViewById(R.id.backButtonproductview);
        iamge=findViewById(R.id.productImagevieww);
        discription=findViewById(R.id.discriptionview);
        mycart=findViewById(R.id.mycartsproductpreview);
        addtocart=findViewById(R.id.addtocardview);
        minus=findViewById(R.id.minusqtyproductview);
        plus=findViewById(R.id.addqtyproductview);
        qty=findViewById(R.id.qtytxtproductview);
        name=findViewById(R.id.productnameview);

        amt=findViewById(R.id.productamtview);
        Intent intent = getIntent();
        currentProduct = (ProductDetails) intent.getSerializableExtra("product");

        name.setText(currentProduct.getProductname());
        iamge.setImageResource(currentProduct.getProductimage());
        amt.setText("₹ "+currentProduct.getProductamt());
        qty.setText(String.valueOf(currentProduct.getQty()));
        discription.setText(currentProduct.getDiscription());
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=currentProduct.getQty();
                if(count>1){
                    count--;
                    currentProduct.setQty(count);
                    qty.setText(String.valueOf(count));
                }

            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=currentProduct.getQty()+1;
                currentProduct.setQty(count);
                qty.setText(String.valueOf(count));
            }
        });


        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.getInstance().addItem(currentProduct, new cart.CartAddCallback() {
                    @Override
                    public void onItemAlreadyExists() {
                        Toast.makeText(Productpreviewa.this, "Item is already in the cart", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemAdded() {
                        Toast.makeText(Productpreviewa.this, "Product added to cart", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemAddFailed() {
                        Toast.makeText(Productpreviewa.this, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mycart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Productpreviewa.this, Mycart.class));
            }
        });
    }
}