package com.chat.handler;

import com.chat.client.ChatRoom;
import com.chat.client.ChatRoomHandler;
import com.chat.client.Client;
import com.chat.message.JsonMessageFormatter;
import com.chat.message.Message;
import com.chat.message.MessageSender;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class CSRoomsHandler implements MessageHandler {

    private MessageSender messageSender;
    private ChatRoomHandler chatRoomHandler;

    public CSRoomsHandler(ChatRoomHandler chatRoomHandler) {
        this.messageSender = new MessageSender(new JsonMessageFormatter());
        this.chatRoomHandler = chatRoomHandler;
    }

    @Override
    public void handle(Message message) {
        List<ChatRoom> chatRooms = (List<ChatRoom>) chatRoomHandler.getAllRooms();
        JsonObject response = new JsonObject();
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
        response.add("rooms", roomsArray);

        messageSender.sendMessage(message.getClient().getChannel(), response);
    }

}
