package com.chat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final BlockingQueue<Message> messageQueue;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, BlockingQueue<Message> queue) {
        this.clientSocket = socket;
        this.messageQueue = queue;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                try {
                    JSONObject json = new JSONObject(inputLine);
                } catch (JSONException e) {
                    System.err.println("JSON 파싱 오류: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("클라이언트 핸들러 오류: " + e.getMessage());
        } finally {
            close();
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("자원 정리 중 오류 발생: " + e.getMessage());
        }
    }

}
