package org.example;

import java.util.concurrent.atomic.AtomicInteger;

public class WorkStealingQueue {
    private final Task[] tasks;
    private final AtomicInteger top;
    private final AtomicInteger bottom;

    public WorkStealingQueue(int capacity) {
        tasks = new Task[capacity];
        top = new AtomicInteger(0);
        bottom = new AtomicInteger(0);
    }

    public boolean push(Task task) {
        int b = bottom.get();
        int t = top.get();
        if (b - t >= tasks.length) {
            return false;
        }
        tasks[b % tasks.length] = task;
        bottom.incrementAndGet();
        return true;
    }

    public int size() {
        return top.get() - bottom.get();
    }

    public Task pop() {
        int b = bottom.decrementAndGet();
        int t = top.get();
        if (b < t) {
            bottom.incrementAndGet();
            return null;
        }
        Task task = tasks[b % tasks.length];
        tasks[b % tasks.length] = null;
        return task;
    }

    public Task steal() {
        int t = top.get();
        int b = bottom.get();
        if (t >= b) {
            return null;
        }
        Task task = tasks[t % tasks.length];
        if (top.compareAndSet(t, t + 1)) {
            return task;
        }
        return null;
    }
    public Task peek() {
        int t = top.get();
        int b = bottom.get();
        if (t >= b) {
            return null;
        }
        return tasks[t % tasks.length];
    }
}
