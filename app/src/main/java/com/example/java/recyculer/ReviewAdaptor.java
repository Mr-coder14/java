package com.example.java.recyculer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.ComboOfferpen;
import com.example.java.Combopencil;
import com.example.java.Productpreviewa;
import com.example.java.R;

import java.util.List;

public class ReviewAdaptor extends RecyclerView.Adapter<ReviewAdaptor.ProductViewHolder>{
    private List<ProductDetails> products;
    private Context context;

    public ReviewAdaptor(List<ProductDetails> products,Context context) {
        this.products = products;
        this.context=context;
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

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameText = itemView.findViewById(R.id.productNameTextorder);
            productQuantityText = itemView.findViewById(R.id.productQuantityTextorder);
            productPriceText = itemView.findViewById(R.id.productPriceTextorder);
            iamge=itemView.findViewById(R.id.productImageorder);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            constraintLayout=itemView.findViewById(R.id.consclcick);
        }
        void bind(ProductDetails product) {
            productNameText.setText(product.getProductname());
            productQuantityText.setText(String.valueOf(product.getQty()));
            productPriceText.setText("Price: ₹" + product.getProductamt());
            iamge.setImageResource(product.getProductimage());
            ratingBar.setOnRatingBarChangeListener(null);
            ratingBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float touchPositionX = v.getX();
                    float widthOfStar = ratingBar.getWidth() / ratingBar.getNumStars();
                    int rating = (int) (touchPositionX / widthOfStar) + 1;

                    ratingBar.setRating(rating);
                    product.setRating(rating);

                    Toast.makeText(context, rating, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
