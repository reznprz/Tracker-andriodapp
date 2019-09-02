package com.example.macbookpro.tracker;

/**
 * Created by macbookpro on 3/11/18.
 */

public class Stops {



    String id;
    String name;
    String time;
    String latLng;
    boolean isNotifyEnabled=false;

    public Stops(String id, String name, String time, String latLng) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.latLng = latLng;
    }

    public boolean isNotifyEnabled() {
        return isNotifyEnabled;
    }

    public void setNotifyEnabled(boolean notifyEnabled) {
        isNotifyEnabled = notifyEnabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }
}
