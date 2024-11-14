package com.RapCode.java.recyculer;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.RapCode.java.R;

public class RetrivepdfAdaptorhomemainadmin extends RecyclerView.ViewHolder implements View.OnClickListener {
    public com.RapCode.java.recyculer.itemClickListener itemClickListener;
    public final Context context;
    public TextView Grandtotal,UserName1,orderid;
    public CheckBox checkBox;

    public RetrivepdfAdaptorhomemainadmin(@NonNull View itemView) {
        super(itemView);
        Grandtotal=itemView.findViewById(R.id.Grandtotaladmin);
        UserName1=itemView.findViewById(R.id.orderedusername);
        orderid=itemView.findViewById(R.id.orderidadmin);
        context=itemView.getContext();
        checkBox = itemView.findViewById(R.id.order_checkbox);
    }


    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}
