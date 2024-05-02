package com.example.java;

public class Fileinmodel {
    String fileuri;
    String name;

    public Fileinmodel(){

    }
    public Fileinmodel(String name,String fileuri){
        this.name=name;
        this.fileuri=fileuri;
    }

    public String getName(){
        return this.name;
    }

    public String getUri()
    {
        return this.fileuri;
    }
}
