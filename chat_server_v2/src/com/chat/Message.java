package com.chat;

import com.google.gson.JsonObject;

import java.nio.channels.SocketChannel;

public class Message {

    private String type;
    private JsonObject jsonData;
    private SocketChannel clientChannel;

    public Message(String type, JsonObject jsonData, SocketChannel clientChannel) {
        this.type = type;
        this.jsonData = jsonData;
        this.clientChannel = clientChannel;
    }

    public String getType() {
        return type;
    }

    public JsonObject getJsonData() {
        return jsonData;
    }

    public void setClientChannel(SocketChannel clientChannel) {
        this.clientChannel = clientChannel;
    }

    public SocketChannel getClientChannel() {
        return clientChannel;
    }

}

