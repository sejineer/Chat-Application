package com.chat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public class ClientHandler implements Runnable {

    private SocketChannel channel;
    private Selector selector;
    private BlockingQueue<Message> messageQueue;

    public ClientHandler(SocketChannel channel, Selector selector, BlockingQueue<Message> messageQueue) {
        this.channel = channel;
        this.selector = selector;
        this.messageQueue = messageQueue;
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

            // 메시지 파싱 로직
            // 예시: "type|sender|message" 형식의 메시지를 파싱한다고 가정
            String[] parts = received.split("\\|", 3);
            if (parts.length == 3) {
                Message message = new Message(parts[0], parts[1], parts[2]);
                messageQueue.put(message);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("메시지 읽기 오류 발생", e);
        }
    }

}
