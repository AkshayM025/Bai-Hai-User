package com.techno.baihai.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RewardsHistoryModel {

    @SerializedName("result")
    @Expose
    private List<Result> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
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



public static class Result {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("min_coin")
    @Expose
    private String min_coin;
    @SerializedName("max_coin")
    @Expose
    private String max_coin;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("message")
    @Expose
    private String message;

    public Result(String id, String name, String min_coin, String max_coin, String image,String message) {
        this.name = name;
        this.id = id;
        this.min_coin = min_coin;
        this.max_coin = max_coin;
        this.image = image;
        this.message = message;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinCoin() {
        return min_coin;
    }

    public void setMinCoin(String min_coin) {
        this.min_coin = min_coin;
    }

    public String getMaxCoin() {
        return max_coin;
    }

    public void setMaxCoin(String max_coin) {
        this.max_coin = max_coin;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
}