package com.example.playersclubapi;

import android.graphics.Bitmap;

public class Staff {
     String id,name, phone, email;
     Bitmap imageB;
     String imageS;
     int idd;
    public  Staff(String name, String phone, String email, String id, Bitmap image ){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.imageB = image;
    }
    public  Staff(String idd,String name, String phone, String email, String image){
        this.id = idd;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.imageS = image;
    }
    public  Staff(String name, String phone, String email, String image){
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.imageS = image;
    }

    public int getId() {
        return idd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
