package com.techno.baihai.model;

public class CategoryList {

    private String category_name, category_id;
    private String category_imageurl;

    public CategoryList(String category_id, String category_name, String category_imageurl) {
        this.category_name = category_name;
        this.category_imageurl = category_imageurl;
        this.category_id = category_id;

    }


    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_imageurl() {
        return category_imageurl;
    }

    public void setCategory_imageurl(String category_imageurl) {
        this.category_imageurl = category_imageurl;
    }
}
