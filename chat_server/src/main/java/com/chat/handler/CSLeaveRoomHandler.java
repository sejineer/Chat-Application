package com.chat.handler;

import com.chat.client.ChatRoom;
import com.chat.client.ChatRoomHandler;
import com.chat.client.Client;
import com.chat.message.JsonMessageFormatter;
import com.chat.message.Message;
import com.chat.message.MessageSender;
import com.google.gson.JsonObject;

public class CSLeaveRoomHandler implements MessageHandler {

    private MessageSender messageSender;
    private ChatRoomHandler chatRoomHandler;

    public CSLeaveRoomHandler(ChatRoomHandler chatRoomHandler) {
        this.messageSender = new MessageSender(new JsonMessageFormatter());
        this.chatRoomHandler = chatRoomHandler;
    }

    @Override
    public void handle(Message message) {
        Client client = message.getClient();

        if (client.getCurrentRoom() != null) {
            ChatRoom chatRoom = client.getCurrentRoom();
            client.setCurrentRoom(null);

            createResponseMessage(client, "방제[" + chatRoom.getTitle() + "] 대화 방에서 퇴장했습니다.");
            for (Client roomClient : chatRoom.getMembers()) {
                createResponseMessage(roomClient, "[" + client.getName() + "] 님이 퇴장했습니다.");
            }
        } else {
            createResponseMessage(client, "현재 대화방에 들어가 있지 않습니다.");
        }
    }

    private void createResponseMessage(Client client, String text) {
        JsonObject responseMessage = new JsonObject();
        responseMessage.addProperty("type", "SCSystemMessage");
        responseMessage.addProperty("text", text);
        messageSender.sendMessage(client.getChannel(), responseMessage);
    }

}
