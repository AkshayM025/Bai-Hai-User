package com.techno.baihai.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class GetProDetailModel implements Serializable {

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
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


    public static class Result implements Serializable {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lon")
        @Expose
        private String lon;
        @SerializedName("used")
        @Expose
        private String used;
        @SerializedName("image1")
        @Expose
        private String image1;
        @SerializedName("image2")
        @Expose
        private String image2;
        @SerializedName("image3")
        @Expose
        private String image3;
        @SerializedName("image4")
        @Expose
        private String image4;
        @SerializedName("image5")
        @Expose
        private String image5;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("date_time")
        @Expose
        private String dateTime;
        @SerializedName("category_details")
        @Expose
        private CategoryDetails categoryDetails;
        @SerializedName("user_details")
        @Expose
        private UserDetails userDetails;

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

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
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

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public String getImage1() {
            return image1;
        }

        public void setImage1(String image1) {
            this.image1 = image1;
        }

        public String getImage2() {
            return image2;
        }

        public void setImage2(String image2) {
            this.image2 = image2;
        }

        public String getImage3() {
            return image3;
        }

        public void setImage3(String image3) {
            this.image3 = image3;
        }

        public String getImage4() {
            return image4;
        }

        public void setImage4(String image4) {
            this.image4 = image4;
        }

        public String getImage5() {
            return image5;
        }

        public void setImage5(String image5) {
            this.image5 = image5;
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

        public CategoryDetails getCategoryDetails() {
            return categoryDetails;
        }

        public void setCategoryDetails(CategoryDetails categoryDetails) {
            this.categoryDetails = categoryDetails;
        }

        public UserDetails getUserDetails() {
            return userDetails;
        }

        public void setUserDetails(UserDetails userDetails) {
            this.userDetails = userDetails;
        }

        public class CategoryDetails implements Serializable {

            @SerializedName("cat_image")
            @Expose
            private String catImage;
            @SerializedName("cat_name")
            @Expose
            private String catName;

            public String getCatImage() {
                return catImage;
            }

            public void setCatImage(String catImage) {
                this.catImage = catImage;
            }

            public String getCatName() {
                return catName;
            }

            public void setCatName(String catName) {
                this.catName = catName;
            }
        }

        public class UserDetails implements Serializable {

            @SerializedName("user_image")
            @Expose
            private String userImage;
            @SerializedName("user_name")
            @Expose
            private String userName;

            public String getUserImage() {
                return userImage;
            }

            public void setUserImage(String userImage) {
                this.userImage = userImage;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

        }
    }
}