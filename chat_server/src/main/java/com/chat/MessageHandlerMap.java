package com.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MessageHandlerMap {

    private final Map<String, Consumer<Message>> handlers;

    public MessageHandlerMap() {
        this.handlers = new HashMap<>();
    }

    public void registerHandler(String messageType, Consumer<Message> handler) {
        handlers.put(messageType, handler);
    }

    public void handleMessage(Message message) {
        Consumer<Message> handler = handlers.get(message.getType());
        if (handler != null) {
            handler.accept(message);
        } else {
            System.out.println("No handler for message type " + message.getType());
        }
    }

}
