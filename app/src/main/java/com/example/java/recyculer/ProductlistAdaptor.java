package com.example.java.recyculer;

import android.content.Context;
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

public class ProductlistAdaptor extends RecyclerView.Adapter<ProductlistAdaptor.viewHolder> {

    private ArrayList<ProductDetails> productDetails;
    private Context context;

    public ProductlistAdaptor(ArrayList<ProductDetails> productDetails, Context context) {
        this.productDetails = productDetails;
        this.context = context;
    }


    @NonNull
    @Override
    public ProductlistAdaptor.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_template,parent,false);
        return new ProductlistAdaptor.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductlistAdaptor.viewHolder holder, int position) {
        ProductDetails pd=productDetails.get(position);
        pd.setQty(1);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Productpreviewa.class);
                intent.putExtra("product", pd);
                context.startActivity(intent);
            }
        });
        holder.bind(pd);
    }

    @Override
    public int getItemCount() {
        return productDetails.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView productamt,productname;
        private ImageView productimage;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            productamt=itemView.findViewById(R.id.productamt);
            productname=itemView.findViewById(R.id.productname);
            productimage=itemView.findViewById(R.id.productImage);
        }
        void bind(ProductDetails pd){
            productamt.setText("₹"+pd.getProductamt());
            productname.setText(pd.getProductname());
            productimage.setImageResource(pd.getProductimage());
        }
    }
}
