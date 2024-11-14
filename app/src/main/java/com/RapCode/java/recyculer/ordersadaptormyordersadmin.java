package com.RapCode.java.recyculer;

import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.RapCode.java.Fileinmodel;
import com.RapCode.java.R;
import com.RapCode.java.orderdetailsmyorders;

import java.util.ArrayList;
import java.util.List;

public class ordersadaptormyordersadmin extends RecyclerView.Adapter<RetrivepdfAdaptorhomemainadmin> {
    private List<Fileinmodel> orderList;
    private Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    public ordersadaptormyordersadmin(List<Fileinmodel> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    public void updateList(List<Fileinmodel> newList) {
        orderList = newList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RetrivepdfAdaptorhomemainadmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordertemplateadmin, parent, false);
        return new RetrivepdfAdaptorhomemainadmin(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RetrivepdfAdaptorhomemainadmin holder, int position) {
        Fileinmodel fld = orderList.get(position);
        holder.orderid.setText(fld.getOrderid0());
        holder.Grandtotal.setText("₹ " + fld.getGrandTotal0());
        holder.UserName1.setText(fld.getUsername());
        holder.checkBox.setChecked(selectedItems.get(position, false));
        holder.checkBox.setOnClickListener(v -> {
            if (selectedItems.get(position, false)) {
                selectedItems.delete(position);
            } else {
                selectedItems.put(position, true);
            }
            notifyDataSetChanged();
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, orderdetailsmyorders.class);
            intent.putExtra("orderid", fld.getOrderid0());
            intent.putExtra("gt", fld.getGrandTotal0());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
    public List<Fileinmodel> getSelectedItems() {
        List<Fileinmodel> selectedFiles = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            int key = selectedItems.keyAt(i);
            selectedFiles.add(orderList.get(key));
        }
        return selectedFiles;
    }

    // Method to clear selected items
    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }
}
