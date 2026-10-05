# Experiment 08 — Merge Sort using POSIX Threads

## Concept

This experiment applies **divide-and-conquer parallelism** with pthreads. Merge sort
splits an array in half, sorts each half, then **merges** the two sorted halves. The
two halves are independent, so each can be sorted by its **own thread**, recursively.

The recursion forms a **fork–join** tree: `pthread_create` forks two child threads and
`pthread_join` joins them before the merge step. Because all threads share one address
space, the array (and a scratch buffer) are declared as global data.

A plain recursive version would spawn `2^depth` threads, so the refactor adds a
**threshold**: ranges smaller than `THREAD_THRESHOLD` are sorted with an ordinary
recursive merge sort on the *same* thread. This keeps the thread count proportional to
the available cores.

## APIs involved

### C (pthreads)

| API | Purpose |
|-----|---------|
| `pthread_create` | Fork a child thread to sort one half. |
| `pthread_join` | Join the two children before merging (a barrier). |
| `pthread_t` | Thread handle type. |

### Java equivalent — `java.util.concurrent`

| API | Purpose |
|-----|---------|
| `ForkJoinPool` | Runs `ForkJoinTask`s with work-stealing. |
| `RecursiveAction` | A `ForkJoinTask` that returns no result (`compute()`). |
| `RecursiveAction.invokeAll(a, b)` | Fork both subtasks and join them. |
| `Arrays.sort(a, lo, hi)` | Base-case sequential sort for small ranges. |

## Functionality

1. Read `n` and the array from the console.
2. Recursively fork: sort `[left, mid]` and `[mid+1, right]` on separate threads.
3. Join both, then merge the two sorted halves through a scratch buffer.
4. Print the sorted array.

`MergeSortThreads.java` expresses the same algorithm with a `RecursiveAction` and a
`ForkJoinPool`.

## Files

- `merge_sort_pthreads.c` — native pthread version.
- `MergeSortThreads.java` — Java fork–join version.

## Compile & run

```bash
# C
cc -pthread merge_sort_pthreads.c -o msortp
./msortp
# Enter number of elements: 5
# Enter the elements: 5 2 9 1 6

# Java
javac MergeSortThreads.java
java MergeSortThreads
```

## Notes / exam tips

- `pthread_join` is the *synchronisation point* — the parent may not merge until both
  children are done.
- The scratch array (`temp`) is shared; the algorithm guarantees children work on
  **disjoint** index ranges, so there are no write conflicts.
- The threshold is an important practical detail: unbounded thread creation is slower
  than a serial sort for small inputs.
