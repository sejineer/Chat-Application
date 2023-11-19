package com.chat.client;

import com.chat.client.Client;
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
import java.util.logging.Logger;

public class ClientHandler implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
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
            while (true) {
                int read = channel.read(buffer);
                if (read == -1) {
                    LOGGER.info("클라이언트 연결이 끊어졌습니다: " + channel);
                    channel.close();
                    break;
                }
                readMessage(buffer);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("클라이언트 통신 중 오류 발생", e);
        }
    }

    public void readMessage(ByteBuffer buffer) throws IOException, InterruptedException {
        buffer.flip();
        if (buffer.remaining() < 2) {
            buffer.compact();
            return;
        }

        int messageLength = buffer.getShort() & 0xffff;
        if (buffer.remaining() < messageLength) {
            buffer.compact();
            return;
        }

        ByteBuffer messageBuffer = ByteBuffer.allocate(messageLength);
        buffer.get(messageBuffer.array(), 0, messageLength);

        String received = StandardCharsets.UTF_8.decode(messageBuffer).toString();
        LOGGER.info("메시지 수신: " + received);
        JsonObject json = JsonParser.parseString(received).getAsJsonObject();
        String type = json.get("type").getAsString();
        Message message = new Message(type, json, channel, this.client);
        messageQueue.put(message);

        buffer.compact();
    }

    public Client getClient() {
        return client;
    }

}
