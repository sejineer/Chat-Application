package com.chat;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MessageProcessor implements Runnable {

    private final BlockingQueue<Message> messageQueue;
    private final ReentrantLock lock;
    private final Condition notEmpty;

    public MessageProcessor(BlockingQueue<Message> queue) {
        this.messageQueue = queue;
        this.lock = new ReentrantLock();
        this.notEmpty = lock.newCondition();
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            try {
                while (messageQueue.isEmpty()) {
                    notEmpty.await();
                }
                Message message = messageQueue.take();
                System.out.println(message.getSender() + ": " + message.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    public void newMessage() {
        lock.lock();
        try {
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

}
