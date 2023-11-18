package com.chat.handler;

import com.chat.client.ChatRoom;
import com.chat.client.Client;
import com.chat.message.Message;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class CSRoomsHandler implements MessageHandler {

    private List<ChatRoom> chatRooms;

    public CSRoomsHandler() {
        this.chatRooms = new ArrayList<>();
    }

    @Override
    public void handle(Message message) {
        JsonArray roomsArray = new JsonArray();
        for (ChatRoom room : chatRooms) {
            JsonObject roomObject = new JsonObject();
            roomObject.addProperty("roomId", room.getId());
            roomObject.addProperty("title", room.getTitle());

            JsonArray membersArray = new JsonArray();
            for (Client member : room.getMembers()) {
                membersArray.add(member.getName());
            }

            roomObject.add("members", membersArray);
            roomsArray.add(roomObject);
        }
    }

}
