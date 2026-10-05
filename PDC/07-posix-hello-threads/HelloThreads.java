/**
 * Java equivalent of the POSIX-threads "hello" program.
 *
 * Concept: Thread.start() begins a thread of execution; Thread.join()
 * blocks until that thread finishes. Output order is unspecified, exactly
 * like the pthread version.
 */
public class HelloThreads {

    private static final int NUM_THREADS = 3;

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i; // each thread captures its own id
            threads[i] = new Thread(
                    () -> System.out.println("Hello from thread " + threadId + "!"));
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("All threads completed.");
    }
}
