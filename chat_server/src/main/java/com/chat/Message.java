package com.chat;


import com.google.gson.JsonObject;

import java.nio.channels.SocketChannel;

public class Message {

    private String type;
    private JsonObject jsonData;
    private SocketChannel clientChannel;
    private Client client;

    public Message(String type, JsonObject jsonData, SocketChannel clientChannel, Client client) {
        this.type = type;
        this.jsonData = jsonData;
        this.clientChannel = clientChannel;
        this.client = client;
    }

    public String getType() {
        return type;
    }

    public Client getClient() {
        return client;
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

