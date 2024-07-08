package com.example.java;

public class Fileinmodel {
    String uri0;
    String userid0, orderid0;
    String name0;
    String finalamt,ratio,format,sheet,qty,color,pages,perqtyamt,orderDate,perpage,deliveyamt, grandTotal0;

    public  Fileinmodel(String name0, String uri0, String GrandTotal0, String orderid0){
        this.name0 = name0;
        this.uri0 = uri0;
        this.grandTotal0 = GrandTotal0;
        this.orderid0=orderid0;
    }


    public String getPerpage0() {
        return perpage;
    }

    public void setPerpage0(String perpage) {
        this.perpage = perpage;
    }
    public void setName0(String name) {
        this.name0 = name;
    }
    public void setUri0(String uri) {
        this.uri0 = uri;
    }

    public String getGrandTotal0() {
        return grandTotal0;
    }

    public void setGrandTotal0(String grandTotal0) {
        grandTotal0 = grandTotal0;
    }

    public Fileinmodel(){

    }

    public String getDeliveyamt0() {
        return deliveyamt;
    }

    public void setDeliveyamt0(String deliveyamt) {
        this.deliveyamt = deliveyamt;
    }




    public String getOrderDate0() {
        return orderDate;
    }

    public void setOrderDate0(String orderDate) {
        this.orderDate = orderDate;
    }

    public Fileinmodel(String name0, String uri0, String userid0, String amt, String ratio, String format, String sheet, String color, String qty, String pages, String orderid0, String perqtyamt, String perpage, String deliveyamt){
        this.name0 = name0;
        this.uri0 = uri0;
        this.deliveyamt=deliveyamt;
        this.userid0 = userid0;
        this.finalamt =amt;
        this.perpage=perpage;
        this.orderid0 = orderid0;
        this.ratio=ratio;
        this.color=color;
        this.perqtyamt=perqtyamt;
        this.format=format;
        this.sheet=sheet;
        this.qty=qty;
        this.pages=pages;
    }

    public void setuserid0(String userid) {
        this.userid0 = userid;
    }
    public String getuserid0(){
        return userid0;
    }

    public String getPerqtyamt0() {
        return perqtyamt;
    }

    public void setPerqtyamt0(String perqtyamt) {
        this.perqtyamt = perqtyamt;
    }

    public String getFinalamt0(){
        return finalamt;
    }

    public String getOrderid0() {
        return orderid0;
    }

    public void setOrderid0(String orderid) {
        this.orderid0 = orderid;
    }

    public void setFinalamt0(String finalamt){
        this.finalamt = finalamt;
    }
    public String getName0(){
        return this.name0;
    }

    public String getUri0()
    {
        return this.uri0;
    }

    public String getRatio0() {
        return ratio;
    }

    public void setRatio0(String ratio) {
        this.ratio = ratio;
    }

    public String getFormat0() {
        return format;
    }

    public void setFormat0(String format) {
        this.format = format;
    }

    public String getSheet0() {
        return sheet;
    }

    public void setSheet0(String sheet) {
        this.sheet = sheet;
    }

    public String getQty0() {
        return qty;
    }

    public void setQty0(String qty) {
        this.qty = qty;
    }

    public String getColor0() {
        return color;
    }

    public void setColor0(String color) {
        this.color = color;
    }


    public String getPages0() {
        return pages;
    }

    public void setPages0(String pages) {
        this.pages = pages;
    }
}
