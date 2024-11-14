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
        graph.setOnClickListener(v -> openProductPreview("GRAPH NOTE BOOK - Practice Map 100 PAGES - A4 SIZE","100",R.drawable.graphh,"\n" +
                "Colour: \tgreen\n" +
                        "Sheet Size: \tA4\n" +
                        "Brand: \tSVE SIDDHI VINAYAK ENTERPRISES\n" +
                        "Sheet Count: \t100\n" +
                        "Product Dimensions: \t10L x 12W Centimeters"));
        calculator.setOnClickListener(v -> openProductPreview("Casio FX-991MS Scientific Calculator, 401 Functions and 2-line Display","1165",R.drawable.casio991,"Style Name: 2nd Gen\n" +
                "Brand: \tCasio\n" +
                "Colour: \tBlack\n" +
                "Calculator Type: \tEngineering/Scientific\n" +
                "Power Source: \tBattery Powered\n" +
                "Screen Size: \t7 Inches"));
        aenote.setOnClickListener(v -> openProductPreview("A3 Drawing Book","80",R.drawable.athreenotee,"A drawing book in A3 size is a versatile tool for artists, students, and creative enthusiasts. The A3 format, measuring 297 x 420 mm (11.7 x 16.5 inches), provides a spacious canvas that’s ideal for detailed artwork, sketches, and technical drawings. Typically bound with a sturdy cover, an A3 drawing book often contains high-quality, smooth, or lightly textured paper designed to handle various media, such as pencils, charcoal, inks, and light watercolors."));
        drafter.setOnClickListener(v -> openProductPreview("ORFORX Mini Drafter with Heavy Mild Steel Rod & Shatterproof Scale for Engineering Student With Protective Cover (Blue)","350",R.drawable.drafter1,"A drafter, also known as a drafting machine or drafting instrument, is a precision tool used in technical drawing, architecture, engineering, and design. It's typically mounted on a drawing board and combines the functions of a protractor, ruler, and set square, allowing users to draw precise lines, angles, and geometric shapes with ease and accuracy"));
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
        banners.add(new BannerItem("Drafter Combo!", "Drafter,A3 Note", "Get Now", Color.parseColor("#f0df60"), R.drawable.draftercombo));
        banners.add(new BannerItem("Order Book!","Order your Favourite Book now!","Get Book",Color.parseColor("#FFE4E1"),R.drawable.bookimg));
        banners.add(new BannerItem("Order products!","Get the Products for Projects!","Get Products",Color.parseColor("#E1F5FE"),R.drawable.productimagee));


        BannerAdapter bannerAdapter = new BannerAdapter(banners, this);
        bannerViewPager.setAdapter(bannerAdapter);
    }

    private void loadProducts() {
        new Handler().postDelayed(() -> {
            productDetails.add(new ProductDetails("Casio FX-991MS Scientific Calculator, 401 Functions and 2-line Display","1165",R.drawable.casio991,1,"Style Name: 2nd Gen\n" +
                    "Brand: \tCasio\n" +
                    "Colour: \tBlack\n" +
                    "Calculator Type: \tEngineering/Scientific\n" +
                    "Power Source: \tBattery Powered\n" +
                    "Screen Size: \t7 Inches"));
            productDetails.add(new ProductDetails("Caltrix CX-991S Scientific Calculator","600",R.drawable.caltrix,1,"Brand\tCaltrix\n" +
                    "Calculator Type: \tEngineering/Scientific\n" +
                    "Power Source: \tBattery Powered\n" +
                    "Model Name: \tCX 991S\n" +
                    "Material: \tPlastic"));
            productDetails.add(new ProductDetails("GRAPH NOTE BOOK - Practice Map 100 PAGES - A4 SIZE","100",R.drawable.graphh,1,"\n" +
                    "Colour: \tgreen\n" +
                    "Sheet Size: \tA4\n" +
                    "Brand: \tSVE SIDDHI VINAYAK ENTERPRISES\n" +
                    "Sheet Count: \t100\n" +
                    "Product Dimensions: \t10L x 12W Centimeters"));
            productDetails.add(new ProductDetails("Hauser XO Ball Pen Black INK","10",R.drawable.xooblack,1,"\n" +
                    "Brand: \tGeneric\n" +
                    "Ink Colour: \tBlack\n" +
                    "Net Quantity: \t10.0 count\n" +
                    "Body Shape: \tRound\n" +
                    "Hand Orientation: \tAmbidextrous"));
            productDetails.add(new ProductDetails("Hauser XO Ball Pen Blue INK","10",R.drawable.xoblue,1,"\n" +
                    "Brand: \tGeneric\n" +
                    "Ink Colour: \tBlue\n" +
                    "Net Quantity: \t10.0 count\n" +
                    "Body Shape: \tRound\n" +
                    "Hand Orientation: \tAmbidextrous"));
            productDetails.add(new ProductDetails("Stylish X3 Ball Pen - Blue (0.7mm)","7",R.drawable.stylishpenblue,1,"Discover the perfect blend of style and functionality with the Stylish X3 Ball Pen. Designed for those who appreciate elegance and efficiency, this pen features an ergonomic grip for comfortable writing, a click-lock mechanism for ease of use, and an ultra-proof body for durability. Whether you're signing documents, jotting down notes, or sketching ideas, the 0.7mm TC ball point tip ensures smooth and precise writing every time. Plus, its leak-proof design guarantees a mess-free experience, making it a reliable companion for all your writing needs."));
            productDetails.add(new ProductDetails("Stylish X3 Ball Pen - Black (0.7mm)","7",R.drawable.stylishblackpen,1,"Discover the perfect blend of style and functionality with the Stylish X3 Ball Pen. Designed for those who appreciate elegance and efficiency, this pen features an ergonomic grip for comfortable writing, a click-lock mechanism for ease of use, and an ultra-proof body for durability. Whether you're signing documents, jotting down notes, or sketching ideas, the 0.7mm TC ball point tip ensures smooth and precise writing every time. Plus, its leak-proof design guarantees a mess-free experience, making it a reliable companion for all your writing needs."));
            productDetails.add(new ProductDetails("A3 Drawing Book","80",R.drawable.athreenotee,1,"A drawing book in A3 size is a versatile tool for artists, students, and creative enthusiasts. The A3 format, measuring 297 x 420 mm (11.7 x 16.5 inches), provides a spacious canvas that’s ideal for detailed artwork, sketches, and technical drawings. Typically bound with a sturdy cover, an A3 drawing book often contains high-quality, smooth, or lightly textured paper designed to handle various media, such as pencils, charcoal, inks, and light watercolors."));
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
            productDetails.add(new ProductDetails("Bipolar Graph (100 sheets)","100",R.drawable.bipolar,1,"A bipolar graph is a visual representation used to display data with two opposite scales or contrasting variables. It's particularly useful for showing how values fall along a spectrum between two extremes, such as positive and negative opinions, high and low values, or two competing characteristics."));
            productDetails.add(new ProductDetails("Tip Bpx Camlin Kokuyo 0.7mm Klick Lead Tube","5",R.drawable.tipbox,1,"Leads are tough, smooth and dark\n" +
                    "0.7mm B leads\n" +
                    "High polymer fine leads for smooth writing"));
            productDetails.add(new ProductDetails("Kokuyo Camlin Exam Scale Broad 30cm Ruler ","10",R.drawable.scale,1,"Camlin Exam portfolio of scales are the perfect tools for high precision and accuracy. Smooth taped edges that lie flat on the surface to give a sharp line. Transparent body"));
            productDetails.add(new ProductDetails("White Apsara Eraser","5",R.drawable.eraser,1,"As a quality focused firm, we are engaged in offering a high quality range of Apsara Eraser."));
            productDetails.add(new ProductDetails("ORFORX Mini Drafter with Heavy Mild Steel Rod & Shatterproof Scale for Engineering Student With Protective Cover (Blue)","350",R.drawable.drafter1,1,"A drafter, also known as a drafting machine or drafting instrument, is a precision tool used in technical drawing, architecture, engineering, and design. It's typically mounted on a drawing board and combines the functions of a protractor, ruler, and set square, allowing users to draw precise lines, angles, and geometric shapes with ease and accuracy"));
            productDetails.add(new ProductDetails("A4 Sheet Bundle COPIER PAPER 80 GSM 500 SHEETS","280",R.drawable.afoursheet,1,"Brand\tTNPL\n" +
                    "Colour: \tWhite\n" +
                    "Item Weight: \t80 Grams\n" +
                    "Paper Finish: \tGlossy\n" +
                    "Sheet Size: \tA4"));
            productDetails.add(new ProductDetails("A4 Sheet Bundle COPIER PAPER 70 GSM 500 SHEETS","270",R.drawable.afoursheet,1,"Brand\tTNPL\n" +
                    "Colour: \tWhite\n" +
                    "Item Weight: \t80 Grams\n" +
                    "Paper Finish: \tGlossy\n" +
                    "Sheet Size: \tA4"));
            productDetails.add(new ProductDetails("Classmate Long Size Notebook(A4) - 120 Pages (Ruled)","60",R.drawable.note,1,"\n" +
                    "Brand: \tClassmate\n" +
                    "Colour: \tWhite\n" +
                    "Theme: \tPlain\n" +
                    "Cover Material: \tsoft cover\n" +
                    "Style: \tindian"));
            productDetails.add(new ProductDetails("Classmate Long Size Notebook(A4) - 120 Pages (UnRuled)","60",R.drawable.note,1,"\n" +
                    "Brand: \tClassmate\n" +
                    "Colour: \tWhite\n" +
                    "Theme: \tPlain\n" +
                    "Cover Material: \tsoft cover\n" +
                    "Style: \tindian"));
            productDetails.add(new ProductDetails("Classmate Long Size Notebook(A4) - 60 Pages (Ruled)","30",R.drawable.note,1,"\n" +
                    "Brand: \tClassmate\n" +
                    "Colour: \tWhite\n" +
                    "Theme: \tPlain\n" +
                    "Cover Material: \tsoft cover\n" +
                    "Style: \tindian"));
            productDetails.add(new ProductDetails("Classmate Long Size Notebook(A4) - 60 Pages (UnRuled)","30",R.drawable.note,1,"\n" +
                    "Brand: \tClassmate\n" +
                    "Colour: \tWhite\n" +
                    "Theme: \tPlain\n" +
                    "Cover Material: \tsoft cover\n" +
                    "Style: \tindian"));
            productDetails.add(new ProductDetails("Classmate Small Size Notebook - 120 Pages (Ruled)","40",R.drawable.smallnote,1,"\n" +
                    "Brand: \tClassmate\n" +
                    "Colour: \tWhite\n" +
                    "Theme: \tPlain\n" +
                    "Cover Material: \tsoft cover\n" +
                    "Style: \tindian"));
            productDetails.add(new ProductDetails("Classmate Small Size Notebook - 120 Pages (UnRuled)","40",R.drawable.smallnote,1,"\n" +
                    "Brand: \tClassmate\n" +
                    "Colour: \tWhite\n" +
                    "Theme: \tPlain\n" +
                    "Cover Material: \tsoft cover\n" +
                    "Style: \tindian"));
            productDetails.add(new ProductDetails("ALIS Unisex Lab Coat/Apron Cotton White Colour for Medical Students, Chemistry Lab, Doctors, Nurses and Surgeon","500",R.drawable.labcourt,1,"Material \tcomposition100% Cotton\n" +
                    "Care:  \tinstructionsMachine Wash\n" +
                    "Country of Origin:  \tIndia"));
            productDetails.add(new ProductDetails("Kangaro No. 10 Stapler","60",R.drawable.stabler,1,"Attractive pack of stapler and staples\n" +
                    "Durable\n" +
                    "Color of the product delivered is subject to stock availability\n" +
                    "Product information provided by the seller on the Website is not exhaustive, please read the label on the physical product carefully for complete information provided by the manufacturer. For additional information, please contact the manufacturer."));
            productDetails.add(new ProductDetails("Shuban Documents File Folder Fs Poly-Plastic Certificate Organizer With Snap Button Closure For School Office Gazette File","20",R.drawable.files,1,"Colour:    Gazette File (Single type)-Mix\n" +
                    "Material:    Plastic\n" +
                    "Style:    Envelope\n" +
                    "Brand:    Shuban\n" +
                    "Sheet Size:    letter\n"));
            productDetails.add(new ProductDetails("FLAIR FC-991 MS FC-991 MS Scientific Calculator  (12 Digit)","670",R.drawable.flair,1,"\n" +
                    "Crafted with special features, this FC-991 MS Scientific from FLAIR is a perfect companion that will help you make lengthy calculations with great ease. Promising you with multiple benefits, this FC991MS Scientific calculator is the most useful tool for calculating your mathematical problems with great ease. An ideal tool for young students and professionals, this 12 digits calculator is designed with innovative technologies."));
            productDetails.add(new ProductDetails("Casio FX-991ES Plus-2nd Edition Scientific Calculator","1350",R.drawable.calculatorr,1,"\n" +
                    "Brand: \tCasio\n" +
                    "Colour: \tBlack\n" +
                    "Calculator Type: \tEngineering/Scientific\n" +
                    "Power Source: \tBattery Powered\n" +
                    "Screen Size: \t4 Inches"));


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
            case "Get Book":
                intent=new Intent(getActivity(), BookFormApplication.class);
                break;
            case "Get Products":
                intent=new Intent(getActivity(), Projectproductsfrormapplication.class);
                break;
            default:
                intent = new Intent(getActivity(), ComboDrafter.class);
                break;
        }
        startActivity(intent);
    }
}
