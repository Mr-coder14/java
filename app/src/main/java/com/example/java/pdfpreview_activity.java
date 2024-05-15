package com.example.java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;

public class pdfpreview_activity extends AppCompatActivity {
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfpreview);
        pdfView=findViewById(R.id.pdffinalview);

        pdfView.fromUri(getIntent().getData())
                .defaultPage(0)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {

                    }
                })
                .enableAntialiasing(true)
                .spacing(0)
                .autoSpacing(false)
                .pageFitPolicy(FitPolicy.WIDTH)
                .fitEachPage(true)
                .pageSnap(true)
                .pageFling(false)
                .nightMode(false)
                .load();



    }
}