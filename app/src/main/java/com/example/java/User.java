package com.example.java;

import android.net.Uri;

public class User {
    private String name;
    private String email;
    private String phno,collegename;



    public User() {

    }



    public User(String name, String email, String phno,String Collegename) {
        this.name = name;
        this.email = email;
        this.phno=phno;
        this.collegename=Collegename;
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

