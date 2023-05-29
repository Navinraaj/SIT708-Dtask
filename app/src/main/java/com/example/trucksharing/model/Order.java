package com.example.trucksharing.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Random;

public class Order implements Serializable {

    int orderId;
    int userId;

    String receiverName;
    String imageName;

    String pickupLocation;
    double pickupLatitude;
    double pickupLongitude;

    String dropOffLocation;
    double dropOffLatitude;
    double dropOffLongitude;

    String pickupDate;
    String pickupTime;
    String goodType;
    String vehicleType;
    String driverName;
    String driverPhoneNumber;

    Double weight;
    Double width;
    Double height;
    Double length;
    Integer quantity;

    public Order(int userId, String receiverName, String imageName, String pickupLocation, double pickupLatitude, double pickupLongitude, String dropOffLocation, double dropOffLatitude, double dropOffLongitude, String pickupDate, String pickupTime, String goodType, String vehicleType, String driverName, String driverPhoneNumber, Double weight, Double width, Double height, Double length, Integer quantity) {
        this.userId = userId;
        this.receiverName = receiverName;
        this.imageName = imageName;

        this.pickupLocation = pickupLocation;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;

        this.dropOffLocation = dropOffLocation;
        this.dropOffLatitude = dropOffLatitude;
        this.dropOffLongitude = dropOffLongitude;

        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.goodType = goodType;
        this.vehicleType = vehicleType;
        this.driverName = driverName;
        this.driverPhoneNumber = driverPhoneNumber;
        this.weight = weight;
        this.width = width;
        this.height = height;
        this.length = length;
        this.quantity = quantity;
    }

    public Order() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropOffLocation() {
        return dropOffLocation;
    }

    public void setDropOffLocation(String dropOffLocation) {
        this.dropOffLocation = dropOffLocation;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getGoodType() {
        return goodType;
    }

    public void setGoodType(String goodType) {
        this.goodType = goodType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    public double getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(double pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public double getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(double pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public double getDropOffLatitude() {
        return dropOffLatitude;
    }

    public void setDropOffLatitude(double dropOffLatitude) {
        this.dropOffLatitude = dropOffLatitude;
    }

    public double getDropOffLongitude() {
        return dropOffLongitude;
    }

    public void setDropOffLongitude(double dropOffLongitude) {
        this.dropOffLongitude = dropOffLongitude;
    }

    public String getEstDeliveryTime() {
        // Assume that this time = pickup time + 24 hours
        return this.pickupTime + " round 9PM";
    }

    public String getApproxFare(Double fare) {
        // Formula: 2 dollar per minute
        return "$" + fare;
    }

    public int getApproxTravelTime() {
        int random = new Random().nextInt(49) + 10; // from 10 -> 59
        return random;
    }

    public String getApproxTravelTimeInString(int timeInMinute) {
        if (timeInMinute == 1) {
            return "1 minute";
        } else {
            return timeInMinute + " minutes";
        }
    }

    public String shareContent() {
        return
                "Order information: \n" +
                        "To: " + this.getReceiverName() + "\n" +
                        "Good type: " + this.getGoodType() + "\n" +
                        "Weight: " + this.getWeight().toString() + "\n" +
                        "Height: " + this.getHeight().toString() + "\n" +
                        "Width: " + this.getWidth().toString() + "\n" +
                        "Length: " + this.getLength().toString() + "\n" +
                        "Vehicle type: " + this.getVehicleType() + "\n";
    }
}
