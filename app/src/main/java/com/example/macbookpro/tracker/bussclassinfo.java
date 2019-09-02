package com.example.macbookpro.tracker;

public class bussclassinfo {

    String busid, busplate, make, color;

    public  bussclassinfo(String busid, String busplate, String make, String color){
        busid = busid;
        busplate = busplate;
        make = make;
        color = color;



    }

    public String getBusid() {
        return busid;
    }

    public void setBusid(String busid) {
        this.busid = busid;
    }

    public String getBusplate() {
        return busplate;
    }

    public void setBusplate(String busplate) {
        this.busplate = busplate;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
