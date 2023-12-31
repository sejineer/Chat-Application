package com.chat.handler;

import com.chat.client.ChatRoom;
import com.chat.client.ChatRoomHandler;
import com.chat.client.Client;
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
        Long roomId = jsonData.get("roomId").getAsLong();

        Client client = message.getClient();
        ChatRoom chatRoom = chatRoomHandler.getRoom(roomId);

        if (chatRoom == null) {
            createResponseMessage(client, "대화 방이 존재하지 않습니다.");
        } else if (client.getCurrentRoom() != null) {
            createResponseMessage(client, "대화 방에 있을 때는 다른 방에 들어갈 수 없습니다.");
        } else {
            chatRoom.addClient(client);
            client.setCurrentRoom(chatRoom);
            createResponseMessage(client, "방제[" + chatRoom.getTitle() + "] 방에 입장했습니다.");

            for (Client roomClient : chatRoom.getMembers()) {
                createResponseMessage(roomClient, "[" + client.getName() + "] 님이 입장했습니다.");
            }
        }
    }

    private void createResponseMessage(Client client, String text) {
        JsonObject responseMessage = new JsonObject();
        responseMessage.addProperty("type", "SCSystemMessage");
        responseMessage.addProperty("text", text);
        messageSender.sendMessage(client.getChannel(), responseMessage);
    }

}
