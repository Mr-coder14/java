package com.example.java.recyculer;

public class ProductDetails {
    String productname,productamt;
    int productimage;
    int qty;




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
