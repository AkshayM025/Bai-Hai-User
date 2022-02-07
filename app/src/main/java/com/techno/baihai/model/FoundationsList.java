package com.techno.baihai.model;

public class FoundationsList {

    private String org_id;
    private String organizationName;
    private String contactName;
    private String email;
    private String mobile;
    private String location;
    private String description;
    private String webpage;


    public FoundationsList(String org_id, String org_name, String contact_name, String email, String mobile,String webpage, String location, String description) {
        this.organizationName = org_name;
        this.org_id = org_id;
        this.webpage = webpage;
        this.contactName = contact_name;
        this.email = email;
        this.mobile = mobile;
        this.location = location;
        this.description = description;

    }

    public FoundationsList(String organizationName, String contactName) {
        this.organizationName = organizationName;
        this.contactName = contactName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWebpage() {
        return webpage;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }


    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }
}
