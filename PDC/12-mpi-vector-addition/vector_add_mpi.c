#include <stdio.h>
#include <mpi.h>

#define N 8

int main(int argc, char *argv[])
{
    int rank, size;
    int a[N], b[N], result[N];

    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (N % size != 0) {
        if (rank == 0)
            printf("Use a process count that divides %d.\n", N);
        MPI_Finalize();
        return 0;
    }

    int count = N / size;
    int local_a[count], local_b[count], local_result[count];

    if (rank == 0) {
        for (int i = 0; i < N; i++) {
            a[i] = i + 1;
            b[i] = (i + 1) * 10;
        }
    }

    MPI_Scatter(a, count, MPI_INT, local_a, count, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Scatter(b, count, MPI_INT, local_b, count, MPI_INT, 0, MPI_COMM_WORLD);

    for (int i = 0; i < count; i++)
        local_result[i] = local_a[i] + local_b[i];

    MPI_Gather(local_result, count, MPI_INT, result, count, MPI_INT, 0, MPI_COMM_WORLD);

    if (rank == 0) {
        printf("Vector A: ");
        for (int i = 0; i < N; i++)
            printf("%d ", a[i]);

        printf("\nVector B: ");
        for (int i = 0; i < N; i++)
            printf("%d ", b[i]);

        printf("\nResult:   ");
        for (int i = 0; i < N; i++)
            printf("%d ", result[i]);

        printf("\n");
    }

    MPI_Finalize();
    return 0;
}
