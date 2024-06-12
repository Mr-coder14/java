package com.example.java;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RetrivepdfAdaptorhomeadmin extends RecyclerView.ViewHolder implements View.OnClickListener {
    public com.example.java.recyculer.itemClickListener itemClickListener;
    public final Context context;
    public TextView pdffilename1,UserName1,orderid;


    public RetrivepdfAdaptorhomeadmin(@NonNull View itemView) {
        super(itemView);
        pdffilename1=itemView.findViewById(R.id.filenameadmin);
        UserName1=itemView.findViewById(R.id.orderedusername);
        orderid=itemView.findViewById(R.id.orderidadmin);
        context=itemView.getContext();
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}