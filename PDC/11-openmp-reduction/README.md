# Experiment 11 — Parallel Reduction (Sum of Array Elements) using OpenMP

## Concept

A **reduction** combines many values into one (here, a sum). In a parallel loop the
accumulator is the classic hazard: if every thread wrote the same `sum`, updates would
race. OpenMP's **`reduction` clause** solves this automatically —

> **`reduction(op : list)`** gives each thread a **private copy** of each listed
> variable, initialised to the identity element for `op` (0 for `+`, 1 for `*`), and
> at the end of the region combines all private copies with `op` into the original
> **shared** variable.

So `#pragma omp parallel for reduction(+:sum)` splits the array across threads, sums
each thread's share privately, then adds those partial sums together.

## APIs / directives involved

| API / directive | Purpose |
|-----------------|---------|
| `#pragma omp parallel for reduction(+:sum)` | Parallel loop with an automatic private-accumulator reduction. |
| `omp.h` | OpenMP header. |
| `omp_get_wtime()` | Wall-clock timing (optional). |

Operators allowed for C/C++: `+ - * & ^ | && ||`. The variable must be a **scalar**
and must be **shared** in the enclosing context.

### Java equivalent

| API | Purpose |
|-----|---------|
| `IntStream.range(0, size).parallel().mapToDouble(...).sum()` | Parallel reduction built into the streams API. |
| `java.util.stream.DoubleStream.sum()` | Reduces the mapped stream to one value. |

## Functionality

1. Read the array size, then the elements.
2. In a `parallel for` with `reduction(+:sum)`, add every element to `sum`.
3. Print the total.

`ArraySum.java` performs the same reduction with a parallel stream.

## Files

- `array_sum_omp.c` — OpenMP version.
- `ArraySum.java` — Java parallel-stream version.

## Compile & run

```bash
cc -fopenmp array_sum_omp.c -o predompasum
./predompasum
# Enter the size of the array: 5
# Enter the values for the array: 1 2 3 4 5
# Sum of the array elements: 15.00

javac ArraySum.java
java ArraySum
```

## Notes / exam tips

- Without `reduction`, a naive `sum += arr[i]` in a parallel loop is a **data race**
  and gives wrong answers — this is the key teaching point of the experiment.
- The reduction variable must **not** be declared `private`.
- Floating-point reduction is not truly associative; the order of combining may vary,
  so exact last-digit results can differ run to run.
- Java's parallel-stream `sum()` internally does the same tree-style reduction.
