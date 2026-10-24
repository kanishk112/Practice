package LLD.ThreadSafeConnectionPool;

import LLD.ThreadSafeConnectionPool.core.Connection;
import LLD.ThreadSafeConnectionPool.core.ConnectionPool;
import LLD.ThreadSafeConnectionPool.decorator.MetricsDecorator;
import LLD.ThreadSafeConnectionPool.decorator.QueryRetryDecorator;
import LLD.ThreadSafeConnectionPool.pool.ThreadSafeConnectionPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws Exception {

        // 1. Initialize Pool with Max Size of 3
        int maxPoolSize = 3;
        ConnectionPool pool = new ThreadSafeConnectionPool(maxPoolSize);

        // 2. Demonstrate Single-Threaded Acquire & Try-With-Resources (Proxy close)
        System.out.println("--- 1. Testing Single Acquire, Query Decorators & Auto-Return ---");
        try (Connection conn = pool.acquire(2000)) {
            // Apply Decorators: Retry -> Metrics -> Pooled Connection
            Connection resilientConn = new QueryRetryDecorator(
                    new MetricsDecorator(conn, pool.getStats()),
                    3);

            String result = resilientConn.executeQuery("SELECT * FROM users WHERE id = 1");
            System.out.println("Client received: " + result);
        } // Connection automatically returned here via PoolConnectionDecorator.close()

        System.out.println("Pool stats after return: " + pool.getStats());

        // 3. Demonstrate Concurrent Worker Threads Borrowing & Returning
        System.out.println("\n--- 2. Testing Concurrent Access (6 Tasks competing for 3 Connections) ---");
        int threadCount = 6;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<Future<Void>> futures = new ArrayList<>();

        for (int i = 1; i <= threadCount; i++) {
            final int taskId = i;
            futures.add(executor.submit(() -> {
                try {
                    System.out.printf("[Task-%d] Requesting connection...%n", taskId);
                    try (Connection conn = pool.acquire(3000)) {
                        System.out.printf("[Task-%d] Acquired! Pool Stats: %s%n", taskId, pool.getStats());

                        // Execute query
                        String res = conn
                                .executeQuery("UPDATE accounts SET balance = balance + 100 WHERE id = " + taskId);

                        // Simulate query processing time
                        Thread.sleep(150);
                    }
                    System.out.printf("[Task-%d] Released connection back to pool.%n", taskId);
                } catch (Exception e) {
                    System.err.printf("[Task-%d] Error: %s%n", taskId, e.getMessage());
                } finally {
                    latch.countDown();
                }
                return null;
            }));
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        // 4. Demonstrate Timeout Handling When Pool is Exhausted
        System.out.println("\n--- 3. Testing Timeout Handling Under Starvation ---");
        Connection c1 = pool.acquire(1000);
        Connection c2 = pool.acquire(1000);
        Connection c3 = pool.acquire(1000);
        System.out.printf("Exhausted all %d connections. Current Stats: %s%n", maxPoolSize, pool.getStats());

        try {
            System.out.println("Attempting to acquire 4th connection with 300ms timeout...");
            pool.acquire(300);
            System.out.println("ERROR: Acquisition should have timed out!");
        } catch (TimeoutException te) {
            System.out.println("SUCCESS: Caught expected TimeoutException -> " + te.getMessage());
        } finally {
            c1.close();
            c2.close();
            c3.close();
        }

        // 5. Final Stats and Teardown
        System.out.println("\n--- 4. Final Summary & Shutdown ---");
        System.out.println("Final " + pool.getStats());
        pool.shutDown();
        System.out.println("Pool successfully shut down.");
    }
}
