package com.example.java.recyculer;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.R;
import com.example.java.User;
import com.google.firebase.database.FirebaseDatabase;

public class searchadminadaptor  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public com.example.java.recyculer.itemClickListener itemClickListener;
    public TextView adminname,adminemail;



    public searchadminadaptor(@NonNull View itemView) {
        super(itemView);
        adminname = itemView.findViewById(R.id.admin_profile_name);
        adminemail = itemView.findViewById(R.id.admin_email);

    }

    public void bind(User user){
        adminname.setText(user.getName());
        adminemail.setText(user.getEmail());
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
