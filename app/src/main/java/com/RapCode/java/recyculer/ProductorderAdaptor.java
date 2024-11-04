package com.RapCode.java.recyculer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.RapCode.java.ProjectProduct;
import com.RapCode.java.R;
import com.RapCode.java.projectorderactivityuser;

import java.util.List;

public class ProductorderAdaptor extends RecyclerView.Adapter<ProductorderAdaptor.ProductOrderViewHolder> {
    private List<ProjectProduct> bookList;
    private Context context;
    public ProductorderAdaptor(List<ProjectProduct> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public ProductorderAdaptor.ProductOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productorder, parent, false);
        return new ProductorderAdaptor.ProductOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductorderAdaptor.ProductOrderViewHolder holder, int position) {
        ProjectProduct Product = bookList.get(position);
        holder.textViewProductName.setText(Product.getName());
        holder.textViewmodelno.setText(Product.getModel());
        holder.textViewOrderDate.setText(Product.getOrderDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, projectorderactivityuser.class);
                intent.putExtra("productName", Product.getName());
                intent.putExtra("modelno", Product.getModel());
                intent.putExtra("price", Product.getPrice());
                intent.putExtra("description", Product.getDescription());
                intent.putExtra("orderdate",Product.getOrderDate());
                intent.putExtra("ordertime",Product.getOrderTime());
                intent.putExtra("name",Product.getUsername());
                intent.putExtra("phno",Product.getPhno());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ProductOrderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProductName, textViewmodelno, textViewOrderDate;
        public ProductOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.productnameorder);
            textViewmodelno = itemView.findViewById(R.id.modelno);
            textViewOrderDate = itemView.findViewById(R.id.textViewOrderDateproduct);
        }
    }
}
