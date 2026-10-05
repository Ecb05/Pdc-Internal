#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

/*
 * Parallel reduction: sum all elements of an array.
 * The reduction(+:sum) clause gives each thread a private copy of sum,
 * then combines the copies with + when the parallel loop ends.
 */
int main(void) {
    int size;

    printf("Enter the size of the array: ");
    if (scanf("%d", &size) != 1 || size <= 0) {
        fprintf(stderr, "Invalid size\n");
        return EXIT_FAILURE;
    }

    float *arr = (float *)malloc(size * sizeof(float));
    if (arr == NULL) {
        fprintf(stderr, "Memory allocation failed\n");
        return EXIT_FAILURE;
    }

    printf("Enter the values for the array:\n");
    for (int i = 0; i < size; i++) {
        scanf("%f", &arr[i]);
    }

    float sum = 0.0;

    #pragma omp parallel for reduction(+:sum)
    for (int i = 0; i < size; i++) {
        sum += arr[i];
    }

    printf("Sum of the array elements: %.2f\n", sum);

    free(arr);
    return 0;
}

/* Compile: cc -fopenmp array_sum_omp.c -o predompasum
 * Run:     ./predompasum */
