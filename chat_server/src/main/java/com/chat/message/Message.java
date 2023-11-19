package com.chat.message;


import com.chat.client.Client;
import com.google.gson.JsonObject;


public class Message {

    private String type;
    private JsonObject jsonData;
    private Client client;

    public Message(String type, JsonObject jsonData, Client client) {
        this.type = type;
        this.jsonData = jsonData;
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

}

