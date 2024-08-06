package com.example.java.recyculer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.Order;
import com.example.java.ProductAdapter;
import com.example.java.R;
import com.example.java.oderspreviewadmin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderAdaptormadmin extends RecyclerView.Adapter<OrderAdaptormadmin.ViewHolder> {
    private List<Order> orders;
    private Context context;
    private FirebaseAuth mauth;
    private FirebaseUser user;
    private ArrayList<String> arrayList=new ArrayList<>();

    public OrderAdaptormadmin(List<Order> orders,Context context){
        this.orders = orders;
        this.context=context;
        mauth=FirebaseAuth.getInstance();
        user=mauth.getCurrentUser();
        arrayList.add("abcd1234@gmail.com");
    }
    @NonNull
    @Override
    public OrderAdaptormadmin.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderAdaptormadmin.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdaptormadmin.ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, oderspreviewadmin.class);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdText, orderTotalText, orderDateText;
        RecyclerView productRecyclerView;
        public ViewHolder(@NonNull View itemView) {
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

            ProductAdapter productAdapter = new ProductAdapter(order.getProducts(),context);
            productRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            productRecyclerView.setAdapter(productAdapter);
        }
    }
}
