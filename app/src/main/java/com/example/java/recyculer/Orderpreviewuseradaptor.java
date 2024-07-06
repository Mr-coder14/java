package com.example.java.recyculer;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.OrderdDetailsuser;
import com.example.java.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Orderpreviewuseradaptor extends RecyclerView.ViewHolder {
    public TextView fileNameTextViewauser, pguser, finalamtuser, qtynouser, qtytxt1user, perpageamtuser, deliveryamtuser, colortxtuser, formatuser, ratiouser, sheetuser,perqtyamtuser;
    private Button preview;
    private FirebaseAuth mAuth;
    private String userid,orderid;
    private PDFView pdfView;

    private DatabaseReference databaseReference;

    public Orderpreviewuseradaptor(@NonNull View itemView) {
        super(itemView);
        fileNameTextViewauser = itemView.findViewById(R.id.filenametxt1user);

        qtytxt1user = itemView.findViewById(R.id.qtytxt1user);
        mAuth= FirebaseAuth.getInstance();
        qtynouser = itemView.findViewById(R.id.qtynouser);
        perpageamtuser = itemView.findViewById(R.id.perpageuser);
        deliveryamtuser = itemView.findViewById(R.id.deliveryamtuser);
        colortxtuser = itemView.findViewById(R.id.colorfontuser);
        pguser = itemView.findViewById(R.id.pagenouser);
        pdfView = itemView.findViewById(R.id.pdfViewuser);
        formatuser = itemView.findViewById(R.id.formatuser);
        preview=itemView.findViewById(R.id.previewuser);
        perqtyamtuser=itemView.findViewById(R.id.amtperqtyuer);
        finalamtuser=itemView.findViewById(R.id.amt1user);
        userid=mAuth.getCurrentUser().getUid();


        ratiouser = itemView.findViewById(R.id.ratiouser);
        sheetuser = itemView.findViewById(R.id.sheetuser);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("pdfs").child(userid);



    }


}
