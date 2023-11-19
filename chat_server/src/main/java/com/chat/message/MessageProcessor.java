package com.chat.message;


import com.chat.CustomBlockingQueue;
import com.chat.message.Message;
import com.chat.message.MessageHandlerMap;

import java.util.concurrent.BlockingQueue;

public class MessageProcessor implements Runnable {

    private final CustomBlockingQueue<Message> messageQueue;
    private final MessageHandlerMap handlerMap;

    public MessageProcessor(CustomBlockingQueue<Message> messageQueue, MessageHandlerMap handlerMap) {
        this.messageQueue = messageQueue;
        this.handlerMap = handlerMap;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message message = messageQueue.take();
                handlerMap.handleMessage(message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
