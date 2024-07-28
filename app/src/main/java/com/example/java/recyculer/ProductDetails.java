package com.example.java.recyculer;

import java.io.Serializable;

public class ProductDetails implements Serializable {
    String productname,productamt;
    int productimage;
    int qty;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public ProductDetails() {

    }

    public ProductDetails(String productname, String productamt, int productimage) {
        this.productname = productname;
        this.productamt = productamt;
        this.productimage = productimage;
    }
    public ProductDetails(String productname, String productamt, int productimage,int qty) {
        this.productname = productname;
        this.productamt = productamt;
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
