# Custom Work-Stealing Algorithm Using DAGs

This project implements a **custom work-stealing algorithm** for task scheduling and parallelism using **Directed Acyclic Graphs (DAGs)**. The algorithm is applied to **parallel merge sort**, where worker threads dynamically steal tasks to maximize parallelism and efficiency.

---

## Project Description

### **What is Work-Stealing?**
Work-stealing is a dynamic scheduling algorithm where idle worker threads "steal" tasks from other busy threads' queues. It ensures better load balancing and minimizes idle time in parallel computing.

### **Key Features:**
1. **Custom DAG-Based Scheduler:**
   - Tasks and their dependencies are modeled as a Directed Acyclic Graph (DAG).
   - Tasks are scheduled dynamically based on their readiness (i.e., no unfulfilled dependencies).
   
2. **Parallel Merge Sort:**
   - The DAG generates tasks and dependencies for sorting subarrays.
   - Worker threads dynamically pick and execute tasks, leveraging parallelism.

3. **Benchmarking:**
   - The custom work-stealing algorithm is benchmarked against Java's inbuilt **Fork/Join Pool**.
   - Performance metrics such as execution time and scalability are compared.

---

## Technologies Used
- **Language**: Java
- **Build Tool**: Gradle
- **Framework**: Fork/Join Framework (for benchmarking)

---

## Key Components
1. **DAG Task Generation**:
   - Tasks and their dependencies are represented as nodes and edges in a DAG.
   - Ensures proper sequencing of tasks for merge sort.

2. **Custom Work-Stealing Algorithm**:
   - Worker threads dynamically fetch tasks from their own or other threads' queues.
   - Balances workload across threads to maximize CPU utilization.

3. **Parallel Merge Sort**:
   - Divides the array into smaller subarrays, sorting them in parallel.
   - Merges sorted subarrays recursively using the DAG.

4. **Benchmarking**:
   - Compared with the Java Fork/Join Pool to evaluate:
     - Execution time
     - Thread utilization
     - Scalability with increasing input size

---

This project implements and benchmarks two algorithms, Custom Work Stealing Algorithm and ForkJoin Algorithm. the implementation were all done in java programming language and it uses gradle.

Run the following commands in a terminal to execute the program:

**./gradle build**            

**java -cp build/classes/java/main org.example.Benchmark**

There are 4 arguemtents:

ARG1: ("Custom" or "ForkJoin")

ARG2: (Number of threads)

ARG3: (Size of array)

ARG4: (Number of iterations)

for example: 

**java -cp build/classes/java/main org.example.Benchmark Custom 8 100 2**

Note: The custom algorithm will take time (20-35 seconds) to show an output for high array sizes (10000, 100000) but eventuallly give you the execution time.

Project by: Sabarish Muthumani Narayanasamy
