package com.example.java.recyculer;

import android.content.Context;
import android.net.Uri;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.List;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ViewHolder> {

    private Uri[] uris;
    private String[] fileNames;
    private Context context;

    public PreviewAdapter(Uri[] uris, String[] fileNames) {
        this.uris =  uris ;
        this.fileNames = fileNames ;
    }


    @NonNull
    @Override
    public PreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.orderpreviewactivitytemplate, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PreviewAdapter.ViewHolder holder, int position) {
        holder.bind(uris[position], fileNames[position]);


    }

    @Override
    public int getItemCount() {
        return uris.length;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fileNameTextView;
        private PDFView pdfView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.filenametxt1admin);
            pdfView = itemView.findViewById(R.id.pdfViewadmin);
        }

        public void bind(Uri uri, String fileName) {
            fileNameTextView.setText(fileName);
             pdfView.fromUri(uri).defaultPage(0).load();
        }

    }
}
