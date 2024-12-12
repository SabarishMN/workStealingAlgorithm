package org.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class DAGGenerator {
    // Generate a simple DAG
    public static List<Task> generateDAG(int numTasks, int maxDependencies) {
        Random random = new Random();
        List<Task> tasks = new ArrayList<>();

        // Create all tasks
        for (int i = 0; i < numTasks; i++) {
            tasks.add(new Task(i));
        }

        // Add dependencies (ensure no cycles)
        for (int i = 0; i < numTasks; i++) {
            Task task = tasks.get(i);
            int numDependencies = random.nextInt(Math.min(maxDependencies, i + 1));

            for (int j = 0; j < numDependencies; j++) {
                Task dependency = tasks.get(random.nextInt(i + 1)); // Only earlier tasks
                if (!task.dependencies.contains(dependency)) {
                    task.dependencies.add(dependency);
                }
            }
        }
        return tasks;
    }

    private static int taskIdCounter = 0;
    public static Task generateMergeSortDAG(int[] array, int start, int end) {
        int taskId = taskIdCounter++; // Generate a unique task ID
        Task task = new Task(taskId, array, start, end);

        if (start < end) {
            int mid = (start + end) / 2;

            // Generate left and right tasks recursively
            Task leftTask = generateMergeSortDAG(array, start, mid);
            Task rightTask = generateMergeSortDAG(array, mid + 1, end);

            // Add left and right tasks as dependencies
            task.dependencies.add(leftTask);
            task.dependencies.add(rightTask);

            //System.out.println("Task " + task.id + " depends on: " + leftTask.id + ", " + rightTask.id);
        }

        return task;
    }

    // Print the DAG
    public static void printDAG(List<Task> tasks) {
        System.out.println("Generated DAG:");
        for (Task task : tasks) {
            System.out.print("Task " + task.id + " depends on: ");
            for (Task dependency : task.dependencies) {
                System.out.print(dependency.id + " ");
            }
            System.out.println();
        }
    }
}