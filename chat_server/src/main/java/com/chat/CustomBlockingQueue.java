package com.chat;

import java.util.LinkedList;
import java.util.Queue;

public class CustomBlockingQueue<T> {

    private Queue<T> queue;
    private int capacity;
    private Object lock = new Object();

    public CustomBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    public void put(T item) throws InterruptedException {
        synchronized (lock) {
            while (queue.size() == capacity) {
                lock.wait();
            }
            queue.add(item);
            lock.notifyAll();
        }
    }

    public T take() throws InterruptedException {
        synchronized (lock) {
            while (queue.isEmpty()) {
                lock.wait();
            }
            T item = queue.remove();
            lock.notifyAll();
            return item;
        }
    }

    public void clear() {
        synchronized (lock) {
            queue.clear();
            lock.notifyAll();
        }
    }

}
