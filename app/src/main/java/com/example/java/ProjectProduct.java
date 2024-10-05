package com.example.java;

public class ProjectProduct {
    private String name,model,price,description;
    private String orderDate;
    private String orderTime;
    private String username;
    private String phno;

    public ProjectProduct(String name, String model, String price, String description,String orderDate,String orderTime,String username,String phno) {
        this.name = name;
        this.model = model;
        this.price = price;
        this.orderTime = orderTime;
        this.username = username;
        this.phno=phno;
        this.orderDate=orderDate;
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public ProjectProduct() {
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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
