package com.example.java;

public class Fileinmodel {
    String uri;
    String userid,orderid;
    String name;
    String finalamt,ratio,format,sheet,qty,color,pages,perqtyamt,orderDate,perpage,deliveyamt,GrandTotal;

    public  Fileinmodel(String name,String uri,String GrandTotal,String orderid){
        this.name=name;
        this.uri=uri;
        this.GrandTotal=GrandTotal;
        this.orderid=orderid;
    }


    public String getPerpage0() {
        return perpage;
    }

    public void setPerpage0(String perpage) {
        this.perpage = perpage;
    }
    public void setName0(String name) {
        this.name = name;
    }
    public void setUri0(String uri) {
        this.uri = uri;
    }

    public String getGrandTotal0() {
        return GrandTotal;
    }

    public void setGrandTotal0(String grandTotal) {
        GrandTotal = grandTotal;
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

    public Fileinmodel(String name, String uri, String userid, String amt, String ratio, String format, String sheet, String color, String qty, String pages, String orderid, String perqtyamt,String perpage,String deliveyamt){
        this.name=name;
        this.uri=uri;
        this.deliveyamt=deliveyamt;
        this.userid =userid;
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

    public void setuserid0(String userid) {
        this.userid = userid;
    }
    public String getuserid0(){
        return userid;
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
        return orderid;
    }

    public void setOrderid0(String orderid) {
        this.orderid = orderid;
    }

    public void setFinalamt0(String finalamt){
        this.finalamt = finalamt;
    }
    public String getName0(){
        return this.name;
    }

    public String getUri0()
    {
        return this.uri;
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
