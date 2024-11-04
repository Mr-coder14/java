package com.RapCode.java;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RetrivepdfAdaptoradmin extends RecyclerView.ViewHolder implements View.OnClickListener {
    public com.RapCode.java.recyculer.itemClickListener itemClickListener;
    public final Context context;
    public TextView pdffilename,UserName,email,amt;
    public RetrivepdfAdaptoradmin(@NonNull View itemView) {
        super(itemView);
        pdffilename=itemView.findViewById(R.id.filenamerecyculeradmin);
        UserName=itemView.findViewById(R.id.namerecyculeradmin);
        email=itemView.findViewById(R.id.emailerecucyleradmin);
        amt=itemView.findViewById(R.id.amounterecucyleradmin);
        context=itemView.getContext();
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}