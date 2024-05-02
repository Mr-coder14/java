package com.example.java.recyculer;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.R;

public class RetrivepdfAdaptor extends RecyclerView.ViewHolder implements View.OnClickListener {
    public itemClickListener itemClickListener;
    public final Context context;
    public TextView pdffilename;
    public RetrivepdfAdaptor(@NonNull View itemView) {
        super(itemView);
        pdffilename=itemView.findViewById(R.id.filenamerecyculer);
        context=itemView.getContext();
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}