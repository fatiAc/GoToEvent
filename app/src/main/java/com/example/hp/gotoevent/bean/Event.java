package com.example.hp.gotoevent.bean;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

/**
 * Created by hp on 18/12/2018.
 */

public class Event {

    private String name;
    private String Description;
    private String lieu;
    private LatLng latLng;
    private Date date;
    private String imgID;
    private List<Role> roles;

    public Event() {
    }

    public Event(String name, String description, String lieu, LatLng latLng, Date date,String imgID) {
        this.name = name;
        Description = description;
        this.lieu = lieu;
        this.latLng = latLng;
        this.date = date;
        this.imgID = imgID;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }

    @Override
    public String toString() {
        return "Event{" +
                ", name='" + name + '\'' +
                '}';
    }
}
