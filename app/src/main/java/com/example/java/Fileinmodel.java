package com.example.java;

public class Fileinmodel {
    String fileuri;
    String userID;
    String name;

    public Fileinmodel(){

    }
    public Fileinmodel(String name,String fileuri,String userID){
        this.name=name;
        this.fileuri=fileuri;
        this.userID=userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getuserID(){
        return userID;
    }

    public String getName(){
        return this.name;
    }

    public String getUri()
    {
        return this.fileuri;
    }
}
