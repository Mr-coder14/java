package com.example.java;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private Context context;

    public OrderAdapter(List<Order> orders,Context context) {
        this.orders = orders;
        this.context=context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, orderspreview.class);
                intent.putExtra("order", order);
                context.startActivity(intent);
            }
        });
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdText, orderTotalText, orderDateText;
        RecyclerView productRecyclerView;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdText = itemView.findViewById(R.id.orderIdText);
            orderTotalText = itemView.findViewById(R.id.orderTotalText);
            orderDateText = itemView.findViewById(R.id.orderDateText);
            productRecyclerView = itemView.findViewById(R.id.productRecyclerView);
        }

        void bind(Order order) {
            orderIdText.setText("Order ID: " + order.getOrderId());
            orderTotalText.setText("Total: " + order.getOrderTotal());
            orderDateText.setText("Date: " + new Date(order.getOrderTimestamp()));

            ProductAdapter productAdapter = new ProductAdapter(order.getProducts());
            productRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            productRecyclerView.setAdapter(productAdapter);
        }
    }
}
