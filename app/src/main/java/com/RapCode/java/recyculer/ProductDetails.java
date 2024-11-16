package com.RapCode.java.recyculer;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductDetails implements Parcelable {
    private String productname, productamt, discription, key;
    private int productimage, qty;
    private float rating;

    // Default constructor
    public ProductDetails() {
    }

    // Constructor for Parcelable
    protected ProductDetails(Parcel in) {
        productname = in.readString();
        productamt = in.readString();
        discription = in.readString();
        key = in.readString();
        productimage = in.readInt();
        qty = in.readInt();
        rating = in.readFloat();
    }

    public static final Creator<ProductDetails> CREATOR = new Creator<ProductDetails>() {
        @Override
        public ProductDetails createFromParcel(Parcel in) {
            return new ProductDetails(in);
        }

        @Override
        public ProductDetails[] newArray(int size) {
            return new ProductDetails[size];
        }
    };

    // Constructor with essential fields
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

    // Parcelable Methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productname);
        dest.writeString(productamt);
        dest.writeString(discription);
        dest.writeString(key);
        dest.writeInt(productimage);
        dest.writeInt(qty);
        dest.writeFloat(rating);
    }
}
