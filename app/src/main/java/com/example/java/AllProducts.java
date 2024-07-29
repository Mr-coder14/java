package com.example.java;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.recyculer.ProductDetails;
import com.example.java.recyculer.ProductSearchAdapter;
import com.example.java.recyculer.ProductlistAdaptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllProducts extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductlistAdaptor adaptor;

    private ProductSearchAdapter searchAdapter;
    private AutoCompleteTextView searchallproducts;
    private View cartIconWithBadge;
    private TextView badgeTextView;
    private ArrayList<ProductDetails> productDetails;
    private LinearLayout cart1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        recyclerView=findViewById(R.id.recyculerviewallproducts);
        productDetails= new ArrayList<>();
        searchallproducts=findViewById(R.id.searchallproducts);
        cart1=findViewById(R.id.mycarthome2);
        cartIconWithBadge = findViewById(R.id.cart_icon_with_badge2);
        badgeTextView = cartIconWithBadge.findViewById(R.id.cart_badge);
        productDetails.add(new ProductDetails("Casio FX-991ES Plus Second Edition Scientific Calculator","750",R.drawable.calculatorr));
        productDetails.add(new ProductDetails("GRAPH NOTE BOOK - Practice Map 100 PAGES - A4 SIZE","120",R.drawable.graphh));
        productDetails.add(new ProductDetails("Stylish X3 Ball Pen - Blue (0.7mm)","10",R.drawable.stylishpenblue));
        productDetails.add(new ProductDetails("Stylish X3 Ball Pen - Black (0.7mm)","10",R.drawable.stylishblackpen));
        productDetails.add(new ProductDetails("A3 Sketch Book/Art Book/Drawing Book","300",R.drawable.athreenotee));
        productDetails.add(new ProductDetails("Mechanical Pencils with 1 Tube Lead, 0.7mm Tip","15",R.drawable.tippencil,1,"Name : Faber-Castell Tri-Click Mechanical Pencils with 1 Tube Lead, 0.7mm Tip\n" +
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
        productDetails.add(new ProductDetails("Tip Bpx Camlin Kokuyo 0.7mm Klick Lead Tube","5",R.drawable.tipbox,1,"Leads are tough, smooth and dark\n" +
                "0.7mm B leads\n" +
                "High polymer fine leads for smooth writing"));
        productDetails.add(new ProductDetails("Kokuyo Camlin Exam Scale Broad 30cm Ruler ","20",R.drawable.scale,1,"Camlin Exam portfolio of scales are the perfect tools for high precision and accuracy. Smooth taped edges that lie flat on the surface to give a sharp line. Transparent body"));
        productDetails.add(new ProductDetails("White Apsara Eraser","5",R.drawable.eraser,1,"As a quality focused firm, we are engaged in offering a high quality range of Apsara Eraser."));
        productDetails.add(new ProductDetails("ORFORX Mini Drafter with Heavy Mild Steel Rod & Shatterproof Scale for Engineering Student With Protective Cover (Blue)","800",R.drawable.drafter1));

        adaptor=new ProductlistAdaptor(productDetails,this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adaptor);
        searchAdapter = new ProductSearchAdapter(this, productDetails);
        searchallproducts.setAdapter(searchAdapter);

        fetchCartItemCount();

        cart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllProducts.this, Mycart.class));
            }
        });

        searchallproducts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterProducts(s.toString());

            }
        });

        searchallproducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductDetails selectedProduct = (ProductDetails) parent.getItemAtPosition(position);
                searchallproducts.setText(selectedProduct.getProductname());
                filterProducts(selectedProduct.getProductname());
            }
        });




    }
    private void filterProducts(String text) {
        ArrayList<ProductDetails> filteredList = new ArrayList<>();

        for (ProductDetails item : productDetails) {
            if (item.getProductname().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adaptor.filterList(filteredList);
    }
    private void updateCartBadge(int itemCount) {
        if (itemCount > 0) {
            badgeTextView.setVisibility(View.VISIBLE);
            badgeTextView.setText(String.valueOf(itemCount));
        } else {
            badgeTextView.setVisibility(View.GONE);
        }
    }

    private void fetchCartItemCount() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("userscart").child(userId);

        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int itemCount = (int) dataSnapshot.getChildrenCount();
                updateCartBadge(itemCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}