# Experiment 10 — Matrix–Vector Multiplication using OpenMP

## Concept

Same OpenMP fork–join model as experiment 09, but the inner operation is a **dot
product**: `y[i] = Σ_j A[i][j] * x[j]`. The outer loop over rows is again
*embarrassingly parallel* because each row writes a distinct `y[i]` and only **reads**
`A` and `x`, so no two threads conflict.

This illustrates an important OpenMP idea: a loop is safely parallelisable when its
iterations are **independent** — here, the only cross-iteration dependency would be a
shared accumulator, which we avoid by giving each `i` its own `y[i]`.

## APIs / directives involved

| API / directive | Purpose |
|-----------------|---------|
| `#pragma omp parallel for` | Parallelise the row loop. |
| `omp_get_wtime()` | Wall-clock timing. |
| `omp.h` | OpenMP header. |

### Java equivalent

| API | Purpose |
|-----|---------|
| `IntStream.range(0, N).parallel()` | Parallel loop over rows. |
| `System.nanoTime()` | Timing. |

## Functionality

1. Build matrix `A` (`A[i][j] = (i+1)*(j+1)`) and vector `x` (`x[i] = i+1`).
2. In parallel, for each row compute `y[i] = Σ_j A[i][j] * x[j]`.
3. Print the elapsed time and the result vector.

## Files

- `vector_mul_omp.c` — OpenMP version.
- `VectorMul.java` — Java parallel-stream version.

## Compile & run

```bash
cc -fopenmp vector_mul_omp.c -o vmulomp
./vmulomp

javac VectorMul.java
java VectorMul
```

## Notes / exam tips

- Compare with experiment 09: the *only* change is `y[i] += A[i][j] * x[j]`.
- If the accumulator `sum` were declared **shared** outside the loop, threads would
  race. Declaring it inside the loop body keeps it effectively private per iteration.
- Use `reduction` when a single shared accumulator genuinely must span iterations
  (see experiment 11).
