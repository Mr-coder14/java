package com.RapCode.java.recyculer;

import java.io.Serializable;

public class ProductDetails implements Serializable {
    String productname,productamt;
    int productimage;
    int qty;
    private float rating;
    private String key;
    private String discription;

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public ProductDetails() {

    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public ProductDetails(String productname, String productamt, int productimage) {
        this.productname = productname;
        this.productamt = productamt;
        this.discription="";
        this.productimage = productimage;
    }
    public ProductDetails(String productname, String productamt, int productimage,int qty,String discription) {
        this.productname = productname;
        this.productamt = productamt;
        this.discription=discription;
        this.productimage = productimage;
        this.qty=qty;
    }
    public ProductDetails(String productname, String productamt, int productimage,int qty) {
        this.productname = productname;
        this.productamt = productamt;
        this.discription="";
        this.productimage = productimage;
        this.qty=qty;
    }

    public String getProductname() {
        return productname;
    }
    public int getQty() {
        return qty;
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
    public void setQty(int qty) {
        this.qty = qty;
    }


    public void setProductimage(int productimage) {
        this.productimage = productimage;
    }
}
