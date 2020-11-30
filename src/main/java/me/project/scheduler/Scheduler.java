package me.project.scheduler;

import me.project.Request;

import java.util.Queue;

public abstract class Scheduler {

    Queue<Request> queue;

    public abstract Request poll();

    public abstract void push(Request request);

    public abstract boolean empty();

}
