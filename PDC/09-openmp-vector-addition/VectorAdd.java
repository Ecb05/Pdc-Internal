import java.util.stream.IntStream;

/**
 * Java equivalent of the OpenMP matrix-vector addition.
 *
 * Concept: IntStream.parallel() distributes the range 0..N-1 across the
 * common ForkJoinPool, just as "#pragma omp parallel for" distributes loop
 * iterations. Each index writes a distinct y[i], so no synchronisation is
 * needed.
 */
public class VectorAdd {

    private static final int N = 4;

    public static void main(String[] args) {
        int[][] a = new int[N][N];
        int[] x = new int[N];
        int[] y = new int[N];

        for (int i = 0; i < N; i++) {
            x[i] = i + 1;
            for (int j = 0; j < N; j++) {
                a[i][j] = (i + 1) * (j + 1);
            }
        }

        System.out.println("Matrix A:");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Vector x:");
        for (int xi : x) {
            System.out.print(xi + " ");
        }
        System.out.println();

        long startTime = System.nanoTime();

        IntStream.range(0, N).parallel().forEach(i -> {
            int sum = 0;
            for (int j = 0; j < N; j++) {
                sum += a[i][j];
            }
            y[i] = sum + x[i];
        });

        long endTime = System.nanoTime();
        System.out.printf("Time taken for matrix-vector addition: %f seconds%n",
                (endTime - startTime) / 1e9);

        System.out.println("Result vector y (A + x):");
        for (int i = 0; i < N; i++) {
            System.out.println("y[" + i + "] = " + y[i]);
        }
    }
}
