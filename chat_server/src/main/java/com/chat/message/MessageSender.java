package com.chat.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class MessageSender {
    private final MessageFormatter formatter;

    public MessageSender(MessageFormatter formatter) {
        this.formatter = formatter;
    }

    public void sendMessage(SocketChannel channel, Object message) {
        try {
            byte[] messageBytes = formatter.formatMessage(message);
            ByteBuffer lengthBuffer = ByteBuffer.allocate(2);
            lengthBuffer.putShort((short) messageBytes.length);
            lengthBuffer.flip();

            ByteBuffer buffer = ByteBuffer.allocate(lengthBuffer.remaining() + messageBytes.length);
            buffer.put(lengthBuffer);
            buffer.put(messageBytes);
            buffer.flip();
            System.out.println("sendMessage method befor");

            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
            System.out.println("sendMessage method after");
        } catch (IOException e) {
            System.err.println("메시지 전송 중 오류 발생: " + e.getMessage());
        }
    }

}