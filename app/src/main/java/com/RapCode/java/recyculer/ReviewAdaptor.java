package com.RapCode.java.recyculer;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.RapCode.java.ComboOfferpen;
import com.RapCode.java.Combopencil;
import com.RapCode.java.Productpreviewa;
import com.RapCode.java.R;
import com.RapCode.java.Review;
import com.RapCode.java.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ReviewAdaptor extends RecyclerView.Adapter<ReviewAdaptor.ProductViewHolder>{
    private List<ProductDetails> products;
    private Context context;
    private String username,userid;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;

    public ReviewAdaptor(List<ProductDetails> products,Context context) {
        this.products = products;
        this.context=context;
        userid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("ProductReview");
        databaseReference1=FirebaseDatabase.getInstance().getReference().child("users").child(userid);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User userData = dataSnapshot.getValue(User.class);
                    if (userData != null) {
                        username = userData.getName();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @NonNull
    @Override
    public ReviewAdaptor.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reiviewtemplate, parent, false);
        return new ReviewAdaptor.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdaptor.ProductViewHolder holder, int position) {
        ProductDetails product = products.get(position);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.getProductimage()==R.drawable.pencombo) {
                    Intent intent = new Intent(context, ComboOfferpen.class);
                    intent.putExtra("comboProduct", product);
                    context.startActivity(intent);
                } else if (product.getProductimage()==R.drawable.pencilcombo) {
                    Intent intent = new Intent(context, Combopencil.class);
                    intent.putExtra("comboProduct", product);
                    context.startActivity(intent);

                } else {
                    Intent intent=new Intent(context, Productpreviewa.class);
                    intent.putExtra("product",product);
                    context.startActivity(intent);
                }
            }
        });
        holder.bind(product);
        holder.submitReviewButton.setOnClickListener(v -> {
            // Capture the current rating and feedback
            float rating = holder.ratingBar.getRating();
            String feedback = holder.feedbackEditText.getText().toString().trim();

            if (!TextUtils.isEmpty(feedback)) {
                // Prepare the data to upload to Firebase
                Review review = new Review(rating, feedback, username);

                // Upload the data under username and product name
                databaseReference.child(userid)
                        .child(product.getProductname()) // Assuming getProductname() returns a unique product name
                        .setValue(review)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Review submitted successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Failed to submit review.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(context, "Please enter feedback.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameText, productQuantityText, productPriceText;
        private ImageView iamge;
        private ConstraintLayout constraintLayout;
        private RatingBar ratingBar;
        private EditText feedbackEditText;
        private Button submitReviewButton;
        private TextView quantityMinusReview, quantityPlusReview;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameText = itemView.findViewById(R.id.productNameTextorder);
            productQuantityText = itemView.findViewById(R.id.productQuantityTextorder);
            productPriceText = itemView.findViewById(R.id.productPriceTextorder);
            iamge=itemView.findViewById(R.id.productImageorder);
            quantityMinusReview = itemView.findViewById(R.id.quantityMinusreviewform);
            quantityPlusReview = itemView.findViewById(R.id.quantityPlusreviewform);
            ratingBar = itemView.findViewById(R.id.ratingBarreviewform);
            feedbackEditText = itemView.findViewById(R.id.feedbackEditText);
            submitReviewButton = itemView.findViewById(R.id.submitreview);
            constraintLayout=itemView.findViewById(R.id.consclcick);
        }
        void bind(ProductDetails product) {
            productNameText.setText(product.getProductname());
            productQuantityText.setText(String.valueOf(product.getQty()));
            productPriceText.setText("Price: ₹" + product.getProductamt());
            iamge.setImageResource(product.getProductimage());
            quantityMinusReview.setOnClickListener(v -> {
                float currentRating = ratingBar.getRating();
                if (currentRating > 1) {
                    currentRating--;
                    ratingBar.setRating(currentRating);
                    product.setRating(currentRating);
                }
            });

            quantityPlusReview.setOnClickListener(v -> {
                float currentRating = ratingBar.getRating();
                if (currentRating < 5) {
                    currentRating++;
                    ratingBar.setRating(currentRating);
                    product.setRating(currentRating);
                }
            });

            // Optionally show a toast for rating change
            ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                if (fromUser) {
                    product.setRating(rating);
                    Toast.makeText(context, "Rating: " + rating, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
