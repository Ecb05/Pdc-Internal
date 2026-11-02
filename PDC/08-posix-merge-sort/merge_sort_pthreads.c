#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define MAX 100

int arr[MAX];
int temp[MAX];

/* Stores the range of the array assigned to a thread. */
typedef struct {
    int left;
    int right;
} ThreadData;

/* Merge two sorted parts: arr[left..mid] and arr[mid+1..right]. */
void merge(int left, int mid, int right)
{
    int i = left;
    int j = mid + 1;
    int k = left;

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

/* Sort the assigned range using two POSIX threads. */
void *merge_sort(void *arg)
{
    ThreadData *data = (ThreadData *)arg;

    int left = data->left;
    int right = data->right;

    if (left < right) {
        int mid = left + (right - left) / 2;

        ThreadData left_data = {left, mid};
        ThreadData right_data = {mid + 1, right};

        pthread_t left_thread;
        pthread_t right_thread;

        pthread_create(&left_thread, NULL, merge_sort, &left_data);
        pthread_create(&right_thread, NULL, merge_sort, &right_data);

        pthread_join(left_thread, NULL);
        pthread_join(right_thread, NULL);

        merge(left, mid, right);
    }

    return NULL;
}

int main(void)
{
    int n;
    pthread_t main_thread;

    printf("Enter number of elements: ");
    if (scanf("%d", &n) != 1 || n < 1 || n > MAX) {
        printf("Invalid size. Enter a value between 1 and %d.\n", MAX);
        return 1;
    }

    printf("Enter the elements:\n");
    for (int i = 0; i < n; i++) {
        scanf("%d", &arr[i]);
    }

    ThreadData data = {0, n - 1};

    pthread_create(&main_thread, NULL, merge_sort, &data);
    pthread_join(main_thread, NULL);

    printf("Sorted array:\n");
    for (int i = 0; i < n; i++) {
        printf("%d ", arr[i]);
    }

    printf("\n");
    return 0;
}
