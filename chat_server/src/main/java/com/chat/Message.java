package com.chat;

public class Message {

    private String type;
    private String sender;
    private String message;

    public Message(String type, String sender, String message) {
        this.type = type;
        this.sender = sender;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

}

