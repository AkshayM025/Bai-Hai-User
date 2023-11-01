package com.techno.baihai.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class GetUserFoundnTransModel {

    @SerializedName("result")
    @Expose
    private List<GetUserFoundnTransDataModel> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<GetUserFoundnTransDataModel> getResult() {
        return result;
    }

    public void setResult(List<GetUserFoundnTransDataModel> result) {
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

