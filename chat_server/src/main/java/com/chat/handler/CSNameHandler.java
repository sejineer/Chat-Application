package com.chat.handler;

import com.chat.ChatRoom;
import com.chat.Client;
import com.chat.Message;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class CSNameHandler implements MessageHandler {

    private static final Logger LOGGER = Logger.getLogger(CSNameHandler.class.getName());

    @Override
    public void handle(Message message) {
        JsonObject jsonData = message.getJsonData();
        String newName = jsonData.get("name").getAsString();
        SocketChannel clientChannel = message.getClientChannel();

        Client client = message.getClient();
        if (client == null) {
            LOGGER.warning("Client not found for channel: " + message.getClientChannel());
            return;
        }

        String oldName = client.getName();
        client.setName(newName);

        sendMessageToClient(clientChannel, "[시스템 메시지] 이름이 " + newName + "으로 변경되었습니다.");

        if (client.getCurrentRoom() != null) {
            ChatRoom room = client.getCurrentRoom();
            for (Client roomClient : room.getMembers()) {
                sendMessageToClient(roomClient.getChannel(), "[시스템 메시지] " + oldName + "의 이름이" + newName + "으로 변경되었습니다.");
            }
        }
    }

    private void sendMessageToClient(SocketChannel clientChannel, String message) {
        try {
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

            ByteBuffer buffer = ByteBuffer.wrap(messageBytes);
            buffer.rewind();

            while (buffer.hasRemaining()) {
                clientChannel.write(buffer);
            }
        } catch (IOException e) {
            System.err.println("메세지 전송 중 오류 발생: " + e.getMessage());
        }
    }

}
