package com.chat.client;

import com.chat.client.ChatRoom;

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

    public String getName() {
        return name;
    }

    public ChatRoom getCurrentRoom() {
        return currentRoom;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentRoom(ChatRoom room) {
        this.currentRoom = room;
    }


}
