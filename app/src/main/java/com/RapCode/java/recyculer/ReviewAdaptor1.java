package com.RapCode.java.recyculer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.RapCode.java.R;
import com.RapCode.java.Review;

import java.util.List;


public class ReviewAdaptor1 extends RecyclerView.Adapter<ReviewAdaptor1.ReviewViewHolder> {

    private List<Review> reviews;

    public ReviewAdaptor1(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_template, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.usernameTextView.setText(review.getUsername());
        holder.reviewTextView.setText(review.getFeedback());
        holder.ratingTextView.setRating(review.getRating());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView, reviewTextView;
        RatingBar ratingTextView;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            reviewTextView = itemView.findViewById(R.id.reviewTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
        }
    }
}
