package com.example.lab1;
public class MessageModel {

    private long id;
    private String message;
    private String type;


    public MessageModel() {
    }

    public MessageModel(String message, String type) {
        this.message = message;
        this.type = type;
    }

    public MessageModel(long id, String message, String type) {
        this(message,type);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

