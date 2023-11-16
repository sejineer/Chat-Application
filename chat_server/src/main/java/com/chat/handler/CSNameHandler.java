package com.chat.handler;

import com.chat.Message;
import com.google.gson.JsonObject;

import java.nio.channels.SocketChannel;

public class CSNameHandler implements MessageHandler {

    @Override
    public void handle(Message message) {
        JsonObject jsonData = message.getJsonData();
        String name = jsonData.get("name").getAsString();


    }

}
