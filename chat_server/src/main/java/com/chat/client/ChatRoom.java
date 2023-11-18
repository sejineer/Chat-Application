package com.chat.client;

import com.chat.message.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {

    private Long id;
    private String title;
    private List<Client> members;

    public ChatRoom(Long id, String title) {
        this.id = id;
        this.title = title;
        this.members = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Client> getMembers() {
        return members;
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
