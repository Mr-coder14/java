package com.example.java;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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


public class history_fragment extends Fragment {
    private ViewPager2 bannerViewPager;
    private LinearLayout pen,tippencil,drafter,aenote,calculator,graph;
    private RecyclerView recyclerView;
    private ProductlistAdaptor adaptor;
    private ArrayList<ProductDetails> productDetails;
    private ImageButton cart;

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

        productDetails.add(new ProductDetails("Calculator","750",R.drawable.calculator));
        productDetails.add(new ProductDetails("Graph","750",R.drawable.geaph));
        productDetails.add(new ProductDetails("Pencil","100",R.drawable.tippencil));
        productDetails.add(new ProductDetails("pen","100",R.drawable.pen));
        productDetails.add(new ProductDetails("A3 Note","500",R.drawable.athreenote));
        productDetails.add(new ProductDetails("Drafter","1000",R.drawable.drafter));
        adaptor=new ProductlistAdaptor(productDetails,getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adaptor);


        List<BannerItem> banners = new ArrayList<>();
        banners.add(new BannerItem("Get discount on fashion day", "Up to 50%", "Get Now", Color.parseColor("#FFE4E1"),R.drawable.vcc ));
        banners.add(new BannerItem("Summer sale!", "Up to 30%", "Shop Now", Color.parseColor("#E1F5FE"),R.drawable.vcc ));
        BannerAdapter bannerAdapter = new BannerAdapter(banners);
        bannerViewPager.setAdapter(bannerAdapter);

        return view;
    }
}
