package com.techno.baihai.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFoundationDetailsModel {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("org_name")
    @Expose
    public String orgName;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("lat")
    @Expose
    public String lat;
    @SerializedName("lon")
    @Expose
    public String lon;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("phone_no")
    @Expose
    public String phoneNo;
    @SerializedName("contact_name")
    @Expose
    public String contactName;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("register_id")
    @Expose
    public String registerId;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("date_time")
    @Expose
    public String dateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


}
