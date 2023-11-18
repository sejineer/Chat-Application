package com.chat.client;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ChatRoomHandler {

    private final ConcurrentHashMap<Long, ChatRoom> chatRooms;
    private final AtomicLong roomIdGenerator;

    public ChatRoomHandler() {
        this.chatRooms = new ConcurrentHashMap<>();
        this.roomIdGenerator = new AtomicLong(0);
    }

    public ChatRoom createRoom(String title) {
        long roomId = roomIdGenerator.incrementAndGet();
        ChatRoom newRoom = new ChatRoom(roomId, title);
        chatRooms.put(roomId, newRoom);
        return newRoom;
    }

    public ChatRoom getRoom(Long id) {
        return chatRooms.get(id);
    }

    public boolean deleteRoom(Long id) {
        return chatRooms.remove(id) != null;
    }

    public Collection<ChatRoom> getAllRooms() {
        return chatRooms.values();
    }

}
