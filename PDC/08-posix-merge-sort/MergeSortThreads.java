import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Java equivalent of the pthread merge sort, using the fork–join framework.
 *
 * Concept: RecursiveAction.invokeAll() forks both subtasks and joins them
 * before the merge, mirroring pthread_create + pthread_join. Small ranges
 * fall back to a sequential sort (Arrays.sort), mirroring the C threshold.
 */
public class MergeSortThreads {

    private static final int THREAD_THRESHOLD = 4096;

    /** Sorts array[left..right] in parallel, then merges. */
    private static final class MergeSortTask extends RecursiveAction {

        private static final long serialVersionUID = 1L;

        private final int[] array;
        private final int left;
        private final int right;

        MergeSortTask(int[] array, int left, int right) {
            this.array = array;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            if (right - left < THREAD_THRESHOLD) {
                Arrays.sort(array, left, right + 1); // base case, sequential
                return;
            }
            int mid = left + (right - left) / 2;
            invokeAll(new MergeSortTask(array, left, mid),
                      new MergeSortTask(array, mid + 1, right));
            merge(array, left, mid, right);
        }
    }

    /** Merge array[left..mid] and array[mid+1..right] into sorted order. */
    private static void merge(int[] array, int left, int mid, int right) {
        int[] merged = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            merged[k++] = (array[i] <= array[j]) ? array[i++] : array[j++];
        }
        while (i <= mid) {
            merged[k++] = array[i++];
        }
        while (j <= right) {
            merged[k++] = array[j++];
        }
        System.arraycopy(merged, 0, array, left, merged.length);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of elements: ");
        int n = scanner.nextInt();

        int[] array = new int[n];
        System.out.println("Enter the elements:");
        for (int i = 0; i < n; i++) {
            array[i] = scanner.nextInt();
        }

        ForkJoinPool.commonPool().invoke(new MergeSortTask(array, 0, n - 1));

        System.out.println("Sorted array: " + Arrays.toString(array));
    }
}
