package com.example.java;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private RecyclerView recyclerView;
    private ProductlistAdaptor adaptor;
    private ArrayList<ProductDetails> productDetails;

    public history_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);

        bannerViewPager = view.findViewById(R.id.bannerViewPager);
        recyclerView=view.findViewById(R.id.productrecyculer);
        productDetails=new ArrayList<>();

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
