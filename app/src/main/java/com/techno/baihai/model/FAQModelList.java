package com.techno.baihai.model;

public class FAQModelList {

    private String faq_id;
    private String faq_name;
    private String faq_status;


    public FAQModelList(String faq_id, String faq_name, String faq_status) {
        this.faq_id = faq_id;
        this.faq_name = faq_name;
        this.faq_status = faq_status;
    }


    public String getFaq_id() {
        return faq_id;
    }

    public void setFaq_id(String faq_id) {
        this.faq_id = faq_id;
    }

    public String getFaq_name() {
        return faq_name;
    }

    public void setFaq_name(String faq_name) {
        this.faq_name = faq_name;
    }

    public String getFaq_status() {
        return faq_status;
    }

    public void setFaq_status(String faq_status) {
        this.faq_status = faq_status;
    }


}
