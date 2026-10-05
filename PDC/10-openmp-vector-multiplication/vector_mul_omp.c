#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

#define N 4 /* size of the matrix and vector */

/*
 * Matrix-vector multiplication: y[i] = sum_j A[i][j] * x[j]
 * The outer loop is parallelised; each thread writes a distinct y[i] and
 * only reads the shared A and x, so no data race occurs.
 */
int main(void) {
    int A[N][N], x[N], y[N];

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
        y[i] = 0;
        for (int j = 0; j < N; j++) {
            y[i] += A[i][j] * x[j]; /* dot product of row i with x */
        }
    }

    double end_time = omp_get_wtime();
    printf("Time taken for matrix-vector multiplication: %f seconds\n", end_time - start_time);

    printf("Result vector y:\n");
    for (int i = 0; i < N; i++) {
        printf("y[%d] = %d\n", i, y[i]);
    }
    return 0;
}

/* Compile: cc -fopenmp vector_mul_omp.c -o vmulomp
 * Run:     ./vmulomp */
