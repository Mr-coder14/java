package com.example.java;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class history_fragment extends Fragment implements BannerAdapter.OnBannerClickListener{
    private ViewPager2 bannerViewPager;
    private LinearLayout tippencil,drafter,aenote,calculator,graph;
    private RecyclerView recyclerView;
    private TextView allproducts;
    private ProductlistAdaptor adaptor;
    private ArrayList<ProductDetails> productDetails;
    private ProductDetails pr;
    private LinearLayout cart;
    private EditText editText;

    public history_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);

        bannerViewPager = view.findViewById(R.id.bannerViewPager);
        recyclerView=view.findViewById(R.id.productrecyculer);
        productDetails=new ArrayList<>();
        tippencil=view.findViewById(R.id.onclicktippencil);
        graph=view.findViewById(R.id.onclickgraph);
        editText=view.findViewById(R.id.tyu);
        aenote=view.findViewById(R.id.onclicka3note);
        drafter=view.findViewById(R.id.onclickdrafter);
        calculator=view.findViewById(R.id.onclickcalculator);
        cart=view.findViewById(R.id.mycarthome);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AllProducts.class));
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Mycart.class));
            }
        });


        View cartIconWithBadge = view.findViewById(R.id.cart_icon_with_badge);
        cartIconWithBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Mycart.class));
            }
        });
        fetchCartItemCount();
        allproducts=view.findViewById(R.id.allproducts);
        allproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),AllProducts.class));
            }
        });



        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pr=new ProductDetails("GRAPH NOTE BOOK - Practice Map 100 PAGES - A4 SIZE","120",R.drawable.graphh,1);
                Intent intent = new Intent(getContext(), Productpreviewa.class);
                intent.putExtra("product", pr);
                startActivity(intent);

            }
        });

        calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pr=new ProductDetails("Casio FX-991ES Plus Second Edition Scientific Calculator","750",R.drawable.calculatorr,1);
                Intent intent = new Intent(getContext(), Productpreviewa.class);
                intent.putExtra("product", pr);
                startActivity(intent);

            }
        });

        aenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pr=new ProductDetails("A3 Sketch Book/Art Book/Drawing Book","300",R.drawable.athreenotee,1);
                Intent intent = new Intent(getContext(), Productpreviewa.class);
                intent.putExtra("product", pr);
                startActivity(intent);

            }
        });

        drafter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pr=new ProductDetails("ORFORX Mini Drafter with Heavy Mild Steel Rod & Shatterproof Scale for Engineering Student With Protective Cover (Blue)","800",R.drawable.drafter1,1);
                Intent intent = new Intent(getContext(), Productpreviewa.class);
                intent.putExtra("product", pr);
                startActivity(intent);

            }
        });

        tippencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pr=new ProductDetails("Mechanical Pencils with 1 Tube Lead, 0.7mm Tip","15",R.drawable.tippencil,1,"Name : Faber-Castell Tri-Click Mechanical Pencils with 1 Tube Lead, 0.7mm Tip\n" +
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
                        "Set includes mechanical pencil with one tube of lead (0.7mm)");
                Intent intent = new Intent(getContext(), Productpreviewa.class);
                intent.putExtra("product", pr);
                startActivity(intent);

            }
        });

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
        adaptor=new ProductlistAdaptor(productDetails,getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adaptor);


        List<BannerItem> banners = new ArrayList<>();
        banners.add(new BannerItem("Combo Offer!", "Blue Pen-3,Black-1", "Buy Now", Color.parseColor("#FFE4E1"),R.drawable.pencombo ));
        banners.add(new BannerItem("Combo Offer!", "Tip Pencil,Box,Scale,Eraser", "Shop Now", Color.parseColor("#E1F5FE"), R.drawable.pencilcombo));
        banners.add(new BannerItem("Drafter Combo","Drafter,A3 Note","Get Now",Color.parseColor("#f0df60"),R.drawable.draftercombo));
        BannerAdapter bannerAdapter = new BannerAdapter(banners,this);
        bannerViewPager.setAdapter(bannerAdapter);



        return view;
    }
    private void updateCartBadge(int itemCount) {
        View view = getView();
        if (view == null) {

            return;
        }

        View cartIconWithBadge = view.findViewById(R.id.cart_icon_with_badge);
        if (cartIconWithBadge == null) {

            return;
        }

        TextView badgeTextView = cartIconWithBadge.findViewById(R.id.cart_badge);
        if (badgeTextView == null) {

            return;
        }

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
                if (isAdded()) {
                    updateCartBadge(itemCount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBannerClick(BannerItem bannerItem) {
        if(bannerItem.getButtonText().equals("Buy Now")){
            startActivity(new Intent(getContext(),ComboOfferpen.class));
        }else if(bannerItem.getButtonText().equals("Shop Now")){
            startActivity(new Intent(getContext(), Combopencil.class));
        }else {
            startActivity(new Intent(getContext(),ComboDrafter.class));
        }

    }
}
