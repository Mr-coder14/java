package com.example.java;

public class Fileinmodel {
    String uri0;
    String userid0, orderid0;
    String name0,username;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    String notes;
    boolean orderd,delivered;
    String finalamt0;
    String ratio0;
    String format0;
    String sheet0;
    String qty0;
    String color0;
    String pages0;
    String perqtyamt0;
    String orderDate0;
    String perpage0;
    Boolean paid;

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String deliveyamt0;
    String grandTotal0;

    public  Fileinmodel(String name0, String uri0, String GrandTotal0, String orderid0,boolean delivered,String username){
        this.name0 = name0;
        this.uri0 = uri0;
        this.delivered=delivered;
        this.grandTotal0 = GrandTotal0;
        this.username=username;
        this.orderid0=orderid0;
    }


    public String getPerpage0() {
        return perpage0;
    }

    public boolean isOrderd() {
        return orderd;
    }

    public void setOrderd(boolean orderd) {
        this.orderd = orderd;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public void setPerpage0(String perpage) {
        this.perpage0 = perpage;
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
        this.grandTotal0=grandTotal0;
    }

    public Fileinmodel(){

    }

    public String getDeliveyamt0() {
        return deliveyamt0;
    }

    public void setDeliveyamt0(String deliveyamt) {
        this.deliveyamt0 = deliveyamt;
    }




    public String getOrderDate0() {
        return orderDate0;
    }

    public void setOrderDate0(String orderDate) {
        this.orderDate0 = orderDate;
    }

    public Fileinmodel(String name0, String uri0, String userid0, String amt, String ratio, String format, String sheet, String color, String qty, String pages, String orderid0, String perqtyamt, String perpage, String deliveyamt,boolean orderd,boolean delivered,String notes,String username){
        this.name0 = name0;
        this.uri0 = uri0;
        this.deliveyamt0 =deliveyamt;
        this.userid0 = userid0;
        this.finalamt0 =amt;
        this.perpage0 =perpage;
        this.notes=notes;
        this.orderid0 = orderid0;
        this.username=username;
        this.ratio0 =ratio;
        this.orderd=orderd;
        this.delivered=delivered;
        this.color0 =color;
        this.perqtyamt0 =perqtyamt;
        this.format0 =format;
        this.sheet0 =sheet;
        this.qty0 =qty;
        this.pages0 =pages;
    }

    public void setuserid0(String userid) {
        this.userid0 = userid;
    }
    public String getuserid0(){
        return userid0;
    }

    public String getPerqtyamt0() {
        return perqtyamt0;
    }

    public void setPerqtyamt0(String perqtyamt) {
        this.perqtyamt0 = perqtyamt;
    }

    public String getFinalamt0(){
        return finalamt0;
    }

    public String getOrderid0() {
        return orderid0;
    }

    public void setOrderid0(String orderid) {
        this.orderid0 = orderid;
    }

    public void setFinalamt0(String finalamt){
        this.finalamt0 = finalamt;
    }
    public String getName0(){
        return this.name0;
    }

    public String getUri0()
    {
        return this.uri0;
    }

    public String getRatio0() {
        return ratio0;
    }

    public void setRatio0(String ratio) {
        this.ratio0 = ratio;
    }

    public String getFormat0() {
        return format0;
    }

    public void setFormat0(String format) {
        this.format0 = format;
    }

    public String getSheet0() {
        return sheet0;
    }

    public void setSheet0(String sheet) {
        this.sheet0 = sheet;
    }

    public String getQty0() {
        return qty0;
    }

    public void setQty0(String qty) {
        this.qty0 = qty;
    }

    public String getColor0() {
        return color0;
    }

    public void setColor0(String color) {
        this.color0 = color;
    }


    public String getPages0() {
        return pages0;
    }

    public void setPages0(String pages) {
        this.pages0 = pages;
    }
}
