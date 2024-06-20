package com.example.java;

public class Fileinmodel {
    String uri;
    String userID,orderid;
    String name;
    String finalamt,ratio,format,sheet,qty,color,pages,perqtyamt,orderDate,perpage,deliveyamt;
    long timestamp;

    public String getPerpage() {
        return perpage;
    }

    public void setPerpage(String perpage) {
        this.perpage = perpage;
    }

    public Fileinmodel(){

    }

    public String getDeliveyamt() {
        return deliveyamt;
    }

    public void setDeliveyamt(String deliveyamt) {
        this.deliveyamt = deliveyamt;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Fileinmodel(String name, String uri, String userID, String amt, String ratio, String format, String sheet, String color, String qty, String pages, String orderid, String perqtyamt,String orderDate,String perpage,String deliveyamt){
        this.name=name;
        this.uri=uri;
        this.deliveyamt=deliveyamt;
        this.userID=userID;
        this.orderDate=orderDate;
        this.finalamt =amt;
        this.perpage=perpage;
        this.orderid=orderid;
        this.ratio=ratio;
        this.color=color;
        this.perqtyamt=perqtyamt;
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

    public String getPerqtyamt() {
        return perqtyamt;
    }

    public void setPerqtyamt(String perqtyamt) {
        this.perqtyamt = perqtyamt;
    }

    public String getFinalamt(){
        return finalamt;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public void setFinalamt(String finalamt){
        this.finalamt = finalamt;
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
