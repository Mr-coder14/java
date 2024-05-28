package com.example.java;

import java.security.SecureRandom;

public class Fileinmodel {
    String fileuri;
    String userID;
    String name;
    String amt;

    public Fileinmodel(){

    }
    public Fileinmodel(String name,String fileuri,String userID,String amt){
        this.name=name;
        this.fileuri=fileuri;
        this.userID=userID;
        this.amt=amt;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getuserID(){
        return userID;
    }
    public String getAmt(){
        return amt;
    }

    public void setAmt(String amt){
        this.amt=amt;
    }
    public String getName(){
        return this.name;
    }

    public String getUri()
    {
        return this.fileuri;
    }
}
