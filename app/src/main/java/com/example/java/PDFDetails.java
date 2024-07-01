package com.example.java;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class PDFDetails implements Parcelable {
    private int count = 1;
    private String color = "Black",deliverycharge="10.0",pages="0",perqtyamt="10",perpage="0.75",uerid,finalmat="89",orderdate;
    private String formats = "Front & Back";
    private String ratios = "1:1";
    private String sheet = "A4";
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

    public String getUerid() {
        return uerid;
    }



    public void setUerid(String uerid) {
        this.uerid = uerid;
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
        perqtyamt=in.readString();
        perpage=in.readString();
        uerid=in.readString();
        orderdate=in.readString();
        formats=in.readString();
        ratios=in.readString();
        sheet=in.readString();

    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeString(color);
        dest.writeString(deliverycharge);
        dest.writeString(pages);
        dest.writeString(perqtyamt);
        dest.writeString(perpage);
        dest.writeString(uerid);
        dest.writeString(finalmat);
        dest.writeString(orderdate);
        dest.writeString(formats);
        dest.writeString(ratios);
        dest.writeString(sheet);
    }
}