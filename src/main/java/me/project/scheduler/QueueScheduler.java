package me.project.scheduler;

import me.project.Request;

import java.util.LinkedList;

public class QueueScheduler extends Scheduler {

    public QueueScheduler() {
        this.queue = new LinkedList<>();
    }

    @Override
    public Request poll() {
        return this.queue.poll();
    }

    @Override
    public void push(Request request) {
        this.queue.add(request);
    }

    @Override
    public boolean empty() {
        return this.queue.peek() == null;
    }
}
