package com.github.kingbbode.chatbot.core.event;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by YG on 2016-11-03.
 */
public class EventQueue {
    private Queue<Event> queue = new ConcurrentLinkedQueue<>();

    public boolean hasNext(){
        return queue.size()>0;
    }

    public void offer(Event e) {
        if (e == null) {
            return;
        }
        queue.offer(e);
    }

    Event poll() {
        return queue.poll();
    }
}
