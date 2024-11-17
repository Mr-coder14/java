package com.RapCode.java.recyculer;

import java.io.Serializable;

public class ProductDetails implements Serializable {
    private String productname, productamt, discription, key, offeramt;
    private int productimage, qty;
    private float rating;


    public ProductDetails() {
    }

    // Constructor with essential fields
    public ProductDetails(String productname, String productamt, int productimage) {
        this.productname = productname;
        this.productamt = productamt;
        this.productimage = productimage;
        this.discription = "";
    }

    public ProductDetails(String productname, String productamt, int productimage, int qty, String discription) {
        this.productname = productname;
        this.productamt = productamt;
        this.productimage = productimage;
        this.qty = qty;
        this.discription = discription;
    }

    public ProductDetails(String productname, String productamt, String offeramt, int productimage, int qty, String discription) {
        this.productname = productname;
        this.productamt = productamt;
        this.offeramt = offeramt;
        this.productimage = productimage;
        this.qty = qty;
        this.discription = discription;
    }

    public ProductDetails(String productname, String productamt, int productimage, int qty) {
        this.productname = productname;
        this.productamt = productamt;
        this.productimage = productimage;
        this.qty = qty;
        this.discription = "";
    }

    // Getters and Setters
    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductamt() {
        return productamt;
    }

    public void setProductamt(String productamt) {
        this.productamt = productamt;
    }

    public int getProductimage() {
        return productimage;
    }

    public void setProductimage(int productimage) {
        this.productimage = productimage;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getOfferamt() {
        return offeramt;
    }

    public void setOfferamt(String offeramt) {
        this.offeramt = offeramt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
}
