package com.example.java.recyculer;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.Fileinmodel;
import com.example.java.R;
import com.github.barteksc.pdfviewer.PDFView;
import java.util.ArrayList;

public class orederpreviewadaptor extends RecyclerView.Adapter<orederpreviewadaptor.ViewHolder> {
    private ArrayList<Fileinmodel> fileinmodels;
    private Activity activity;

    public orederpreviewadaptor(ArrayList<Fileinmodel> fileinmodels, Activity activity) {
        this.fileinmodels = fileinmodels;
        this.activity=activity;
    }
    private String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderpreviewtemplateuser, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Fileinmodel fileinmodel = fileinmodels.get(position);
        holder.fileNameTextViewauser.setText(fileinmodel.getName0());
        holder.colortxtuser.setText(fileinmodel.getColor0());
        holder.sheetuser.setText(fileinmodel.getSheet0());
        holder.preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileUrl = fileinmodel.getUri0();
                String fileName = fileinmodel.getName0();
                String fileExtension = getFileExtension(fileName);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                switch (fileExtension.toLowerCase()) {
                    case "pdf":
                        intent.setDataAndType(Uri.parse(fileUrl), "application/pdf");
                        break;
                    case "jpg":
                    case "jpeg":
                    case "png":
                        intent.setDataAndType(Uri.parse(fileUrl), "image/*");
                        break;
                    case "doc":
                    case "docx":
                        intent.setDataAndType(Uri.parse(fileUrl), "application/msword");
                        break;
                    case "xls":
                    case "xlsx":
                        intent.setDataAndType(Uri.parse(fileUrl), "application/vnd.ms-excel");
                        break;
                    // Add more file types as needed
                    default:
                        intent.setDataAndType(Uri.parse(fileUrl), "*/*");
                        break;
                }

                try {
                    activity.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(activity, "No application found to open this file", Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.formatuser.setText(fileinmodel.getFormat0());
        holder.perpageamtuser.setText(fileinmodel.getPerpage0());
        holder.perqtyamtuser.setText("₹ "+fileinmodel.getPerqtyamt0());
        holder.ratiouser.setText(fileinmodel.getRatio0());
        holder.finalamtuser.setText("₹ "+fileinmodel.getFinalamt0());
        holder.deliveryamtuser.setText("₹ "+fileinmodel.getDeliveyamt0());
        holder.qtynouser.setText(fileinmodel.getQty0());
        holder.pguser.setText(fileinmodel.getPages0());

    }


    @Override
    public int getItemCount() {
        return fileinmodels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameTextViewauser, colortxtuser, sheetuser, formatuser, perpageamtuser, perqtyamtuser, ratiouser, finalamtuser, deliveryamtuser, qtynouser, pguser;
        Button preview;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fileNameTextViewauser = itemView.findViewById(R.id.filenametxt1user);
            colortxtuser = itemView.findViewById(R.id.colorfontuser);
            sheetuser = itemView.findViewById(R.id.sheetuser);
            formatuser = itemView.findViewById(R.id.formatuser);
            perpageamtuser = itemView.findViewById(R.id.perpageuser);
            perqtyamtuser = itemView.findViewById(R.id.amtperqtyuer);
            ratiouser = itemView.findViewById(R.id.ratiouser);
            finalamtuser = itemView.findViewById(R.id.amt1user);
            deliveryamtuser = itemView.findViewById(R.id.deliveryamtuser);
            qtynouser = itemView.findViewById(R.id.qtynouser);
            preview=itemView.findViewById(R.id.previewuser);
            pguser = itemView.findViewById(R.id.pagenouser);
        }

    }
}
