package com.RapCode.java.recyculer;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.RapCode.java.R;
import com.RapCode.java.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class searchadminadaptor  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public com.RapCode.java.recyculer.itemClickListener itemClickListener;
    public TextView adminname,adminemail;
    public CircleImageView pic;



    public searchadminadaptor(@NonNull View itemView) {
        super(itemView);
        adminname = itemView.findViewById(R.id.admin_profile_name);
        adminemail = itemView.findViewById(R.id.admin_email);
        pic=itemView.findViewById(R.id.profilepicadmin);

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
