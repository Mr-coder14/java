package com.RapCode.java;

import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AppData {
    private static AppData instance;
    private ArrayList<Uri> uris;
    private ArrayList<String> fileNames;
    private String orderid;
    private ArrayList<PDFDetails> pdfDetails;
    private RecyclerView.ViewHolder holder;

    public RecyclerView.ViewHolder getHolder() {
        return holder;
    }

    public void setHolder(RecyclerView.ViewHolder holder) {
        this.holder = holder;
    }

    private AppData() {
        // Private constructor to prevent instantiation
    }

    public static synchronized AppData getInstance() {
        if (instance == null) {
            instance = new AppData();
        }
        return instance;
    }

    // Getters and setters for your data
    public ArrayList<Uri> getUris() {
        return uris;
    }

    public void setUris(ArrayList<Uri> uris) {
        this.uris = uris;
    }

    public ArrayList<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(ArrayList<String> fileNames) {
        this.fileNames = fileNames;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public ArrayList<PDFDetails> getPdfDetails() {
        return pdfDetails;
    }

    public void setPdfDetails(ArrayList<PDFDetails> pdfDetails) {
        this.pdfDetails = pdfDetails;
    }
}
