# Experiment 07 — Hello Message using Multiple Parallel POSIX Threads

## Concept

**POSIX threads** (pthreads, IEEE POSIX 1003.1c) is the standard API for
**shared-memory** parallelism on UNIX-like systems. Threads within one process share a
single virtual address space but each has its own stack, registers and thread id.

The basic model:

- A thread is created with `pthread_create`, runs **one function**, and returns a
  pointer.
- The creating thread may **join** it with `pthread_join` (a blocking wait) to obtain
  its return value.
- Execution order between the new thread and its creator is **undefined** — never
  assume one starts before the other.

Each thread receives a single `void*` argument, which is how arbitrary data is passed
by address.

## APIs involved

### C (pthreads) — `pthread.h`

| API | Purpose |
|-----|---------|
| `pthread_create(&tid, attr, fn, arg)` | Start a new thread running `fn(arg)`. `attr = NULL` means defaults. |
| `pthread_join(tid, &retval)` | Block until the thread finishes. |
| `pthread_exit(retval)` | Terminate the calling thread (implicit on `return`). |
| `pthread_self()` | Get the calling thread's id. |

### Java equivalent — `java.lang.Thread`

| API | Purpose |
|-----|---------|
| `new Thread(Runnable)` | Define a thread's work. |
| `Thread.start()` | Begin execution (do **not** call `run()` directly). |
| `Thread.join()` | Wait for the thread to finish. |
| `Thread.currentThread().getName()` | Identify the running thread. |

## Functionality

- `hello_threads.c` creates **3** threads. Thread *i* receives a pointer to its own id
  and prints `Hello from thread i!`. The `main` thread joins all of them, then prints
  `All threads completed.`
- `HelloThreads.java` is the Java equivalent: it starts 3 threads, joins them, and
  prints the same messages (order is unspecified).

## Files

- `hello_threads.c` — native pthread version.
- `HelloThreads.java` — Java equivalent.

## Compile & run

```bash
# C
cc -pthread hello_threads.c -o hello_threads
./hello_threads

# Java
javac HelloThreads.java
java HelloThreads
```

## Notes / exam tips

- The output order varies between runs — that is expected with threads.
- Pass *distinct* data to each thread. The C version passes `&thread_ids[i]` so each
  thread owns its id; sharing one variable would be a race.
- `pthread_join` is the pthread equivalent of Java's `Thread.join()`.
