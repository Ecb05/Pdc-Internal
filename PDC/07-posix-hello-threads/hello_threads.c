#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define NUM_THREADS 3

/*
 * Thread entry point. Receives a pointer to this thread's id and prints a
 * greeting. Every pthread runs exactly one such function.
 */
static void *thread_function(void *arg) {
    int thread_id = *(int *)arg;
    printf("Hello from thread %d!\n", thread_id);
    return NULL; /* equivalent to pthread_exit(NULL) */
}

int main(void) {
    pthread_t threads[NUM_THREADS];
    int thread_ids[NUM_THREADS];

    /* Create the threads. Each gets its own id by address. */
    for (int i = 0; i < NUM_THREADS; i++) {
        thread_ids[i] = i;
        int rc = pthread_create(&threads[i], NULL, thread_function, &thread_ids[i]);
        if (rc != 0) {
            fprintf(stderr, "Error creating thread %d: %d\n", i, rc);
            exit(EXIT_FAILURE);
        }
    }

    /* Wait for every thread to finish. */
    for (int i = 0; i < NUM_THREADS; i++) {
        int rc = pthread_join(threads[i], NULL);
        if (rc != 0) {
            fprintf(stderr, "Error joining thread %d: %d\n", i, rc);
            exit(EXIT_FAILURE);
        }
    }

    printf("All threads completed.\n");
    return 0;
}

/* Compile: cc -pthread hello_threads.c -o hello_threads
 * Run:     ./hello_threads */
