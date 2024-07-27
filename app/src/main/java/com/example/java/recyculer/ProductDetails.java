package com.example.java.recyculer;

public class ProductDetails {
    String productname,productamt;
    int productimage;

    public ProductDetails(String productname, String productamt, int productimage) {
        this.productname = productname;
        this.productamt = productamt;
        this.productimage = productimage;
    }

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
}
