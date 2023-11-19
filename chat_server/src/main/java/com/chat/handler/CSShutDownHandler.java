package com.chat.handler;

import com.chat.Server;
import com.chat.message.Message;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CSShutDownHandler implements MessageHandler {

    private Server server;

    public CSShutDownHandler(Server server) {
        this.server = server;
    }

    @Override
    public void handle(Message message) {
        try {
            server.closeAllClientConnections();

            server.getThreadPool().shutdown();
            server.getThreadPool().awaitTermination(60, TimeUnit.SECONDS);

            server.getSelector().close();
            server.getServerChannel().close();

            server.getMessageQueue().clear();

            System.out.println("서버가 종료되었습니다.");
            System.exit(0);
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

}
