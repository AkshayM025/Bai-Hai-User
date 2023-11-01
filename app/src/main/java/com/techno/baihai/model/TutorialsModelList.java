package com.techno.baihai.model;

public class TutorialsModelList {

    private String tutorial_id;
    private String tutorial_name;
    private String tutorial_status;
    private String tutorial_language;
    private String tutorial_url;


    public TutorialsModelList(String tutorial_id, String tutorial_name, String tutorial_status, String tutorial_language,String tutorial_url) {
        this.tutorial_id = tutorial_id;
        this.tutorial_name = tutorial_name;
        this.tutorial_status = tutorial_status;
        this.tutorial_language = tutorial_language;
        this.tutorial_url=tutorial_url;
    }


    public String getTutorial_id() {
        return tutorial_id;
    }

    public void setTutorial_id(String tutorial_id) {
        this.tutorial_id = tutorial_id;
    }

    public String getTutorial_name() {
        return tutorial_name;
    }

    public void setTutorial_name(String tutorial_name) {
        this.tutorial_name = tutorial_name;
    }

    public String getTutorial_status() {
        return tutorial_status;
    }

    public void setTutorial_status(String tutorial_status) {
        this.tutorial_status = tutorial_status;
    }

    public String getTutorial_language() {
        return tutorial_language;
    }

    public void setTutorial_language(String tutorial_language) {
        this.tutorial_language = tutorial_language;
    }

    public String getTutorial_url() {
        return tutorial_url;
    }

    public void setTutorial_url(String tutorial_url) {
        this.tutorial_url = tutorial_url;
    }


}
