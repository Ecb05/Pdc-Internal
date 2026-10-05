#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define MAX 100000
/* Ranges smaller than this are sorted serially to avoid spawning
 * exponentially many threads. */
#define THREAD_THRESHOLD 4096

static int arr[MAX];
static int temp[MAX]; /* scratch buffer shared by the merge step */

typedef struct {
    int left;
    int right;
} ThreadData;

/* Merge two adjacent sorted ranges arr[left..mid] and arr[mid+1..right]. */
static void merge(int left, int mid, int right) {
    int i = left, j = mid + 1, k = left;

    while (i <= mid && j <= right) {
        if (arr[i] <= arr[j]) {
            temp[k++] = arr[i++];
        } else {
            temp[k++] = arr[j++];
        }
    }
    while (i <= mid) {
        temp[k++] = arr[i++];
    }
    while (j <= right) {
        temp[k++] = arr[j++];
    }
    for (i = left; i <= right; i++) {
        arr[i] = temp[i];
    }
}

/* Ordinary recursive merge sort for small ranges (no new threads). */
static void merge_sort_serial(int left, int right) {
    if (left >= right) {
        return;
    }
    int mid = left + (right - left) / 2;
    merge_sort_serial(left, mid);
    merge_sort_serial(mid + 1, right);
    merge(left, mid, right);
}

/* Thread entry point: sort arr[left..right], forking for large ranges. */
static void *merge_sort(void *arg) {
    ThreadData *data = (ThreadData *)arg;
    int left = data->left;
    int right = data->right;

    if (left >= right) {
        return NULL;
    }

    int mid = left + (right - left) / 2;

    if (right - left < THREAD_THRESHOLD) {
        merge_sort_serial(left, right);
        return NULL;
    }

    ThreadData left_data = {left, mid};
    ThreadData right_data = {mid + 1, right};

    pthread_t left_thread, right_thread;
    pthread_create(&left_thread, NULL, merge_sort, &left_data);
    pthread_create(&right_thread, NULL, merge_sort, &right_data);

    /* Both halves are done only after these joins. */
    pthread_join(left_thread, NULL);
    pthread_join(right_thread, NULL);

    merge(left, mid, right);
    return NULL;
}

int main(void) {
    int n;
    printf("Enter number of elements: ");
    if (scanf("%d", &n) != 1 || n <= 0 || n > MAX) {
        fprintf(stderr, "Invalid size (1..%d)\n", MAX);
        return EXIT_FAILURE;
    }

    printf("Enter the elements:\n");
    for (int i = 0; i < n; i++) {
        scanf("%d", &arr[i]);
    }

    ThreadData data = {0, n - 1};
    pthread_t main_thread;
    pthread_create(&main_thread, NULL, merge_sort, &data);
    pthread_join(main_thread, NULL);

    printf("Sorted array:\n");
    for (int i = 0; i < n; i++) {
        printf("%d ", arr[i]);
    }
    printf("\n");
    return 0;
}

/* Compile: cc -pthread merge_sort_pthreads.c -o msortp
 * Run:     ./msortp */
