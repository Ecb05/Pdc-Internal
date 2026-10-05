# Experiment 09 — Matrix–Vector Addition using OpenMP

## Concept

**OpenMP** is a portable, standard API for **shared-memory, multi-threaded
parallelism** in C/C++/Fortran. It is **directive-based** and uses the **fork–join**
model:

- Every program starts as a single process running the **master thread**.
- At a `parallel` construct the master **forks** a team of threads.
- Statements in the region run in parallel across the team.
- At the end of the region the threads **join** — an implicit barrier — leaving only
  the master.

Key terms: **construct** (directive + structured block), **region** (dynamic extent of
a construct), **private** (per-thread storage) and **shared** (single storage).

This experiment adds each row of a matrix `A` and the matching element of a vector
`x`: `y[i] = x[i] + Σ_j A[i][j]`. Since each `i` touches only `y[i]`, the loop is
*embarrassingly parallel* — ideal for `#pragma omp parallel for`.

## APIs / directives involved

| API / directive | Purpose |
|-----------------|---------|
| `#pragma omp parallel for` | Distribute the loop iterations across a team of threads. |
| `omp.h` | OpenMP header (timing + runtime functions). |
| `omp_get_wtime()` | Wall-clock timer (returns seconds as a double). |
| `omp_get_thread_num()` | Id of the calling thread (0 = master). |
| `omp_get_max_threads()` | Size of the thread team. |

### Java equivalent

| API | Purpose |
|-----|---------|
| `java.util.stream.IntStream.range(0, N).parallel()` | Parallel loop (uses the common ForkJoinPool). |
| `System.nanoTime()` | Timing. |

## Functionality

1. Build matrix `A` (`A[i][j] = (i+1)*(j+1)`) and vector `x` (`x[i] = i+1`).
2. Time a `#pragma omp parallel for` loop that, for each row, sums the row and adds
   `x[i]`, storing into `y[i]`.
3. Print the elapsed time and the result vector.

`VectorAdd.java` performs the same computation with a parallel stream.

## Files

- `vector_add_omp.c` — OpenMP version.
- `VectorAdd.java` — Java parallel-stream version.

## Compile & run

```bash
# C (link the OpenMP runtime)
cc -fopenmp vector_add_omp.c -o vaddomp
./vaddomp

# Java
javac VectorAdd.java
java VectorAdd
```

## Notes / exam tips

- `parallel` alone replicates the work; **`for`** (`parallel for`) is what divides the
  iterations among threads.
- There is an **implicit barrier** at the end of a `parallel for`; the master resumes
  only after all threads finish.
- Time a tiny 4×4 problem like this only to *illustrate* `omp_get_wtime()` — the
  parallel overhead dominates at this size.
