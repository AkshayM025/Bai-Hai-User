package com.techno.baihai.model;

public class User {

    private final String id;
    private final String username;
    private final String email;
    private final String phone;
    private final String image;
    private final String password;

    public User(String id, String username, String email, String password, String phone, String image) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.image = image;

    }

    public String getImage() {
        return image;
    }

    public String getPhone() {
        return phone;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }

}