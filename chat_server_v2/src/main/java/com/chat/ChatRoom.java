package com.chat;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {

    private String title;
    private List<Client> members;

    public ChatRoom(String title) {
        this.title = title;
        this.members = new ArrayList<>();
    }

    public void addClient(Client client) {
        members.add(client);
        client.setCurrentRoom(this);
    }

    public void removeClient(Client client) {
        members.remove(client);
        client.setCurrentRoom(null);
    }

    public void broadcastMessage(Message message) {
        for (Client client : members) {
//            client.sendMessage(message.getJsonData());
        }
    }

}
