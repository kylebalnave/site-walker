package com.balnave.rambler.queue;

import java.util.ArrayList;
import java.util.List;

public class Queue {
    
    private final List<QueueItem> queue = new ArrayList<QueueItem>();
        
    public void add(String url) {
        add(url, url);
    }
    
    public void add(String parentUrl, String url) {
        QueueItem item = new QueueItem(parentUrl, url);
        if(!queue.contains(item)) {
            queue.add(item);
        }
    }
    
    public QueueItem removeFirst() {
        if(queue.isEmpty()) {
            return null;
        }
        return queue.remove(0);
    }
    
    public boolean contains(String url) {
        QueueItem item = new QueueItem(url, url);
        return queue.contains(item);
    }
    
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    public int size() {
        return queue.size();
    }
    
    public void clear() {
        queue.clear();
    }
    
}
