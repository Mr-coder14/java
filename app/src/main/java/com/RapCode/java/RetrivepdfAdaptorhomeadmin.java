package com.RapCode.java;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RetrivepdfAdaptorhomeadmin extends RecyclerView.ViewHolder implements View.OnClickListener {
    public com.RapCode.java.recyculer.itemClickListener itemClickListener;
    public final Context context;
    public TextView Grandtotal,UserName1,orderid;




    public RetrivepdfAdaptorhomeadmin(@NonNull View itemView) {
        super(itemView);
        Grandtotal=itemView.findViewById(R.id.Grandtotaladmin);
        UserName1=itemView.findViewById(R.id.orderedusername);
        orderid=itemView.findViewById(R.id.orderidadmin);
        context=itemView.getContext();

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}