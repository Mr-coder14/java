package com.example.java.recyculer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.java.R;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private final List<BannerItem> banners;

    public BannerAdapter(List<BannerItem> banners) {
        this.banners = banners;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        holder.bind(banners.get(position));
    }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout rootLayout;
        private final TextView titleTextView;
        private final TextView discountTextView;
        private final Button getButton;
        private final ImageView bannerImageView;

        public BannerViewHolder(View itemView) {
            super(itemView);
            rootLayout = itemView.findViewById(R.id.rootLayout);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            bannerImageView = itemView.findViewById(R.id.bannerImageView);
            discountTextView = itemView.findViewById(R.id.discountTextView);
            getButton = itemView.findViewById(R.id.getButton);
        }

        public void bind(BannerItem banner) {
            titleTextView.setText(banner.getTitle());
            discountTextView.setText(banner.getDiscountText());
            getButton.setText(banner.getButtonText());
            rootLayout.setBackgroundColor(banner.getBackgroundColor());
            Glide.with(bannerImageView.getContext())
                    .load(R.drawable.vcc)
                    .into(bannerImageView);
        }
    }
}

