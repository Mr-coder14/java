package com.RapCode.java;

public class BookModel {
    private String bookName;
    private String authorName;
    private String launchedYear;
    private String isbn;

    private String publisher;
    private String price;
    private String description;
    private String username;
    private String phno;



    private String orderDate;
    private String orderTime;

    public BookModel() {
    }

    public BookModel(String name, String model, String price, String description, String orderDate, String orderTime) {
    }

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

    public BookModel(String bookName, String authorName, String launchedYear, String isbn, String publisher, String price, String description, String orderDate, String orderTime, String username, String phno) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.launchedYear = launchedYear;
        this.isbn = isbn;
        this.publisher = publisher;
        this.price = price;
        this.username=username;
        this.phno=phno;
        this.description = description;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
    }

    // Getter and setter methods for all fields

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getLaunchedYear() {
        return launchedYear;
    }

    public void setLaunchedYear(String launchedYear) {
        this.launchedYear = launchedYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
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
}
