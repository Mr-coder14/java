package com.example.java;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

public class Orderedactivityadmin extends AppCompatActivity {
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderedactivityadmin);

        pdfView=findViewById(R.id.pdfview3);
        Uri pdfUri = getIntent().getData();
        if (pdfUri != null) {
            displayPdfFromUri(pdfUri);
        } else {

        }


    }
    private void displayPdfFromUri(Uri uri) {
        pdfView.fromUri(uri)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .pageFitPolicy(FitPolicy.WIDTH)
                .fitEachPage(true)
                .enableAnnotationRendering(true)
                .load();
    }
}