package com.chat;

import com.chat.handler.*;
import org.json.JSONObject;

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
    private BlockingQueue<Message> messageQueue;
    private MessageHandlerMap handlerMap;

    public Server(int port, int threadPoolSize) {
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            threadPool = Executors.newFixedThreadPool(threadPoolSize);
            messageQueue = new LinkedBlockingQueue<>();
            handlerMap = new MessageHandlerMap();
            registerMessageHandlers();
            startMessageProcessor();
        } catch (IOException e) {
            throw new RuntimeException("서버 초기화 중 오류 발생", e);
        }
    }

    private void startMessageProcessor() {
        MessageProcessor processor = new MessageProcessor(messageQueue, handlerMap);
        threadPool.submit(processor);
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

    private void registerClient() throws IOException {
        // ServerSocketChannel을 통해 SocketChannel 얻기
        SocketChannel clientChannel = serverChannel.accept();
        if (clientChannel != null) {
            clientChannel.configureBlocking(false);
            SelectionKey key = clientChannel.register(selector, SelectionKey.OP_READ);

            // ClientHandler 생성 및 Selector의 Key에 첨부
            ClientHandler clientHandler = new ClientHandler(clientChannel, selector, messageQueue);
            key.attach(clientHandler);
        }
    }

    private void readMessage(SelectionKey key) {
        ClientHandler clientHandler = (ClientHandler) key.attachment();
        clientHandler.readMessage(key);
    }

    private void registerMessageHandlers() {
        handlerMap.registerHandler("CSName", new CSNameHandler());
        handlerMap.registerHandler("CSRooms", new CSRoomsHandler());
        handlerMap.registerHandler("CSCreateRoom", new CSCreateRoomHandler());
        handlerMap.registerHandler("CSJoinRoom", new CSJoinRoomHandler());
        handlerMap.registerHandler("CSLeaveRoom", new CSLeaveRoomHandler());
        handlerMap.registerHandler("CSChat", new CSChatHandler());
        handlerMap.registerHandler("CSShutdown", new CSShutDownHandler());
    }

    private void handleCSName(Message message) {
        JSONObject jsonData = message.getJsonData();
        String name = jsonData.getString("name");
    }

    private void processMessage(Message message) {
        handlerMap.handleMessage(message);
    }

}
