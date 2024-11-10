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

public class Combopencil extends AppCompatActivity {
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
        setContentView(R.layout.activity_combopencil);
        recyclerView = findViewById(R.id.recyculercombopencil);
        emptymsg = findViewById(R.id.empptytxtcombopencil);
        back = findViewById(R.id.backbtncombopencil);
        progressBar = findViewById(R.id.progressbarcombopencil);
        productDetailsList = new ArrayList<>();
        addtocart=findViewById(R.id.addtocartcombopencil);
        progressBar.setVisibility(View.VISIBLE);
        emptymsg.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        productDetailsList.add(new ProductDetails("Mechanical Pencils with 1 Tube Lead, 0.7mm Tip","15",R.drawable.tippencil,1,"Name : Faber-Castell Tri-Click Mechanical Pencils with 1 Tube Lead, 0.7mm Tip\n" +
                "\n" +
                "Brand : Faber Castell\n" +
                "\n" +
                "Net Quantity (N) : 1\n" +
                "\n" +
                "Ergonomic triangular grip zone\n" +
                "\n" +
                "Retractable metal sleeve-pocket safe\n" +
                "\n" +
                "Tip size : 0.7mm\n" +
                "\n" +
                "Set includes mechanical pencil with one tube of lead (0.7mm)"));
        productDetailsList.add(new ProductDetails("Tip Bpx Camlin Kokuyo 0.7mm Klick Lead Tube","5",R.drawable.tipbox,1,"Leads are tough, smooth and dark\n" +
                "0.7mm B leads\n" +
                "High polymer fine leads for smooth writing"));
        productDetailsList.add(new ProductDetails("Kokuyo Camlin Exam Scale Broad 30cm Ruler ","10",R.drawable.scale,1,"Camlin Exam portfolio of scales are the perfect tools for high precision and accuracy. Smooth taped edges that lie flat on the surface to give a sharp line. Transparent body"));
        productDetailsList.add(new ProductDetails("White Apsara Eraser","5",R.drawable.eraser,1,"As a quality focused firm, we are engaged in offering a high quality range of Apsara Eraser."));
        cpmbofferpen=new ProductDetails("Combo Offer Tip Pencil,Box,Scale,Eraser","35",R.drawable.pencilcombo,1);
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
                        Toast.makeText(Combopencil.this, "Item is already in the cart", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemAdded() {
                        Toast.makeText(Combopencil.this, "Product added to cart", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemAddFailed() {
                        Toast.makeText(Combopencil.this, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
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