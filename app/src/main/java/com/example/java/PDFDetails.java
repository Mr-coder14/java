package com.example.java;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class PDFDetails implements Parcelable {
    private int count = 1;
    private String color = "Black";
    private String deliverycharge="10.0";
    private String pages="0";
    private String perqtyamt="10";
    private String perpage="0.75";
    private String userid;
    private String finalmat="89";
    private String orderdate;
    private String Grandtotal;
    private String orderid,time;
    private boolean isSpiral;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSpiral(boolean spiral) {
        isSpiral = spiral;
    }

    public boolean isSpiral() {
        return isSpiral;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    private String uri;
    private String formats = "Front & Back";
    private String ratios = "1:1";
    private String sheet = "A4";

    public String getOrderid1() {
        return orderid;
    }

    public void setOrderid1(String orderid) {
        this.orderid = orderid;
    }

    public String getGrandtotal() {
        return Grandtotal;
    }

    public void setGrandtotal(String grandtotal) {
        Grandtotal = grandtotal;
    }

    public PDFDetails(){

    }

    public static final Creator<PDFDetails> CREATOR = new Creator<PDFDetails>() {
        @Override
        public PDFDetails createFromParcel(Parcel in) {
            return new PDFDetails(in);
        }

        @Override
        public PDFDetails[] newArray(int size) {
            return new PDFDetails[size];
        }
    };

    public String getUserid() {
        return userid;
    }



    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFinalmat() {
        return finalmat;
    }

    public void setFinalmat(String finalmat) {
        this.finalmat = finalmat;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getDeliverycharge() {
        return deliverycharge;
    }

    public void setDeliverycharge(String deliverycharge) {
        this.deliverycharge = deliverycharge;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getPerqtyamt() {
        return perqtyamt;
    }

    public void setPerqtyamt(String perqtyamt) {
        this.perqtyamt = perqtyamt;
    }

    public String getPerpage() {
        return perpage;
    }

    public void setPerpage(String perpage) {
        this.perpage = perpage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFormats() {
        return formats;
    }

    public void setFormats(String formats) {
        this.formats = formats;
    }

    public String getRatios() {
        return ratios;
    }

    public void setRatios(String ratios) {
        this.ratios = ratios;
    }

    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    protected PDFDetails(Parcel in) {
        finalmat = in.readString();
        count = in.readInt();
        color=in.readString();
        deliverycharge=in.readString();
        pages=in.readString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isSpiral=in.readBoolean();
        }
        perqtyamt=in.readString();
        perpage=in.readString();
        userid =in.readString();
        orderdate=in.readString();
        formats=in.readString();
        ratios=in.readString();
        time=in.readString();
        sheet=in.readString();

    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(count);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(isSpiral);
        }
        dest.writeString(color);
        dest.writeString(deliverycharge);
        dest.writeString(pages);
        dest.writeString(perqtyamt);
        dest.writeString(perpage);
        dest.writeString(userid);
        dest.writeString(time);
        dest.writeString(finalmat);
        dest.writeString(orderdate);
        dest.writeString(formats);
        dest.writeString(ratios);
        dest.writeString(sheet);
    }
}