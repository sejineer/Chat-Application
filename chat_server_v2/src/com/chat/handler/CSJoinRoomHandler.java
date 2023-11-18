package com.chat.handler;

import com.chat.Message;
import com.google.gson.JsonObject;

public class CSJoinRoomHandler implements MessageHandler {

    @Override
    public void handle(Message message) {
        JsonObject jsonData = message.getJsonData();
        String name = jsonData.get("name").getAsString();
    }

}
