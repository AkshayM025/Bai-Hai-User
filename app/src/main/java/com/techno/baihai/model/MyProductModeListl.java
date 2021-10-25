package com.techno.baihai.model;

public class MyProductModeListl {

    //http://bai-hai.com/webservice/get_product_by_category?category_id=1&user_id=1&distance=10  /*
    //      "id": "1",
        /*  "user_id": "12",
          "category_id": "1",
          "name": "testproduct",
          "description": "thisis test",
          "price": "0.00",
          "address": "vijay",
          "lat": "789456",
          "lon": "5464",
          "used": "No",
          "image1": "",
          "image2": "",
          "image3": "",
          "image4": "",
          "status": "Active",
          "date_time": "2020-09-22 03:57:15"
          "totalchat": "1"*/


    private String product_id;


    private String seller_id;


    private String Product_seller_name;
    private String product_category_id;
    private String product_name;
    private String product_description;
    private String product_price;
    private String product_address;
    private String product_used;
    private String product_image1Url;

    private String category_image;
    private String category_name;

    private  String product_lat;
    private  String product_status;

    public String getProduct_status() {
        return product_status;
    }

    public void setProduct_status(String product_status) {
        this.product_status = product_status;
    }

    public String getProduct_dateTime() {
        return product_dateTime;
    }

    public void setProduct_dateTime(String product_dateTime) {
        this.product_dateTime = product_dateTime;
    }

    private  String product_dateTime;


    public String getProduct_lat() {
        return product_lat;
    }

    public void setProduct_lat(String product_lat) {
        this.product_lat = product_lat;
    }

    public String getProduct_lon() {
        return product_lon;
    }

    public void setProduct_lon(String product_lon) {
        this.product_lon = product_lon;
    }

    private String product_lon;



    private String product_IntrustTotalCount;


    public MyProductModeListl(String product_id, String seller_id, String Product_seller_name,
                              String product_category_id, String product_name, String product_description,
                              String product_price, String product_address, String product_used,
                              String product_image1Url, String product_IntrustTotalCount,
                              String category_image, String category_name, String product_lat,
                              String product_lon, String product_status, String product_dateTime) {

        this.product_id = product_id;
        this.seller_id = seller_id;
        this.Product_seller_name = Product_seller_name;
        this.product_category_id = product_category_id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.product_price = product_price;
        this.product_address = product_address;
        this.product_used = product_used;
        this.product_image1Url = product_image1Url;
        this.product_IntrustTotalCount = product_IntrustTotalCount;
        this.category_image = category_image;
        this.category_name = category_name;
        this.product_lat = product_lat;
        this.product_lon = product_lon;
        this.product_dateTime = product_dateTime;
        this.product_status = product_status;






    }



    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }


    public String getProduct_IntrustTotalCount() {
        return product_IntrustTotalCount;
    }

    public void setProduct_IntrustTotalCount(String product_IntrustTotalCount) {
        this.product_IntrustTotalCount = product_IntrustTotalCount;
    }

    public String getProduct_seller_name() {
        return Product_seller_name;
    }

    public void setProduct_seller_name(String product_seller_name) {
        Product_seller_name = product_seller_name;
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


    public String getProduct_category_id() {
        return product_category_id;
    }

    public void setProduct_category_id(String product_category_id) {
        this.product_category_id = product_category_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_address() {
        return product_address;
    }

    public void setProduct_address(String product_address) {
        this.product_address = product_address;
    }

    public String getProduct_used() {
        return product_used;
    }

    public void setProduct_used(String product_used) {
        this.product_used = product_used;
    }

    public String getProduct_image1Url() {
        return product_image1Url;
    }

    public void setProduct_image1Url(String product_image1Url) {
        this.product_image1Url = product_image1Url;
    }
}




