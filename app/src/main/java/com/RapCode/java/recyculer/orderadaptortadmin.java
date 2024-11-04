package com.RapCode.java.recyculer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.RapCode.java.Fileinmodel;
import com.RapCode.java.OrderDetialsadmin;
import com.RapCode.java.R;
import com.RapCode.java.RetrivepdfAdaptorhomeadmin;

import java.util.List;

public class orderadaptortadmin extends RecyclerView.Adapter<RetrivepdfAdaptorhomeadmin> {
    private List<Fileinmodel> orderList;
    private Context context;
    public orderadaptortadmin(){

    }

    public orderadaptortadmin(List<Fileinmodel> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }
    public void updateList(List<Fileinmodel> newList) {
        orderList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RetrivepdfAdaptorhomeadmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_template, parent, false);
        return new RetrivepdfAdaptorhomeadmin(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RetrivepdfAdaptorhomeadmin holder, int position) {
        Fileinmodel fld = orderList.get(position);
        holder.orderid.setText(fld.getOrderid0());
        holder.Grandtotal.setText("₹ " + fld.getGrandTotal0());
        holder.UserName1.setText(fld.getUsername());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetialsadmin.class);
            intent.putExtra("orderid", fld.getOrderid0());
            intent.putExtra("gt", fld.getGrandTotal0());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
