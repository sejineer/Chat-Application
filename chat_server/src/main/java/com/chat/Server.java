package com.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Server implements Runnable {

    private ServerSocket serverSocket;
    private List<ClientHandler> clientHandlers;
    private ExecutorService threadPool;
    private BlockingQueue<Message> messageQueue;

    public Server(int port, int threadPoolSize) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("서버 소켓을 초기화하는데 실패했습니다.", e);
        }

        clientHandlers = new ArrayList<>();
        threadPool = Executors.newFixedThreadPool(threadPoolSize);
        messageQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        System.out.println("서버가 포트 " + serverSocket.getLocalPort() + "에서 시작되었습니다.");

        try {
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("클라이언트가 연결되었습니다: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, messageQueue);
                clientHandlers.add(clientHandler);
                threadPool.execute(clientHandler);
            }
        } catch (IOException e) {
            if (!serverSocket.isClosed()) {
                System.err.println("클라이언트 연결 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            stop();
        }
    }

    public void stop() {
        try {
            for (ClientHandler handler : clientHandlers) {
                handler.close();
            }
            serverSocket.close();
            threadPool.shutdown();
        } catch (IOException e) {
            System.err.println("서버를 정리하는 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processMessage() {

    }

}
