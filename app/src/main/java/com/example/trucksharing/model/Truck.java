package com.example.trucksharing.model;

import java.io.Serializable;

public class Truck implements Serializable {

    private int id;
    private String imageName;
    private String name;
    private String status;

    public Truck() {

    }

    public Truck(String imageName, String name, String status) {
        this.imageName = imageName;
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String shareContent() {
        return "Truck information: \n" + this.getName() + "\n" + this.getStatus();
    }
}
