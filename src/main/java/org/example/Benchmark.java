package org.example;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Benchmark {

    // Generate a random array of integers
    public static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(100000); // Random numbers between 0 and 50
        }
        return array;
    }

    public static void main(String[] args) {
        //  if (args.length < 4) {
        //     System.out.println("Usage: java Benchmark <algorithm> <numThreads> <arraySize> <iterations>");
        //   System.out.println("<algorithm>: 'custom' or 'forkjoin'");
        // System.out.println("<numThreads>: Number of threads to use");
        // System.out.println("<arraySize>: Size of the array to sort");
        // System.out.println("<iterations>: Number of iterations to run");
        //  return;
        // }

        // Parse arguments
        String algorithm = args[0]; // 'custom' or 'forkjoin'
        int numThreads = Integer.parseInt(args[1]); // Number of threads
        int arraySize = Integer.parseInt(args[2]); // Size of the array
        int iterations = Integer.parseInt(args[3]); // Number of iterations

        // Generate a random array
        int[] array = generateRandomArray(arraySize);
        System.out.println("Benchmarking " + algorithm + " with array size " + arraySize + " using " + numThreads + " threads over " + iterations + " iterations.");

        // Benchmark based on the chosen algorithm
        if (algorithm.equalsIgnoreCase("custom")) {
            benchmarkCustomWorkStealing(array, numThreads, iterations);
        } else if (algorithm.equalsIgnoreCase("forkjoin")) {
            benchmarkForkJoin(array, numThreads, iterations);
        } else {
            System.out.println("Invalid algorithm. Use 'custom' or 'forkjoin'.");
        }
    }

    private static void benchmarkCustomWorkStealing(int[] array, int numThreads, int iterations) {
        long totalExecutionTime = 0;
        for (int i = 0; i < iterations; i++) {
            // Clone the array for each iteration
            int[] arrayCopy = array.clone();

            // Generate the DAG for merge sort
            Task rootTask = DAGGenerator.generateMergeSortDAG(arrayCopy, 0, arrayCopy.length - 1);

            // Execute the custom work-stealing algorithm
            WorkStealing workStealing = new WorkStealing(numThreads);

            long startTime = System.nanoTime();
            workStealing.execute(rootTask);
            long endTime = System.nanoTime();

            long executionTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
            totalExecutionTime += executionTime;

            System.out.println("Iteration " + (i + 1) + ": Custom Work-Stealing Execution Time: " + executionTime + " ms");
        }

        System.out.println("Average Custom Work-Stealing Execution Time: " + (totalExecutionTime / iterations) + " ms");
    }

    private static void benchmarkForkJoin(int[] array, int numThreads, int iterations) {
        long totalExecutionTime = 0;
        for (int i = 0; i < iterations; i++) {
            // Clone the array for each iteration
            int[] arrayCopy = array.clone();

            // Create a ForkJoinPool with the specified number of threads
            ForkJoinPool pool = new ForkJoinPool(numThreads);

            // Create the root task for merge sort
            ForkJoinMergeSort task = new ForkJoinMergeSort(arrayCopy, 0, arrayCopy.length - 1);

            long startTime = System.nanoTime();
            pool.invoke(task);
            long endTime = System.nanoTime();

            long executionTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
            totalExecutionTime += executionTime;

            System.out.println("Iteration " + (i + 1) + ": ForkJoinPool Execution Time: " + executionTime + " ms");
        }

        System.out.println("Average ForkJoinPool Execution Time: " + (totalExecutionTime / iterations) + " ms");
    }
}
