package com.example.java;

import android.net.Uri;

public class User {
    private String name;
    private String email;
    private String phno;



    public User() {

    }



    public User(String name, String email, String phno) {
        this.name = name;
        this.email = email;
        this.phno=phno;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    public  String getPhno()
    {
        return phno;
    }
}

