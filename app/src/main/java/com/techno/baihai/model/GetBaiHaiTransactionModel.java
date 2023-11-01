package com.techno.baihai.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetBaiHaiTransactionModel {


    @SerializedName("result")
    @Expose
    public List<BaiHaiTransactionDataModel> result = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public List<BaiHaiTransactionDataModel> getResult() {
        return result;
    }

    public void setResult(List<BaiHaiTransactionDataModel> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}