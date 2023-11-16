package com.chat;

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
    private Selector selector;
    private BlockingQueue<Message> messageQueue;

    public ClientHandler(SocketChannel channel, Selector selector, BlockingQueue<Message> messageQueue) {
        this.channel = channel;
        this.selector = selector;
        this.messageQueue = messageQueue;
        this.client = new Client(channel);
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
        try {
            ByteBuffer buffer = ByteBuffer.allocate(256);
            int read = channel.read(buffer);
            if (read == -1) {
                channel.close();
                key.cancel();
                return;
            }

            String received = new String(buffer.array(), 0, buffer.position(), StandardCharsets.UTF_8);
            buffer.clear();

            JsonObject json = JsonParser.parseString(received).getAsJsonObject();
            String type = json.get("type").getAsString();
            Message message = new Message(type, json, channel);
            messageQueue.put(message);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("메시지 읽기 오류 발생", e);
        }
    }

    public Client getClient() {
        return client;
    }

}
