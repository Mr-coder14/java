package com.example.java;

public class User {
    private String name;
    private String email;
    private String userid;
    private String phno,collegename;
    private String profileuri;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public User() {

    }

    public User(String name, String email, String phno,String userid) {
        this.name = name;
        this.email = email;
        this.phno=phno;
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

