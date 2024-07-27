package com.example.java.recyculer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.R;

import java.util.ArrayList;

public class Itemlistadaptormycart extends RecyclerView.Adapter<Itemlistadaptormycart.ViewHolder> {
    private ArrayList<ProductDetails> productDetails;

    public Itemlistadaptormycart(ArrayList<ProductDetails> productDetails) {
        this.productDetails = productDetails;
    }

    @NonNull
    @Override
    public Itemlistadaptormycart.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlistmycart,parent,false);
        return new Itemlistadaptormycart.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Itemlistadaptormycart.ViewHolder holder, int position) {
        ProductDetails p=productDetails.get(position);
        holder.bind(p);

    }

    @Override
    public int getItemCount() {
        return productDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,qty,qty1,amt;
        private ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.productNamemycart);
            qty=itemView.findViewById(R.id.quantityValuemycart);
            qty1=itemView.findViewById(R.id.quantityValuemycart1);
            amt=itemView.findViewById(R.id.productPricemycart);
            img=itemView.findViewById(R.id.productImagemycart);

        }

        public void bind(ProductDetails p) {
            name.setText(p.getProductname());
            qty.setText(String.valueOf(p.getQty()));
            qty1.setText(String.valueOf(p.getQty()));
            amt.setText(p.getProductamt());
            img.setImageResource(p.getProductimage());
        }
    }
}
