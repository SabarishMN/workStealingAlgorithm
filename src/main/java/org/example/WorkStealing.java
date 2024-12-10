package org.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;

public class WorkStealing {
    private List<Deque<Task>> taskQueues; // One deque per thread
    private int numThreads;

    public WorkStealing(int numThreads) {
        this.numThreads = numThreads;
        taskQueues = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            taskQueues.add(new ArrayDeque<>());
        }
    }

    public void execute(Task rootTask) {
        // Distribute initial tasks across all threads
        taskQueues.get(0).add(rootTask); // Root task for Thread 0
        for (int i = 1; i < numThreads; i++) {
            if (!rootTask.dependencies.isEmpty()) {
                taskQueues.get(i).add(rootTask.dependencies.get(i % rootTask.dependencies.size()));
            }
        }

        // Start threads
        List<Thread> workers = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            int threadId = i;
            Thread worker = new Thread(() -> runWorker(threadId));
            workers.add(worker);
            worker.start();
        }

        // Wait for all threads to complete
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void runWorker(int threadId) {
        Deque<Task> queue = taskQueues.get(threadId);
        int idleTime = 1; // Start with a small idle time

        while (true) {
            Task task = queue.pollFirst();

            if (task == null) {
                task = stealTask(threadId);
                if (task == null) {
                    try {
                        Thread.sleep(idleTime); // Sleep for the current idle time
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    idleTime = Math.min(idleTime * 2, 100); // Exponential backoff up to a limit
                    continue;
                }
            }

            idleTime = 1; // Reset idle time if work is found
            if (task != null) {
                processTask(task, threadId);
            }
        }
    }

    private Task stealTask(int threadId) {
        for (int i = 0; i < numThreads; i++) {
            if (i != threadId) {
                Deque<Task> victimQueue = taskQueues.get(i);
                if (!victimQueue.isEmpty()) { // First check without locking
                    synchronized (victimQueue) {
                        if (!victimQueue.isEmpty()) { // Double-check inside the lock
                            Task stolen = victimQueue.pollLast();
                            if (stolen != null) {
                                System.out.println("Thread " + threadId + " stole Task " + stolen.id + " from Thread " + i);
                                return stolen;
                            }
                        }
                    }
                }
            }
        }
        return null; // No tasks available to steal
    }

    private void processTask(Task task, int threadId) {
        if (task.end - task.start > 1000) { // Split threshold
            int mid = (task.start + task.end) / 2;
            Task left = new Task(task.id * 2, task.array, task.start, mid, new ArrayList<>());
            Task right = new Task(task.id * 2 + 1, task.array,mid + 1, task.end, new ArrayList<>());
            taskQueues.get(threadId).addFirst(left);
            taskQueues.get(threadId).addFirst(right);
        } else {
            merge(task.array, task.start, (task.start + task.end) / 2, task.end);
            task.isCompleted = true;
        }
    }


    private void merge(int[] array, int start, int mid, int end) {
        int[] temp = new int[end - start + 1];
        int i = start, j = mid + 1, k = 0;

        while (i <= mid && j <= end) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = array[i++];
        }

        while (j <= end) {
            temp[k++] = array[j++];
        }

        System.arraycopy(temp, 0, array, start, temp.length);
    }
}
