package com.chat.message;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonMessageFormatter implements MessageFormatter {

    @Override
    public byte[] formatMessage(Object message) throws IOException {
        if (!(message instanceof JsonObject)) {
            throw new IllegalArgumentException("메시지는 JSON 형식이어야 합니다.");
        }
        return ((JsonObject) message).toString().getBytes(StandardCharsets.UTF_8);
    }
    
}
