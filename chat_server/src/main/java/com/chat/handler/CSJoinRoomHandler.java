package com.chat.handler;

import com.chat.client.ChatRoom;
import com.chat.client.ChatRoomHandler;
import com.chat.client.Client;
import com.chat.client.ClientHandler;
import com.chat.message.JsonMessageFormatter;
import com.chat.message.Message;
import com.chat.message.MessageSender;
import com.google.gson.JsonObject;

public class CSJoinRoomHandler implements MessageHandler {

    private MessageSender messageSender;
    private ChatRoomHandler chatRoomHandler;

    public CSJoinRoomHandler(ChatRoomHandler chatRoomHandler) {
        this.messageSender = new MessageSender(new JsonMessageFormatter());
        this.chatRoomHandler = chatRoomHandler;
    }

    @Override
    public void handle(Message message) {
        JsonObject jsonData = message.getJsonData();
        String roomId = jsonData.get("roomId").getAsString();

        Client client = message.getClient();
        ChatRoom chatRoom = chatRoomHandler.getRoom(Long.valueOf(roomId));

        JsonObject response = new JsonObject();
        if (client.getCurrentRoom() != null) {
            response.addProperty("type", "SCSystemMessage");
            response.addProperty("text", "대화 방에 있을 때는 다른 방에 들어갈 수 없습니다.");
        } else {
            chatRoom.addClient(client);
            client.setCurrentRoom(chatRoom);

            response.addProperty("type", "SCSystemMessage");
            response.addProperty("text", "방제[" + chatRoom.getTitle() + "] 방에 입장했습니다.");

            messageSender.sendMessage(client.getChannel(), response);

            for (Client roomClient : chatRoom.getMembers()) {
                JsonObject roomResponseMessage = new JsonObject();
                roomResponseMessage.addProperty("type", "SCSystemMessage");
                roomResponseMessage.addProperty("text", "[" + client.getName() + "] 님이 입장했습니다.");
                messageSender.sendMessage(roomClient.getChannel(), roomResponseMessage);
            }
        }
    }

}
