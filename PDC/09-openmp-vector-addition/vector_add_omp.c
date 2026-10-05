#include <omp.h>
#include <stdio.h>
#include <stdlib.h>

#define N 4 /* size of the matrix and vector */

/*
 * Matrix-vector addition: y[i] = x[i] + sum_j A[i][j]
 * The outer loop is parallelised with OpenMP; each thread writes a
 * distinct y[i], so there is no data race.
 */
int main(void) {
  int A[N][N], x[N], y[N];

  /* Initialize matrix A and vector x. */
  for (int i = 0; i < N; i++) {
    x[i] = i + 1;
    for (int j = 0; j < N; j++) {
      A[i][j] = (i + 1) * (j + 1);
    }
  }

  printf("Matrix A:\n");
  for (int i = 0; i < N; i++) {
    for (int j = 0; j < N; j++) {
      printf("%d ", A[i][j]);
    }
    printf("\n");
  }

  printf("Vector x:\n");
  for (int i = 0; i < N; i++) {
    printf("%d ", x[i]);
  }
  printf("\n");

  double start_time = omp_get_wtime();

#pragma omp parallel for
  for (int i = 0; i < N; i++) {
    y[i] = 0; /* initialize the result */
    for (int j = 0; j < N; j++) {
      y[i] += A[i][j]; /* sum the row */
    }
    y[i] += x[i]; /* add the corresponding element of x */
  }

  double end_time = omp_get_wtime();
  printf("Time taken for matrix-vector addition: %f seconds\n",
         end_time - start_time);

  printf("Result vector y (A + x):\n");
  for (int i = 0; i < N; i++) {
    printf("y[%d] = %d\n", i, y[i]);
  }
  return 0;
}

/* Compile: cc -fopenmp vector_add_omp.c -o vaddomp
 * Run:     ./vaddomp */
