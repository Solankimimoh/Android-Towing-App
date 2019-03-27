package com.example.towinapp;

public class PenltyPriceModel {

    private String twoWheelerPenelty;
    private String fourWheelerPenelty;


    public PenltyPriceModel() {
    }

    public PenltyPriceModel(String twoWheelerPenelty, String fourWheelerPenelty) {
        this.twoWheelerPenelty = twoWheelerPenelty;
        this.fourWheelerPenelty = fourWheelerPenelty;
    }

    public String getTwoWheelerPenelty() {
        return twoWheelerPenelty;
    }

    public void setTwoWheelerPenelty(String twoWheelerPenelty) {
        this.twoWheelerPenelty = twoWheelerPenelty;
    }

    public String getFourWheelerPenelty() {
        return fourWheelerPenelty;
    }

    public void setFourWheelerPenelty(String fourWheelerPenelty) {
        this.fourWheelerPenelty = fourWheelerPenelty;
    }
}
