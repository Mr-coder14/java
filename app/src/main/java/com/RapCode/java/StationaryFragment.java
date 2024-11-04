package com.RapCode.java;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.RapCode.java.recyculer.BannerAdapter;
import com.RapCode.java.recyculer.BannerItem;
import com.RapCode.java.recyculer.ProductDetails;
import com.RapCode.java.recyculer.ProductlistAdaptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StationaryFragment extends Fragment implements BannerAdapter.OnBannerClickListener {
    private ViewPager2 bannerViewPager;
    private RecyclerView recyclerView;
    private Handler bannerHandler;
    private Runnable bannerRunnable;
    private static final int BANNER_DELAY_MS = 2000;
    private ProductlistAdaptor adaptor;
    private ArrayList<ProductDetails> productDetails;
    private LinearLayout tippencil, drafter, aenote, calculator, graph, book;
    private TextView allproducts;
    private LinearLayout cart;
    private LinearLayout editText;
    private FirebaseAuth auth;
    private DatabaseReference cartRef;
    private View cartIconWithBadge;
    private ProgressBar progressBar;
    private LinearLayout contentLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_stationary, container, false);


        bannerViewPager = view.findViewById(R.id.bannerViewPager);
        recyclerView = view.findViewById(R.id.productrecyculer);
        tippencil = view.findViewById(R.id.onclicktippencil);
        graph = view.findViewById(R.id.onclickgraph);
        editText = view.findViewById(R.id.tyu);
        aenote = view.findViewById(R.id.onclicka3note);
        drafter = view.findViewById(R.id.onclickdrafter);
        calculator = view.findViewById(R.id.onclickcalculator);
        cart = view.findViewById(R.id.mycarthome);
        allproducts = view.findViewById(R.id.allproducts);
        cartIconWithBadge = view.findViewById(R.id.cart_icon_with_badge);
        book = view.findViewById(R.id.onclickbook);
        progressBar = view.findViewById(R.id.progress_barvbn);
        contentLayout = view.findViewById(R.id.content_layout);

        productDetails = new ArrayList<>();
        adaptor = new ProductlistAdaptor(productDetails, getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adaptor);

        book.setOnClickListener(v -> startActivity(new Intent(getActivity(), BookFormApplication.class)));

        showLoading();
        setupListeners();
        initializeFirebase();
        loadBanners();
        loadProducts();
        setupAutoSwipeBanners();

        return view;
    }

    private void setupListeners() {
        editText.setOnClickListener(v -> startActivity(new Intent(getActivity(), AllProducts.class)));
        cart.setOnClickListener(v -> startActivity(new Intent(getActivity(), Mycart.class)));
        cartIconWithBadge.setOnClickListener(v -> startActivity(new Intent(getActivity(), Mycart.class)));
        allproducts.setOnClickListener(v -> startActivity(new Intent(getActivity(), AllProducts.class)));
        setupCategoryListeners();
    }

    private void setupCategoryListeners() {
        graph.setOnClickListener(v -> openProductPreview("GRAPH NOTE BOOK - Practice Map 100 PAGES - A4 SIZE", "120", R.drawable.graphh));
        calculator.setOnClickListener(v -> openProductPreview("Caltrix CX-991S Scientific Calculator", "600", R.drawable.caltrix, "Brand\tCaltrix\nCalculator Type\tEngineering/Scientific\nPower Source\tBattery Powered\nModel Name\tCX 991S\nMaterial\tPlastic"));
        aenote.setOnClickListener(v -> openProductPreview("A3 Sketch Book/Art Book/Drawing Book", "300", R.drawable.athreenotee));
        drafter.setOnClickListener(v -> openProductPreview("ORFORX Mini Drafter with Heavy Mild Steel Rod & Shatterproof Scale for Engineering Student With Protective Cover (Blue)", "800", R.drawable.drafter1));
        tippencil.setOnClickListener(v -> openProductPreview("Mechanical Pencils with 1 Tube Lead, 0.7mm Tip", "15", R.drawable.tippencil, "Name : Faber-Castell Tri-Click Mechanical Pencils with 1 Tube Lead, 0.7mm Tip\nBrand : Faber Castell\nNet Quantity (N) : 1\nErgonomic triangular grip zone\nRetractable metal sleeve-pocket safe\nTip size : 0.7mm\nSet includes mechanical pencil with one tube of lead (0.7mm)"));
    }

    private void openProductPreview(String name, String price, int imageResource) {
        openProductPreview(name, price, imageResource, null);
    }

    private void openProductPreview(String name, String price, int imageResource, String description) {
        ProductDetails pr = new ProductDetails(name, price, imageResource, 1, description);
        Intent intent = new Intent(getActivity(), Productpreviewa.class);
        intent.putExtra("product", pr);
        startActivity(intent);
    }

    private void initializeFirebase() {
        auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        cartRef = FirebaseDatabase.getInstance().getReference("userscart").child(userId);
        fetchCartItemCount();
    }

    private void loadBanners() {
        List<BannerItem> banners = new ArrayList<>();
        banners.add(new BannerItem("Combo Offer!", "Blue Pen-3,Black-1", "Buy Now", Color.parseColor("#FFE4E1"), R.drawable.pencombo));
        banners.add(new BannerItem("Combo Offer!", "Tip Pencil,Box,Scale,Eraser", "Shop Now", Color.parseColor("#E1F5FE"), R.drawable.pencilcombo));
        banners.add(new BannerItem("Drafter Combo", "Drafter,A3 Note", "Get Now", Color.parseColor("#f0df60"), R.drawable.draftercombo));

        BannerAdapter bannerAdapter = new BannerAdapter(banners, this);
        bannerViewPager.setAdapter(bannerAdapter);
    }

    private void loadProducts() {
        new Handler().postDelayed(() -> {
            productDetails.clear();
            productDetails.add(new ProductDetails("Stylish X3 Ball Pen - Blue (0.7mm)","10",R.drawable.stylishpenblue));
            productDetails.add(new ProductDetails("Stylish X3 Ball Pen - Black (0.7mm)","10",R.drawable.stylishblackpen));
            productDetails.add(new ProductDetails("Stylish X3 Ball Pen - Blue (0.7mm)","7",R.drawable.stylishpenblue));
            productDetails.add(new ProductDetails("Stylish X3 Ball Pen - Black (0.7mm)","7",R.drawable.stylishblackpen));
            productDetails.add(new ProductDetails("A3 Sketch Book/Art Book/Drawing Book(40 pages)","60",R.drawable.athreenotee));
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
            productDetails.add(new ProductDetails("Bipolar Graph (100 sheets)","140",R.drawable.bipolar,1));
            productDetails.add(new ProductDetails("Tip Bpx Camlin Kokuyo 0.7mm Klick Lead Tube","5",R.drawable.tipbox,1,"Leads are tough, smooth and dark\n" +
                    "0.7mm B leads\n" +
                    "High polymer fine leads for smooth writing"));
            productDetails.add(new ProductDetails("Kokuyo Camlin Exam Scale Broad 30cm Ruler ","10",R.drawable.scale,1,"Camlin Exam portfolio of scales are the perfect tools for high precision and accuracy. Smooth taped edges that lie flat on the surface to give a sharp line. Transparent body"));
            productDetails.add(new ProductDetails("White Apsara Eraser","5",R.drawable.eraser,1,"As a quality focused firm, we are engaged in offering a high quality range of Apsara Eraser."));
            productDetails.add(new ProductDetails("ORFORX Mini Drafter with Heavy Mild Steel Rod & Shatterproof Scale for Engineering Student With Protective Cover (Blue)","330",R.drawable.drafter1));
            productDetails.add(new ProductDetails("A4 Sheet Bundle COPIER PAPER 80 GSM 500 SHEETS","280",R.drawable.afoursheet,1,"Brand\tTNPL\n" +
                    "Colour\tWhite\n" +
                    "Item Weight\t80 Grams\n" +
                    "Paper Finish\tGlossy\n" +
                    "Sheet Size\tA4"));
            productDetails.add(new ProductDetails("A4 Sheet Bundle COPIER PAPER 70 GSM 500 SHEETS","270",R.drawable.afoursheet,1,"Brand\tTNPL\n" +
                    "Colour\tWhite\n" +
                    "Item Weight\t80 Grams\n" +
                    "Paper Finish\tGlossy\n" +
                    "Sheet Size\tA4"));


            adaptor.notifyDataSetChanged();
            showContent();
        }, 1000);
    }

    private void fetchCartItemCount() {
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int itemCount = (int) dataSnapshot.getChildrenCount();
                updateCartBadge(itemCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
    private void setupAutoSwipeBanners() {

        bannerHandler = new Handler(Looper.getMainLooper());
        bannerRunnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = (bannerViewPager.getCurrentItem() + 1) % bannerViewPager.getAdapter().getItemCount();
                bannerViewPager.setCurrentItem(nextItem, true);
                bannerHandler.postDelayed(this, BANNER_DELAY_MS);
            }
        };
        bannerHandler.postDelayed(bannerRunnable, BANNER_DELAY_MS);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bannerHandler != null) {
            bannerHandler.removeCallbacks(bannerRunnable);
        }
    }

    private void updateCartBadge(int itemCount) {
        TextView badgeTextView = cartIconWithBadge.findViewById(R.id.cart_badge);
        if (badgeTextView != null) {
            badgeTextView.setVisibility(itemCount > 0 ? View.VISIBLE : View.GONE);
            badgeTextView.setText(String.valueOf(itemCount));
        }
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);
    }

    private void showContent() {
        progressBar.setVisibility(View.GONE);
        contentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBannerClick(BannerItem bannerItem) {
        Intent intent;
        switch (bannerItem.getButtonText()) {
            case "Buy Now":
                intent = new Intent(getActivity(), ComboOfferpen.class);
                break;
            case "Shop Now":
                intent = new Intent(getActivity(), Combopencil.class);
                break;
            default:
                intent = new Intent(getActivity(), ComboDrafter.class);
                break;
        }
        startActivity(intent);
    }
}
