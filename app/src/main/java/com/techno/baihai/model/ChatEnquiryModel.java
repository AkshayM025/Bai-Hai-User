package com.techno.baihai.model;

public class ChatEnquiryModel {


    private String requestId;
    private String status;

    private String chat_username;



    private String chat_message;


    private String chat_imgUrl;

    private String product_name;
    private String product_id;
    private String date_time;


    private String enquiry_productImageUrl;


    public ChatEnquiryModel(String requestId, String status, String chat_username, String chat_imgUrl,
                            String product_name, String product_id, String date_time, String enquiry_productImageUrl,String message) {
        this.requestId = requestId;
        this.status = status;
        this.chat_username = chat_username;
        this.chat_imgUrl = chat_imgUrl;
        this.product_name = product_name;
        this.product_id = product_id;
        this.date_time = date_time;
        this.enquiry_productImageUrl = enquiry_productImageUrl;
        this.chat_message = message;
    }

    public String getChat_message() {
        return chat_message;
    }

    public void setChat_message(String chat_message) {
        this.chat_message = chat_message;
    }
    public String getChat_imgUrl() {
        return chat_imgUrl;
    }

    public void setChat_imgUrl(String chat_imgUrl) {
        this.chat_imgUrl = chat_imgUrl;
    }


    public String getChat_username() {
        return chat_username;
    }

    public void setChat_username(String chat_username) {
        this.chat_username = chat_username;
    }

    public String getEnquiry_productImageUrl() {
        return enquiry_productImageUrl;
    }

    public void setEnquiry_productImageUrl(String enquiry_productImageUrl) {
        this.enquiry_productImageUrl = enquiry_productImageUrl;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }


    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
