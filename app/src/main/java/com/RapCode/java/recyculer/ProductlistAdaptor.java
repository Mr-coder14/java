package com.RapCode.java.recyculer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.RapCode.java.Productpreviewa;
import com.RapCode.java.R;
import com.RapCode.java.cart;

import java.util.ArrayList;

public class ProductlistAdaptor extends RecyclerView.Adapter<ProductlistAdaptor.viewHolder> {

    private ArrayList<ProductDetails> productDetails;
    private Context context;

    public ProductlistAdaptor(ArrayList<ProductDetails> productDetails, Context context) {
        this.productDetails = productDetails;
        this.context = context;
    }
    public void filterList(ArrayList<ProductDetails> filteredList) {
        productDetails = filteredList;
        notifyDataSetChanged();
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
        holder.addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cart.getInstance().addItem(pd, new cart.CartAddCallback() {
                        @Override
                        public void onItemAlreadyExists() {
                            Toast.makeText(context, "Item is already in the cart", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onItemAdded() {
                            Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onItemAddFailed() {
                            Toast.makeText(context, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IllegalStateException e) {
                    Toast.makeText(context, "Please log in to add items to cart", Toast.LENGTH_SHORT).show();
                }
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
        private Button addtocart;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            productamt=itemView.findViewById(R.id.productamt);
            addtocart=itemView.findViewById(R.id.addToCartButtontemplate);
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
