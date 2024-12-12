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
        int idleChecks = 0; // Counter to track idle attempts

        while (true) {
            Task task = queue.pollFirst();

            if (task == null) {
                task = stealTask(threadId); // Attempt to steal tasks
                idleChecks++;
            }

            if (task == null) {
                if (idleChecks > 10) {
                    break; // Exit if no tasks are left to process or steal
                }
                continue; // Keep checking
            }

            idleChecks = 0; // Reset idle checks if a task is found

            // Skip already completed tasks
            if (task.isCompleted) {
                continue;
            }

            //System.out.println("Thread " + threadId + " is processing Task " + task.id);
            processTask(task, threadId);
        }
    }

    private Task stealTask(int threadId) {
        for (int i = 0; i < numThreads; i++) {
            if (i != threadId) {
                Deque<Task> victimQueue = taskQueues.get(i);
                synchronized (victimQueue) { // Synchronize to avoid race conditions
                    if (!victimQueue.isEmpty()) {
                        Task stolen = victimQueue.pollLast(); // Steal the last task
                        if (stolen != null) {
                            //System.out.println("Thread " + threadId + " stole Task " + stolen.id + " from Thread " + i);
                            return stolen;
                        }
                    }
                }
            }
        }
        return null; // No tasks available to steal
    }

    private void processTask(Task task, int threadId) {
        // Check if all dependencies are completed
        for (Task dependency : task.dependencies) {
            if (!dependency.isCompleted) {
                //System.out.println("Task " + task.id + " re-queued due to incomplete dependency " + dependency.id);
                // Re-queue the dependency task first
                taskQueues.get(threadId).add(dependency);
                taskQueues.get(threadId).add(task); // Re-add this task after the dependency
                return; // Exit to avoid processing an incomplete task
            }
        }

        // Perform merging if the task represents a range
        if (task.start < task.end) {
            int mid = (task.start + task.end) / 2;
            merge(task.array, task.start, mid, task.end);
        }

        // Mark task as completed
        task.isCompleted = true;

        //System.out.println("Thread " + threadId + " processed Task " + task.id);
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