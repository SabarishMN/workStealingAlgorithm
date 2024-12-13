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

Project by: Kishore Kumar Senthilkumar and Sabarish Muthumani Narayanasamy
