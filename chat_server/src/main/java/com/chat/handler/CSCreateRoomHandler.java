package com.chat.handler;

import com.chat.message.Message;
import com.google.gson.JsonObject;

public class CSCreateRoomHandler implements MessageHandler {

    @Override
    public void handle(Message message) {
        JsonObject jsonData = message.getJsonData();
        String name = jsonData.get("name").getAsString();
    }

}
