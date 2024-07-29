package com.example.java;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.recyculer.ProductDetails;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductDetails> products;

    public ProductAdapter(List<ProductDetails> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductDetails product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productNameText, productQuantityText, productPriceText;
        ImageView iamge;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameText = itemView.findViewById(R.id.productNameTextorder);
            productQuantityText = itemView.findViewById(R.id.productQuantityTextorder);
            productPriceText = itemView.findViewById(R.id.productPriceTextorder);
            iamge=itemView.findViewById(R.id.productImageorder);
        }

        void bind(ProductDetails product) {
            productNameText.setText(product.getProductname());
            productQuantityText.setText(String.valueOf(product.getQty()));
            productPriceText.setText("Price: ₹" + product.getProductamt());
            iamge.setImageResource(product.getProductimage());
        }
    }
}

