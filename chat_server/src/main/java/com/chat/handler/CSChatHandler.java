package com.chat.handler;

import com.chat.client.Client;
import com.chat.message.JsonMessageFormatter;
import com.chat.message.Message;
import com.chat.message.MessageSender;
import com.google.gson.JsonObject;

public class CSChatHandler implements MessageHandler {

    private MessageSender messageSender;

    public CSChatHandler(MessageSender messageSender) {
        this.messageSender = new MessageSender(new JsonMessageFormatter());
    }

    @Override
    public void handle(Message message) {
        JsonObject jsonData = message.getJsonData();
        String chat = jsonData.get("text").getAsString();

        Client client = message.getClient();
        if (client.getCurrentRoom() != null) {
            createChatResponseMessage(client, chat);
            for (Client roomMember : client.getCurrentRoom().getMembers()) {
                createChatResponseMessage(roomMember, chat);
            }
        } else {
            JsonObject responseMessage = new JsonObject();
            responseMessage.addProperty("type", "SCSystemMessage");
            responseMessage.addProperty("text", "현재 대화방에 들어가 있지 않습니다.");
            messageSender.sendMessage(client.getChannel(), responseMessage);
        }
    }

    private void createChatResponseMessage(Client client, String text) {
        JsonObject responseMessage = new JsonObject();
        responseMessage.addProperty("type", "SCChat");
        responseMessage.addProperty("member", client.getName());
        responseMessage.addProperty("text", text);
        messageSender.sendMessage(client.getChannel(), responseMessage);
    }

}
