package com.example.java;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.java.recyculer.BannerAdapter;
import com.example.java.recyculer.BannerItem;
import com.example.java.recyculer.ProductDetails;
import com.example.java.recyculer.ProductlistAdaptor;

import java.util.ArrayList;
import java.util.List;


public class history_fragment extends Fragment implements BannerAdapter.OnBannerClickListener{
    private ViewPager2 bannerViewPager;
    private LinearLayout pen,tippencil,drafter,aenote,calculator,graph;
    private RecyclerView recyclerView;
    private TextView allproducts;
    private ProductlistAdaptor adaptor;
    private ArrayList<ProductDetails> productDetails;
    private ImageView cart;

    public history_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);

        bannerViewPager = view.findViewById(R.id.bannerViewPager);
        recyclerView=view.findViewById(R.id.productrecyculer);
        productDetails=new ArrayList<>();
        pen=view.findViewById(R.id.onclickpen);
        tippencil=view.findViewById(R.id.onclicktippencil);
        graph=view.findViewById(R.id.onclickgraph);
        aenote=view.findViewById(R.id.onclicka3note);
        drafter=view.findViewById(R.id.onclickdrafter);
        calculator=view.findViewById(R.id.onclickcalculator);
        cart=view.findViewById(R.id.mycarthome);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Mycart.class));
            }
        });
        allproducts=view.findViewById(R.id.allproducts);
        allproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),AllProducts.class));
            }
        });

        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Productpreviewa.class));
            }
        });

        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Productpreviewa.class));

            }
        });

        calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Productpreviewa.class));

            }
        });

        aenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Productpreviewa.class));

            }
        });

        drafter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Productpreviewa.class));

            }
        });

        tippencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Productpreviewa.class));

            }
        });

        productDetails.add(new ProductDetails("Casio FX-991ES Plus Second Edition Scientific Calculator","750",R.drawable.calculatorr));
        productDetails.add(new ProductDetails("GRAPH NOTE BOOK - Practice Map 100 PAGES - A4 SIZE","120",R.drawable.graphh));
        productDetails.add(new ProductDetails("Stylish X3|Blue Pen|0.7mm ","10",R.drawable.stylishpen));
        productDetails.add(new ProductDetails("A3 Sketch Book/Art Book/Drawing Book","300",R.drawable.athreenotee));
        productDetails.add(new ProductDetails("UNI MITSUBISHI PENCIL, 0.5mm Mechanical Pencil uni KURU TOGA High Grade Model ","100",R.drawable.tippencil));
        productDetails.add(new ProductDetails("ORFORX Mini Drafter with Heavy Mild Steel Rod & Shatterproof Scale for Engineering Student With Protective Cover (Blue)","800",R.drawable.drafter1));
        adaptor=new ProductlistAdaptor(productDetails,getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adaptor);


        List<BannerItem> banners = new ArrayList<>();
        banners.add(new BannerItem("Combo Offer!", "Blue Pen-3,Black-1", "Buy Now", Color.parseColor("#FFE4E1"),R.drawable.pencombo ));
        banners.add(new BannerItem("Combo Offer!", "Tip Pencil,Box,Scale,Eraser", "Buy Now", Color.parseColor("#E1F5FE"),R.drawable.pencilcombo ));
        BannerAdapter bannerAdapter = new BannerAdapter(banners,this);
        bannerViewPager.setAdapter(bannerAdapter);



        return view;
    }

    @Override
    public void onBannerClick(BannerItem bannerItem) {
        if(bannerItem.getDiscountText().equals("Blue Pen-3,Black-1")){
            Toast.makeText(getContext(), "pen offer", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "pencil offer", Toast.LENGTH_SHORT).show();
        }

    }
}
