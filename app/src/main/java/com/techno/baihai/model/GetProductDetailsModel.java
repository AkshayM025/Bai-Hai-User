package com.techno.baihai.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetProductDetailsModel {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("driver_id")
    @Expose
    public String driverId;
    @SerializedName("organization_id")
    @Expose
    public String organizationId;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("product")
    @Expose
    public String product;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("lat")
    @Expose
    public String lat;
    @SerializedName("lon")
    @Expose
    public String lon;
    @SerializedName("size")
    @Expose
    public String size;
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

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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