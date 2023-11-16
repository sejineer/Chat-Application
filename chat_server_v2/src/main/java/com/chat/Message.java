package com.chat;

import org.json.JSONObject;

import java.nio.channels.SocketChannel;

public class Message {

    private String type;
    private JSONObject jsonData;
    private SocketChannel clientChannel;

    public Message(String type, JSONObject jsonData, SocketChannel clientChannel) {
        this.type = type;
        this.jsonData = jsonData;
        this.clientChannel = clientChannel;
    }

    public String getType() {
        return type;
    }

    public JSONObject getJsonData() {
        return jsonData;
    }

    public void setClientChannel(SocketChannel clientChannel) {
        this.clientChannel = clientChannel;
    }

    public SocketChannel getClientChannel() {
        return clientChannel;
    }

}

