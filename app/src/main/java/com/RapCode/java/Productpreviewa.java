package com.RapCode.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.RapCode.java.recyculer.ProductDetails;
import com.RapCode.java.recyculer.ReviewAdaptor1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Productpreviewa extends AppCompatActivity {
    private ImageButton back;
    private ImageView iamge;
    private ImageButton minus,plus;
    private TextView name,amt;
    private RecyclerView recyclerViewReviews;
    private ReviewAdaptor1 reviewAdapter;
    private String productname;
    private List<Review> reviewList = new ArrayList<>();
    private DatabaseReference reviewsRef;
    private LinearLayout cart1,gh;
    private FirebaseUser user;
    private ArrayList<String> admins=new ArrayList<>();
    private Button addtocart;
    private View cartIconWithBadge;
    private TextView badgeTextView;
    private TextView qty,discription;
    private ProductDetails currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productpreviewa);
        back=findViewById(R.id.backButtonproductview);
        gh=findViewById(R.id.qtyjkjii);

        iamge=findViewById(R.id.productImagevieww);
        discription=findViewById(R.id.discriptionview);

        addtocart=findViewById(R.id.addtocardview);
        user=FirebaseAuth.getInstance().getCurrentUser();
        minus=findViewById(R.id.minusqtyproductview);
        plus=findViewById(R.id.addqtyproductview);
        qty=findViewById(R.id.qtytxtproductview);
        name=findViewById(R.id.productnameview);
        cartIconWithBadge = findViewById(R.id.cart_icon_with_badge1);
        badgeTextView = cartIconWithBadge.findViewById(R.id.cart_badge);
        amt=findViewById(R.id.productamtview);
        admins.add("saleem1712005@gmail.com");
        admins.add("jayaraman00143@gmail.com");
        Intent intent = getIntent();
        currentProduct = (ProductDetails) intent.getSerializableExtra("product");
        productname=currentProduct.getProductname()
                .replace(".", "_")
                .replace("#", "_")
                .replace("$", "_")
                .replace("[", "_")
                .replace("]", "_");


        name.setText(currentProduct.getProductname());
        iamge.setImageResource(currentProduct.getProductimage());
        amt.setText("₹ "+currentProduct.getProductamt());
        qty.setText(String.valueOf(currentProduct.getQty()));
        discription.setText(currentProduct.getDiscription());
        cart1=findViewById(R.id.mycarthome1);
        if(admins.contains(user.getEmail())){
            cart1.setVisibility(View.GONE);
            gh.setVisibility(View.GONE);
            addtocart.setVisibility(View.GONE);
        }else {
            fetchCartItemCount();
        }

        cart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Productpreviewa.this, Mycart.class));
            }
        });

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
                        fetchCartItemCount();
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
        recyclerViewReviews = findViewById(R.id.recyluerreviews);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdaptor1(reviewList);
        recyclerViewReviews.setAdapter(reviewAdapter);
        reviewsRef = FirebaseDatabase.getInstance().getReference("ProductReview");

        fetchReviews();

    }
    private void fetchReviews() {
        reviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewList.clear();
                if(dataSnapshot.exists()){
                for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot u1 : reviewSnapshot.getChildren())
                        if (u1.getKey().equals(productname)) {
                            Review review = u1.getValue(Review.class);
                            reviewList.add(review);
                        }
                    reviewAdapter.notifyDataSetChanged();
                }
            }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Productpreviewa.this, "Failed to load reviews", Toast.LENGTH_SHORT).show();
            }
        });
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
                    // Handle error
                }
            });
        }
    }
