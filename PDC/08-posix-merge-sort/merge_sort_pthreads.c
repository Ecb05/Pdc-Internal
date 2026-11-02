#include <stdio.h>
#include <pthread.h>

int a[100], temp[100];

void merge(int l, int m, int r)
{
    int i = l, j = m + 1, k = l;

    while (i <= m && j <= r)
    {
        if (a[i] <= a[j])
            temp[k++] = a[i++];
        else
            temp[k++] = a[j++];
    }

    while (i <= m)
        temp[k++] = a[i++];

    while (j <= r)
        temp[k++] = a[j++];

    for (i = l; i <= r; i++)
        a[i] = temp[i];
}

void *merge_sort(void *arg)
{
    int *p = (int *)arg;
    int l = p[0], r = p[1];

    if (l < r)
    {
        int m = (l + r) / 2;
        int left[] = {l, m};
        int right[] = {m + 1, r};

        pthread_t t1, t2;

        pthread_create(&t1, NULL, merge_sort, left);
        pthread_create(&t2, NULL, merge_sort, right);

        pthread_join(t1, NULL);
        pthread_join(t2, NULL);

        merge(l, m, r);
    }

    return NULL;
}

int main()
{
    int n;
    int range[2];
    pthread_t t;

    printf("Enter number of elements: ");
    scanf("%d", &n);

    if (n < 1 || n > 100)
    {
        printf("Enter a size between 1 and 100.\n");
        return 1;
    }

    printf("Enter elements: ");
    for (int i = 0; i < n; i++)
        scanf("%d", &a[i]);

    range[0] = 0;
    range[1] = n - 1;

    pthread_create(&t, NULL, merge_sort, range);
    pthread_join(t, NULL);

    printf("Sorted array: ");
    for (int i = 0; i < n; i++)
        printf("%d ", a[i]);

    printf("\n");
    return 0;
}
