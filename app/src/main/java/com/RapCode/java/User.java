package com.RapCode.java;

public class User {
    private String name;
    private String email;
    private String userid;
    private String phno;
    private String collegename;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;
    private String profileuri;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public User() {

    }

    public User(String name, String email, String phno,String userid,String address) {
        this.name = name;
        this.email = email;
        this.phno=phno;
        this.address=address;
        this.userid=userid;

    }

    public String getProfileuri() {
        return profileuri;
    }

    public void setProfileuri(String profileuri) {
        this.profileuri = profileuri;
    }

    public String getName() {
        return name;
    }

    public String getCollegename() {
        return collegename;
    }

    public void setCollegename(String collegename) {
        this.collegename = collegename;
    }

    public String getEmail() {
        return email;
    }
    public  String getPhno()
    {
        return phno;
    }
}

