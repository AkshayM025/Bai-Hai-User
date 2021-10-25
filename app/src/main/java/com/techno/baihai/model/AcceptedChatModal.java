package com.techno.baihai.model;




/*"id": "5",
        "user_id": "74",
        "seller_id": "71",
        "product_id": "80",
        "status": "Accepted",
        "date_time": "2020-10-09 04:26:23"*/

public class AcceptedChatModal {


    private String reciever_id;
    private String seller_id;

    private String product_id;


    private String product_name;
    private String product_imgUrl;
    private String product_desc;

    private String sellerchat_name;


    private String sellerchat_img;


    private String status;

    private String date_time;


    public AcceptedChatModal(String reciever_id, String seller_id, String product_id, String sellerchat_name,
                             String sellerchat_img, String status, String date_time, String product_name,
                             String product_imgUrl, String product_desc) {

        this.reciever_id = reciever_id;
        this.seller_id = seller_id;
        this.product_id = product_id;
        this.sellerchat_name = sellerchat_name;
        this.sellerchat_img = sellerchat_img;
        this.status = status;
        this.date_time = date_time;
        this.product_name = product_name;
        this.product_imgUrl = product_imgUrl;
        this.product_desc = product_desc;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_imgUrl() {
        return product_imgUrl;
    }

    public void setProduct_imgUrl(String product_imgUrl) {
        this.product_imgUrl = product_imgUrl;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }


    public String getSellerchat_img() {
        return sellerchat_img;
    }

    public void setSellerchat_img(String sellerchat_img) {
        this.sellerchat_img = sellerchat_img;
    }

    public String getSellerchat_name() {
        return sellerchat_name;
    }

    public void setSellerchat_name(String sellerchat_name) {
        this.sellerchat_name = sellerchat_name;
    }


    public String getReciever_id() {
        return reciever_id;
    }

    public void setReciever_id(String reciever_id) {
        this.reciever_id = reciever_id;
    }


    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
