package com.chat.client;

import com.chat.CustomBlockingQueue;
import com.chat.message.Message;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public class ClientHandler implements Runnable {

    private Client client;
    private SocketChannel channel;
    private CustomBlockingQueue<Message> messageQueue;

    public ClientHandler(SocketChannel channel, CustomBlockingQueue<Message> messageQueue, Client client) {
        this.channel = channel;
        this.messageQueue = messageQueue;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            int read;
            while (true) {
                read = channel.read(buffer);
                if (read == -1) {
                    channel.close();
                    return;
                }

            }
        } catch (IOException e) {
            throw new RuntimeException("클라이언트 통신 중 오류 발생", e);
        }
    }

    public void readMessage(SelectionKey key) {
        synchronized (channel) {
            try {
                ByteBuffer lengthBuffer = ByteBuffer.allocate(2);
                while (lengthBuffer.hasRemaining()) {
                    channel.read(lengthBuffer);
                }
                lengthBuffer.flip();
                int messageLength = lengthBuffer.getShort() & 0xffff; // ungisned short로 변환

                ByteBuffer messageBuffer = ByteBuffer.allocate(messageLength);
                while (messageBuffer.hasRemaining()) {
                    channel.read(messageBuffer);
                }
                messageBuffer.flip();

                String received = StandardCharsets.UTF_8.decode(messageBuffer).toString();
                JsonObject json = JsonParser.parseString(received).getAsJsonObject();
                String type = json.get("type").getAsString();
                Message message = new Message(type, json, this.client);
                messageQueue.put(message);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("메시지 읽기 오류 발생", e);
            }
        }
    }

    public Client getClient() {
        return client;
    }

}
