package com.example.lab1;

public class MessageModel {
    private boolean right, left;
    private String message;
    private long id;

    public MessageModel() {
        this(false, false, "", -1);
    }

    public MessageModel(boolean right, boolean left, String message, long id) {
        this.right = right;
        this.left = left;
        this.message = message;
        this.id = id;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

