package com.chat;

import com.chat.handler.MessageHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MessageHandlerMap {

    private final Map<String, MessageHandler> handlers;

    public MessageHandlerMap() {
        this.handlers = new HashMap<>();
    }

    public void registerHandler(String messageType, MessageHandler handler) {
        handlers.put(messageType, handler);
    }

    public void handleMessage(Message message) {
        MessageHandler handler = handlers.get(message.getType());
        if (handler != null) {
            handler.handle(message);
        } else {
            System.out.println("No handler for message type " + message.getType());
        }
    }

}
