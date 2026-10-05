import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * Java equivalent of the OpenMP parallel reduction.
 *
 * Concept: a parallel stream reduction splits the work across threads and
 * combines partial results — the same idea as OpenMP's reduction clause,
 * but expressed with the streams API instead of a directive.
 */
public class ArraySum {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the size of the array: ");
        int size = scanner.nextInt();

        double[] arr = new double[size];
        System.out.println("Enter the values for the array:");
        for (int i = 0; i < size; i++) {
            arr[i] = scanner.nextDouble();
        }

        double sum = IntStream.range(0, size)
                .parallel()
                .mapToDouble(i -> arr[i])
                .sum();

        System.out.printf("Sum of the array elements: %.2f%n", sum);
    }
}
