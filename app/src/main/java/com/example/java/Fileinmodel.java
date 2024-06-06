package com.example.java;

import java.security.SecureRandom;

public class Fileinmodel {
    String uri;
    String userID,orderid;
    String name;
    String amt,ratio,format,sheet,qty,color,pages;

    public Fileinmodel(){

    }
    public Fileinmodel(String name,String uri,String userID,String amt,String ratio,String format,String sheet,String color,String qty,String pages,String orderid){
        this.name=name;
        this.uri=uri;
        this.userID=userID;
        this.amt=amt;
        this.orderid=orderid;
        this.ratio=ratio;
        this.color=color;
        this.format=format;
        this.sheet=sheet;
        this.qty=qty;
        this.pages=pages;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getuserID(){
        return userID;
    }
    public String getAmt(){
        return amt;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public void setAmt(String amt){
        this.amt=amt;
    }
    public String getName(){
        return this.name;
    }

    public String getUri()
    {
        return this.uri;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }
}
