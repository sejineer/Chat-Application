package com.chat.handler;

import com.chat.Server;
import com.chat.message.Message;

public class CSShutDownHandler implements MessageHandler {

    private Server server;

    public CSShutDownHandler(Server server) {
        this.server = server;
    }

    @Override
    public void handle(Message message) {
    }

}
