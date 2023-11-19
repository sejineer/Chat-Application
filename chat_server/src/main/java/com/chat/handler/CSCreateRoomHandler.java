package com.chat.handler;

import com.chat.client.ChatRoom;
import com.chat.client.ChatRoomHandler;
import com.chat.client.Client;
import com.chat.message.JsonMessageFormatter;
import com.chat.message.Message;
import com.chat.message.MessageSender;
import com.google.gson.JsonObject;

public class CSCreateRoomHandler implements MessageHandler {

    private ChatRoomHandler chatRoomHandler;
    private MessageSender messageSender;

    public CSCreateRoomHandler(ChatRoomHandler chatRoomHandler) {
        this.chatRoomHandler = chatRoomHandler;
        this.messageSender = new MessageSender(new JsonMessageFormatter());
    }

    @Override
    public void handle(Message message) {
        Client client = message.getClient();
        JsonObject response = new JsonObject();

        if (client.getCurrentRoom() != null) {
            response.addProperty("type", "SCSystemMessage");
            response.addProperty("text", "대화방에 있을 때는 방을 개설 할 수 없습니다.");
        } else {
            String title = message.getJsonData().get("title").getAsString();
            ChatRoom newRoom = chatRoomHandler.createRoom(title);
            newRoom.addClient(client);
            client.setCurrentRoom(newRoom);

            response.addProperty("type", "SCSystemMessage");
            response.addProperty("text", "방제[" + title + "] 방에 입장했습니다.");
        }
        System.out.println(response);
        messageSender.sendMessage(client.getChannel(), response);
    }

}
