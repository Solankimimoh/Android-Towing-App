package com.example.towinapp;

public class AddVehicleModel {


    private String pushKey;

    private String vehicleNumber;
    private String photoUrl;
    private String towinArea;
    private String towingZonePushKey;
    private String date;
    private String time;
    private String uniqueChallan;
    private String fineAmount;
    private String policeUUID;
    private boolean verifyVehicle;
    private boolean receiveStatus;


    public AddVehicleModel() {
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public AddVehicleModel(String vehicleNumber, String photoUrl, String towinArea, String towingZonePushKey, String date, String time, String uniqueChallan, String fineAmount, String policeUUID, boolean verifyVehicle, boolean receiveStatus) {
        this.vehicleNumber = vehicleNumber;
        this.photoUrl = photoUrl;
        this.towinArea = towinArea;
        this.towingZonePushKey = towingZonePushKey;
        this.date = date;
        this.time = time;
        this.uniqueChallan = uniqueChallan;
        this.fineAmount = fineAmount;
        this.policeUUID = policeUUID;
        this.verifyVehicle = verifyVehicle;
        this.receiveStatus = receiveStatus;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTowinArea() {
        return towinArea;
    }

    public void setTowinArea(String towinArea) {
        this.towinArea = towinArea;
    }

    public String getTowingZonePushKey() {
        return towingZonePushKey;
    }

    public void setTowingZonePushKey(String towingZonePushKey) {
        this.towingZonePushKey = towingZonePushKey;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUniqueChallan() {
        return uniqueChallan;
    }

    public void setUniqueChallan(String uniqueChallan) {
        this.uniqueChallan = uniqueChallan;
    }

    public String getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(String fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getPoliceUUID() {
        return policeUUID;
    }

    public void setPoliceUUID(String policeUUID) {
        this.policeUUID = policeUUID;
    }

    public boolean isVerifyVehicle() {
        return verifyVehicle;
    }

    public void setVerifyVehicle(boolean verifyVehicle) {
        this.verifyVehicle = verifyVehicle;
    }

    public boolean isReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(boolean receiveStatus) {
        this.receiveStatus = receiveStatus;
    }
}
