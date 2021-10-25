package com.techno.baihai.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaiHaiTransactionDataModel {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("trans_id")
    @Expose
    public String transId;
    @SerializedName("payment_type")
    @Expose
    public String paymentType;
    @SerializedName("date_time")
    @Expose
    public String dateTime;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("currency")
    @Expose
    public String currency;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}