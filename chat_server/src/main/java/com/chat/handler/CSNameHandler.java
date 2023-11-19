package com.chat.handler;

import com.chat.client.ChatRoom;
import com.chat.client.Client;
import com.chat.message.JsonMessageFormatter;
import com.chat.message.Message;
import com.chat.message.MessageSender;
import com.google.gson.JsonObject;

public class CSNameHandler implements MessageHandler {

    private MessageSender messageSender;

    public CSNameHandler() {
        this.messageSender = new MessageSender(new JsonMessageFormatter());
    }

    @Override
    public void handle(Message message) {
        JsonObject jsonData = message.getJsonData();
        String newName = jsonData.get("name").getAsString();

        Client client = message.getClient();
        client.setName(newName);

        JsonObject responseMessage = createResponseMessage(newName);
        System.out.println(responseMessage);
        messageSender.sendMessage(client.getChannel(), responseMessage);

        if (client.getCurrentRoom() != null) {
            ChatRoom room = client.getCurrentRoom();
            for (Client roomClient : room.getMembers()) {
                JsonObject roomResponseMessage = createResponseMessage(newName);
                messageSender.sendMessage(roomClient.getChannel(), roomResponseMessage);
            }
        }
    }

    private JsonObject createResponseMessage(String newName) {
        JsonObject responseMessage = new JsonObject();
        responseMessage.addProperty("type", "SCSystemMessage");
        responseMessage.addProperty("text", "이름이 " + newName + "으로 변경되었습니다.");
        return responseMessage;
    }

}