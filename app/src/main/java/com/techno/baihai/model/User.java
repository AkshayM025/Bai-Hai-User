package com.techno.baihai.model;

public class User {

    private final String id;
    private final String username;
    private final String email;
    private final String phone;
    private final String image;
    private final String password;
    private final String legal_info;
    private final String guide;
    private final String guide_free;
    private final String guide_give_free;
    private final String suscribe;

    public User(String id, String username, String email, String password, String phone, String image,String legal_info,String guide,String guide_free,String guide_give_free,String suscribe) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.image = image;
        this.legal_info = legal_info;
        this.guide = guide;
        this.guide_free = guide_free;
        this.guide_give_free = guide_give_free;
        this.suscribe=suscribe;

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

    public String getLegalinfo() {
        return legal_info;
    }

    public String getSuscribe() {
        return suscribe;
    }

    public String getGuide() {
        return guide;
    }

    public String getGuideFree() {
        return guide_free;
    }
    public String getGuideGiveFree() {
        return guide_give_free;
    }



    public String getPassword() {
        return password;
    }

}