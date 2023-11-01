package com.techno.baihai.model;

import java.io.Serializable;

public class ChatModel implements Serializable {

    private String sender_id;

    private String receiver_id;


    private String receiver_image;

    private String sender_image;


    private String chat_message;

    private String chat_image;

    private String chat_audio;

    private String chat_video;

    private String chat_document;

    private String status;

    private String date;

    private String time;




    public String getReceiver_image() {
        return receiver_image;
    }

    public void setReceiver_image(String receiver_image) {
        this.receiver_image = receiver_image;
    }

    public String getSender_image() {
        return sender_image;
    }

    public void setSender_image(String sender_image) {
        this.sender_image = sender_image;
    }


    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getChat_message() {
        return chat_message;
    }

    public void setChat_message(String chat_message) {
        this.chat_message = chat_message;
    }

    public String getChat_image() {
        return chat_image;
    }

    public void setChat_image(String chat_image) {
        this.chat_image = chat_image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getChat_audio() {
        return chat_audio;
    }

    public void setChat_audio(String chat_audio) {
        this.chat_audio = chat_audio;
    }

    public String getChat_video() {
        return chat_video;
    }

    public void setChat_video(String chat_video) {
        this.chat_video = chat_video;
    }

    public String getChat_document() {
        return chat_document;
    }

    public void setChat_document(String chat_document) {
        this.chat_document = chat_document;
    }
}
