package com.example.macbookpro.tracker;

public class busrecord {
    String time, Odameterone_txt, Odametertwo_txt, Vehicle_txt, note;

    public busrecord(String time,String odameterone_txt,String odametertwo_txt,String vehicle_txt,String note){
        time = time;
        Odameterone_txt = odameterone_txt;
        Odametertwo_txt = odametertwo_txt;
        Vehicle_txt= vehicle_txt;
        note=note;


    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOdameterone_txt() {
        return Odameterone_txt;
    }

    public void setOdameterone_txt(String odameterone_txt) {
        Odameterone_txt = odameterone_txt;
    }

    public String getOdametertwo_txt() {
        return Odametertwo_txt;
    }

    public void setOdametertwo_txt(String odametertwo_txt) {
        Odametertwo_txt = odametertwo_txt;
    }

    public String getVehicle_txt() {
        return Vehicle_txt;
    }

    public void setVehicle_txt(String vehicle_txt) {
        Vehicle_txt = vehicle_txt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
