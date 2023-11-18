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

        Client client = message.getClient();
        client.setName(newName);

        sendMessageToClient(client.getChannel(), "[시스템 메시지] 이름이 " + newName + "으로 변경되었습니다.");

        System.out.println("[시스템 메시지] 이름이 " + newName + "으로 변경되었습니다.");

        if (client.getCurrentRoom() != null) {
            ChatRoom room = client.getCurrentRoom();
            for (Client roomClient : room.getMembers()) {
                sendMessageToClient(roomClient.getChannel(), "[시스템 메시지] " + client.getName() + "의 이름이 " + newName + "으로 변경되었습니다.");
            }
        }
    }

    private void sendMessageToClient(SocketChannel channel, String message) {
        try {
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

            ByteBuffer lengthBuffer = ByteBuffer.allocate(2);
            lengthBuffer.putShort((short) messageBytes.length);
            lengthBuffer.flip();

            ByteBuffer buffer = ByteBuffer.allocate(2 + messageBytes.length);
            buffer.put(lengthBuffer);
            buffer.put(messageBytes);
            buffer.flip();

            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        } catch (IOException e) {
            System.err.println("메시지 전송 중 오류 발생: " + e.getMessage());
        }
    }

}
