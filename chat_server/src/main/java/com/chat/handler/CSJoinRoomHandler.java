package com.chat.handler;

import com.chat.Message;
import org.json.JSONObject;

public class CSJoinRoomHandler implements MessageHandler {

    @Override
    public void handle(Message message) {
        JSONObject jsonData = message.getJsonData();
        String name = jsonData.getString("name");
    }

}