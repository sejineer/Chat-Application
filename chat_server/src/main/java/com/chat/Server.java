package com.chat;

import com.chat.client.ChatRoomHandler;
import com.chat.client.Client;
import com.chat.client.ClientHandler;
import com.chat.handler.*;
import com.chat.message.Message;
import com.chat.message.MessageHandlerMap;
import com.chat.message.MessageProcessor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.*;

public class Server implements Runnable {

    private Selector selector;
    private ServerSocketChannel serverChannel;
    private ExecutorService threadPool;
    private CustomBlockingQueue<Message> messageQueue;
    private MessageHandlerMap handlerMap;
    private ChatRoomHandler chatRoomHandler;

    public Server(int port, int threadPoolSize) {
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            threadPool = Executors.newFixedThreadPool(threadPoolSize);
            messageQueue = new CustomBlockingQueue<>(100);
            handlerMap = new MessageHandlerMap();
            chatRoomHandler = new ChatRoomHandler();
            registerMessageHandlers();
            startMessageProcessor();
        } catch (IOException e) {
            throw new RuntimeException("서버 초기화 중 오류 발생", e);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if (key.isAcceptable()) {
                        registerClient();
                    } else if (key.isReadable()) {
                        readMessage(key);
                    }
                    iter.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("서버 실행 중 오류 발생", e);
        }
    }

    public void closeAllClientConnections() {
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys) {
            try {
                if (key.isValid() && key.channel() instanceof SocketChannel) {
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    clientChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Selector getSelector() {
        return selector;
    }

    public ServerSocketChannel getServerChannel() {
        return serverChannel;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public CustomBlockingQueue<Message> getMessageQueue() {
        return messageQueue;
    }

    private void registerClient() throws IOException {
        // ServerSocketChannel을 통해 SocketChannel 얻기
        SocketChannel clientChannel = serverChannel.accept();
        if (clientChannel != null) {
            clientChannel.configureBlocking(false);
            SelectionKey key = clientChannel.register(selector, SelectionKey.OP_READ);

            InetSocketAddress remoteAddress = (InetSocketAddress) clientChannel.getRemoteAddress();
            String initialName = remoteAddress.getAddress().getHostAddress() + ":" + remoteAddress.getPort();

            Client client = new Client(clientChannel);
            client.setName(initialName);

            // ClientHandler 생성 및 Selector의 Key에 첨부
            ClientHandler clientHandler = new ClientHandler(clientChannel, messageQueue, client);
            key.attach(clientHandler);
        }
    }

    private void startMessageProcessor() {
        MessageProcessor processor = new MessageProcessor(messageQueue, handlerMap);
        threadPool.submit(processor);
    }

    private void readMessage(SelectionKey key) {
        ClientHandler clientHandler = (ClientHandler) key.attachment();
        clientHandler.readMessage(key);
    }

    private void registerMessageHandlers() {
        handlerMap.registerHandler("CSName", new CSNameHandler());
        handlerMap.registerHandler("CSRooms", new CSRoomsHandler(chatRoomHandler));
        handlerMap.registerHandler("CSCreateRoom", new CSCreateRoomHandler(chatRoomHandler));
        handlerMap.registerHandler("CSJoinRoom", new CSJoinRoomHandler(chatRoomHandler));
        handlerMap.registerHandler("CSLeaveRoom", new CSLeaveRoomHandler());
        handlerMap.registerHandler("CSChat", new CSChatHandler());
        handlerMap.registerHandler("CSShutdown", new CSShutDownHandler(this));
    }

}