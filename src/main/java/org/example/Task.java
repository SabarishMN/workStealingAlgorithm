package org.example;

import java.util.List;
import java.util.ArrayList;

public class Task {
    public int id;
    public List<Task> dependencies;  // Task dependencies
    public boolean isCompleted;
    public int[] array;              // Array for merge sort
    public int start, end;           // Start and end indices
    public int remainingDependencies;

    // Constructor for a simple task
    public Task(int id) {
        this.id = id;
        this.dependencies = new ArrayList<>();
        this.isCompleted = false;
    }

    // Constructor for a merge sort task
    public Task(int id, int[] array, int start, int end, List<Task> dependencies) {
        this.id = id;
        this.array = array;
        this.start = start;
        this.end = end;
        this.dependencies = new ArrayList<>();
        this.isCompleted = false;
        this.remainingDependencies = dependencies.size();
    }
     public void resolveDependency() {
        remainingDependencies--;
     }
}
