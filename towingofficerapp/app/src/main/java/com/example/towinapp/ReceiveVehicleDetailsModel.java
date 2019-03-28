package com.example.towinapp;

import com.example.towinapp.AddVehicleModel;
import com.example.towinapp.ZonalOfficerModel;

public class ReceiveVehicleDetailsModel {

    private String documentPhotoUrl;
    private String personPhotoUrl;
    private String name;
    private String mobile;
    private String email;
    private String address;
    private String extraAmount;
    private String reasonExatraAmount;
    private String totalAmount;
    private String zonalOfficerUuid;
    private String towingVehiclePushKey;
    private AddVehicleModel addVehicleModel;
    private ZonalOfficerModel zonalOfficerModel;
    private String date;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public ReceiveVehicleDetailsModel(String documentPhotoUrl, String personPhotoUrl, String name, String mobile, String email, String address, String extraAmount, String reasonExatraAmount, String totalAmount, String zonalOfficerUuid, String towingVehiclePushKey, String date) {
        this.documentPhotoUrl = documentPhotoUrl;
        this.personPhotoUrl = personPhotoUrl;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.extraAmount = extraAmount;
        this.reasonExatraAmount = reasonExatraAmount;
        this.totalAmount = totalAmount;
        this.zonalOfficerUuid = zonalOfficerUuid;
        this.towingVehiclePushKey = towingVehiclePushKey;
        this.date = date;
    }

    public ReceiveVehicleDetailsModel(String documentPhotoUrl, String personPhotoUrl, String name, String mobile, String email, String address, String extraAmount, String reasonExatraAmount, String totalAmount, String zonalOfficerUuid, String towingVehiclePushKey, AddVehicleModel addVehicleModel, ZonalOfficerModel zonalOfficerModel, String date) {
        this.documentPhotoUrl = documentPhotoUrl;
        this.personPhotoUrl = personPhotoUrl;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.extraAmount = extraAmount;
        this.reasonExatraAmount = reasonExatraAmount;
        this.totalAmount = totalAmount;
        this.zonalOfficerUuid = zonalOfficerUuid;
        this.towingVehiclePushKey = towingVehiclePushKey;
        this.addVehicleModel = addVehicleModel;
        this.zonalOfficerModel = zonalOfficerModel;
        this.date = date;
    }

    public AddVehicleModel getAddVehicleModel() {
        return addVehicleModel;
    }

    public void setAddVehicleModel(AddVehicleModel addVehicleModel) {
        this.addVehicleModel = addVehicleModel;
    }

    public ZonalOfficerModel getZonalOfficerModel() {
        return zonalOfficerModel;
    }

    public void setZonalOfficerModel(ZonalOfficerModel zonalOfficerModel) {
        this.zonalOfficerModel = zonalOfficerModel;
    }

    public String getTowingVehiclePushKey() {
        return towingVehiclePushKey;
    }

    public void setTowingVehiclePushKey(String towingVehiclePushKey) {
        this.towingVehiclePushKey = towingVehiclePushKey;
    }

    public ReceiveVehicleDetailsModel(String documentPhotoUrl, String personPhotoUrl, String name, String mobile, String email, String address, String extraAmount, String reasonExatraAmount, String totalAmount, String zonalOfficerUuid, String towingVehiclePushKey) {
        this.documentPhotoUrl = documentPhotoUrl;
        this.personPhotoUrl = personPhotoUrl;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.extraAmount = extraAmount;
        this.reasonExatraAmount = reasonExatraAmount;
        this.totalAmount = totalAmount;
        this.zonalOfficerUuid = zonalOfficerUuid;
        this.towingVehiclePushKey = towingVehiclePushKey;
    }

    public ReceiveVehicleDetailsModel() {
    }

    public ReceiveVehicleDetailsModel(String documentPhotoUrl, String personPhotoUrl, String name, String mobile, String email, String address, String extraAmount, String reasonExatraAmount, String totalAmount, String zonalOfficerUuid) {
        this.documentPhotoUrl = documentPhotoUrl;
        this.personPhotoUrl = personPhotoUrl;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.extraAmount = extraAmount;
        this.reasonExatraAmount = reasonExatraAmount;
        this.totalAmount = totalAmount;
        this.zonalOfficerUuid = zonalOfficerUuid;
    }


    public String getDocumentPhotoUrl() {
        return documentPhotoUrl;
    }

    public void setDocumentPhotoUrl(String documentPhotoUrl) {
        this.documentPhotoUrl = documentPhotoUrl;
    }

    public String getPersonPhotoUrl() {
        return personPhotoUrl;
    }

    public void setPersonPhotoUrl(String personPhotoUrl) {
        this.personPhotoUrl = personPhotoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExtraAmount() {
        return extraAmount;
    }

    public void setExtraAmount(String extraAmount) {
        this.extraAmount = extraAmount;
    }

    public String getReasonExatraAmount() {
        return reasonExatraAmount;
    }

    public void setReasonExatraAmount(String reasonExatraAmount) {
        this.reasonExatraAmount = reasonExatraAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getZonalOfficerUuid() {
        return zonalOfficerUuid;
    }

    public void setZonalOfficerUuid(String zonalOfficerUuid) {
        this.zonalOfficerUuid = zonalOfficerUuid;
    }
}
