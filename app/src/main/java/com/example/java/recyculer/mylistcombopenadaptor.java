package com.example.java.recyculer;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.Productpreviewa;
import com.example.java.R;

import java.util.ArrayList;

public class mylistcombopenadaptor extends RecyclerView.Adapter<mylistcombopenadaptor.ViewHolder>{
    private ArrayList<ProductDetails> productDetails;
    private Activity activity;
    public ArrayList<ProductDetails> selectedItems = new ArrayList<>();

    public mylistcombopenadaptor(ArrayList<ProductDetails> productDetails, Activity activity) {
        this.productDetails = productDetails;
        this.activity = activity;
    }
    @NonNull
    @Override
    public mylistcombopenadaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.combo_item, parent, false);
        return new mylistcombopenadaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mylistcombopenadaptor.ViewHolder holder, int position) {
        ProductDetails p = productDetails.get(position);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent=new Intent(activity, Productpreviewa.class);
                    intent.putExtra("product",p);
                    activity.startActivity(intent);

            }
        });



        holder.bind(p);

        if (selectedItems.contains(p)) {
            holder.itemView.setBackgroundResource(R.drawable.graybackround);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.whitebg_profile);
        }

    }



    @Override
    public int getItemCount() {
        return productDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, qty, amt;
        private ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productNamecombo);
            qty = itemView.findViewById(R.id.quantityValuecombo);
            amt = itemView.findViewById(R.id.productPricecombo);
            img = itemView.findViewById(R.id.productImagecombo);
        }
        public void bind(ProductDetails p) {
            name.setText(p.getProductname());
            amt.setText("₹ " + p.getProductamt());
            img.setImageResource(p.getProductimage());
            qty.setText(String.valueOf(p.getQty()));

        }
    }
}
