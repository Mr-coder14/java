package com.example.java;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.java.recyculer.ProductDetails;

import java.util.ArrayList;
import java.util.List;

public class Order implements Parcelable {
    private String orderId;
    private String orderTotal,odered,delivered;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    private Long orderTimestamp;
    private String username,phno,notes;

    public String getOdered() {
        return odered;
    }

    public void setOdered(String odered) {
        this.odered = odered;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    private List<ProductDetails> products;

    public Order(String orderId, String orderTotal, Long orderTimestamp, List<ProductDetails> products,String uername,String phno,String notes) {
        this.orderId = orderId;
        this.orderTotal = orderTotal;
        this.username=uername;
        this.notes=notes;
        this.phno=phno;
        this.orderTimestamp = orderTimestamp;
        this.products = products;
    }

    public String getOrderId() { return orderId; }
    public String getOrderTotal() { return orderTotal; }
    public Long getOrderTimestamp() { return orderTimestamp; }
    public List<ProductDetails> getProducts() { return products; }
    protected Order(Parcel in) {
        orderId = in.readString();
        orderTotal = in.readString();
        orderTimestamp = in.readLong();
        products = new ArrayList<>();
        in.readList(products, ProductDetails.class.getClassLoader());
        username = in.readString();
        phno = in.readString();
        notes = in.readString();
    }


    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeString(orderTotal);
        dest.writeLong(orderTimestamp);
        dest.writeList(products);
        dest.writeString(username);
        dest.writeString(phno);
        dest.writeString(notes);
    }
}
