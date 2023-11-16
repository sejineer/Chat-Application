package com.chat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client {

    private String name;
    private SocketChannel channel;
    private ChatRoom currentRoom;

    public Client(SocketChannel channel) {
        this.channel = channel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentRoom(ChatRoom room) {
        this.currentRoom = room;
    }

    public void sendMessage(String message) {
        if (channel != null && channel.isConnected()) {
            try {
                // 문자열을 바이트 배열로 변환
                byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
                // ByteBuffer에 바이트 배열을 담는다.
                ByteBuffer buffer = ByteBuffer.wrap(messageBytes);
                // ByteBuffer의 내용을 채널을 통해 전송한다.
                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
            } catch (IOException e) {
                System.err.println("메시지 전송 중 오류 발생: " + e.getMessage());
            }
        }
    }

}
