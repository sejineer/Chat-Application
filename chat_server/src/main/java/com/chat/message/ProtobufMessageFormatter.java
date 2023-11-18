package com.chat.message;

import java.io.IOException;

public class ProtobufMessageFormatter implements MessageFormatter {
    @Override
    public byte[] formatMessage(Object message) throws IOException {
        if (!(message instanceof com.google.protobuf.Message)) {
            throw new IllegalArgumentException("메시지는 Protobuf 형식이어야 합니다");
        }
        return ((com.google.protobuf.Message) message).toByteArray();
    }
}
