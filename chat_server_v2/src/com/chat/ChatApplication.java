package com.chat;

public class ChatApplication {

    public static void main(String[] args) {
        Server server = new Server(9113, 3);
        server.run();
    }

}
