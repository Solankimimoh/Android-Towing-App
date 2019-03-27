package com.example.towingapp;

public class ZonalOfficerModel {

    private String name;
    private String email;
    private String password;
    private String mobile;
    private String policeid;
    private String zonalPushKey;


    public ZonalOfficerModel() {
    }

    public ZonalOfficerModel(String name, String email, String password, String mobile, String policeid, String zonalPushKey) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.policeid = policeid;
        this.zonalPushKey = zonalPushKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPoliceid() {
        return policeid;
    }

    public void setPoliceid(String policeid) {
        this.policeid = policeid;
    }

    public String getZonalPushKey() {
        return zonalPushKey;
    }

    public void setZonalPushKey(String zonalPushKey) {
        this.zonalPushKey = zonalPushKey;
    }
}
