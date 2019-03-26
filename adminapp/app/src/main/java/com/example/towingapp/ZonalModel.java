package com.example.towingapp;

public class ZonalModel {

    private String pushKey;
    private String zonalName;
    private String zonalAddress;


    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public ZonalModel(String pushKey, String zonalName, String zonalAddress) {
        this.pushKey = pushKey;
        this.zonalName = zonalName;
        this.zonalAddress = zonalAddress;
    }

    public ZonalModel() {
    }

    public ZonalModel(String zonalName, String zonalAddress) {
        this.zonalName = zonalName;
        this.zonalAddress = zonalAddress;
    }


    public String getZonalName() {
        return zonalName;
    }

    public void setZonalName(String zonalName) {
        this.zonalName = zonalName;
    }

    public String getZonalAddress() {
        return zonalAddress;
    }

    public void setZonalAddress(String zonalAddress) {
        this.zonalAddress = zonalAddress;
    }
}
