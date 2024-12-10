package org.example;

import java.util.Arrays;

public class WorkStealingMain {
    public static void main(String[] args) {
        int[] array = {38, 27, 43, 3, 9, 82, 10}; // Input array
        int numThreads = 4;

        // Generate the root task for parallel merge sort
        Task rootTask = DAGGenerator.generateMergeSortDAG(array, 0, array.length - 1);

        // Execute the work-stealing algorithm
        WorkStealing workStealing = new WorkStealing(numThreads);
        workStealing.execute(rootTask);

        // Print the sorted array
        System.out.println("Sorted Array: " + Arrays.toString(array));
    }
}
