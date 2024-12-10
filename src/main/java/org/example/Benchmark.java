package org.example;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Benchmark {

    // Generate a random array of integers
    public static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(50); // Random numbers between 0 and 999,999
        }
        return array;
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java Benchmark <algorithm> <numThreads> <arraySize>");
            System.out.println("<algorithm>: 'custom' or 'forkjoin'");
            System.out.println("<numThreads>: Number of threads to use");
            System.out.println("<arraySize>: Size of the array to sort");
            return;
        }

        // Parse arguments
        String algorithm = args[0]; // 'custom' or 'forkjoin'
        int numThreads = Integer.parseInt(args[1]); // Number of threads
        int arraySize = Integer.parseInt(args[2]); // Size of the array

        // Generate a random array
        int[] array = generateRandomArray(arraySize);
        System.out.println("Sorting array of size: " + arraySize + " using " + algorithm + " with " + numThreads + " threads.");

        // Benchmark based on the chosen algorithm
        if (algorithm.equalsIgnoreCase("custom")) {
            runCustomWorkStealing(array, numThreads);
        } else if (algorithm.equalsIgnoreCase("forkjoin")) {
            runForkJoin(array, numThreads);
        } else {
            System.out.println("Invalid algorithm. Use 'custom' or 'forkjoin'.");
        }
    }

    private static void runCustomWorkStealing(int[] array, int numThreads) {
        // Generate the DAG for merge sort
        Task rootTask = DAGGenerator.generateMergeSortDAG(array, 0, array.length - 1);

        // Execute the custom work-stealing algorithm
        WorkStealing workStealing = new WorkStealing(numThreads);

        long startTime = System.nanoTime();
        workStealing.execute(rootTask);
        long endTime = System.nanoTime();

        System.out.println("Sorted Array: " + java.util.Arrays.toString(array));
        System.out.println("Custom Work-Stealing Execution Time: " + (endTime - startTime) / 1_000_000 + " ms");
    }

    private static void runForkJoin(int[] array, int numThreads) {
        // Create a ForkJoinPool with the specified number of threads
        ForkJoinPool pool = new ForkJoinPool(numThreads);

        // Create the root task for merge sort
        ForkJoinMergeSort task = new ForkJoinMergeSort(array, 0, array.length - 1);

        long startTime = System.nanoTime();
        pool.invoke(task);
        long endTime = System.nanoTime();

        System.out.println("Sorted Array: " + java.util.Arrays.toString(array));
        System.out.println("ForkJoinPool Execution Time: " + (endTime - startTime) / 1_000_000 + " ms");
    }
}
