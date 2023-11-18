package com.chat.handler;

import com.chat.message.Message;

public interface MessageHandler {

    void handle(Message message);

}
