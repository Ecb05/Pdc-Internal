# Experiment 12 — Vector Addition using MPI

## Concept

**MPI (Message Passing Interface)** lets separate processes communicate by sending data.
Here, process 0 divides two arrays among all processes. Each process adds its part,
and process 0 collects the partial results.

## File

- `vector_add_mpi.c` — complete MPI program in C.

## Important MPI functions

- `MPI_Init()` — starts MPI.
- `MPI_Comm_rank()` — gets the current process number.
- `MPI_Comm_size()` — gets the total number of processes.
- `MPI_Scatter()` — sends a part of an array to each process.
- `MPI_Gather()` — collects each process's result on process 0.
- `MPI_Finalize()` — ends MPI.

## Compile and run

Install an MPI implementation such as Open MPI, then run:

```bash
mpicc vector_add_mpi.c -o vector_add
mpirun -np 4 ./vector_add
```

Use a process count that divides 8 (for example, 1, 2, 4, or 8).

## Logic to remember

1. Process 0 initializes arrays A and B.
2. `MPI_Scatter` distributes equal-sized parts of A and B.
3. Every process adds its local elements.
4. `MPI_Gather` collects the answers in process 0.
5. Process 0 prints the final vector.

**Output with 4 processes:** `11 22 33 44 55 66 77 88`
