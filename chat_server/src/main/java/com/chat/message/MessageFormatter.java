package com.chat.message;

import java.io.IOException;

public interface MessageFormatter {
    byte[] formatMessage(Object message) throws IOException;
}
