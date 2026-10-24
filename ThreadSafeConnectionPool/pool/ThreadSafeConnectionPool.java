package LLD.ThreadSafeConnectionPool.pool;

import LLD.ThreadSafeConnectionPool.connection.BasicConnection;
import LLD.ThreadSafeConnectionPool.core.Connection;
import LLD.ThreadSafeConnectionPool.core.ConnectionPool;
import LLD.ThreadSafeConnectionPool.decorator.PoolConnectionDecorator;
import LLD.ThreadSafeConnectionPool.model.Stats;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeConnectionPool implements ConnectionPool {

    private final BlockingQueue<Connection> idleQueue;
    private final AtomicInteger totalConnection;
    private final int maxPoolSize;
    private final Stats stats;
    private final AtomicBoolean isShutdown = new AtomicBoolean(false);

    public ThreadSafeConnectionPool(int maxPoolSize) {
        if (maxPoolSize <= 0) {
            throw new IllegalArgumentException("maxPoolSize must be greater than 0");
        }
        this.maxPoolSize = maxPoolSize;
        this.idleQueue = new LinkedBlockingQueue<>(maxPoolSize);
        this.totalConnection = new AtomicInteger(0);
        this.stats = new Stats();
    }

    @Override
    public Connection acquire(long timeoutMs) throws InterruptedException, TimeoutException {
        checkNotShutdown();
        long startTime = System.currentTimeMillis();

        Connection conn = idleQueue.poll();

        if (conn == null) {
            while (true) {
                int current = totalConnection.get();
                if (current >= maxPoolSize) {
                    break;
                }
                if (totalConnection.compareAndSet(current, current + 1)) {
                    conn = createPhysicalConnection();
                    break;
                }
            }
        }

        if (conn == null) {
            long elapsed = System.currentTimeMillis() - startTime;
            long remaining = timeoutMs - elapsed;
            if (remaining <= 0) {
                throw new TimeoutException("Connection acquisition timed out after " + timeoutMs + " ms");
            }

            conn = idleQueue.poll(remaining, TimeUnit.MILLISECONDS);
            if (conn == null) {
                throw new TimeoutException("Connection acquisition timed out after " + timeoutMs + " ms");
            }
        }

        // Test-on-Borrow Validation (FR 5)
        if (!conn.isValid()) {
            stats.recordValidationFailure();
            totalConnection.decrementAndGet();
            conn.close();

            long elapsed = System.currentTimeMillis() - startTime;
            long remaining = timeoutMs - elapsed;
            if (remaining <= 0) {
                throw new TimeoutException("Timed out while retrying after invalid connection detection.");
            }
            return acquire(remaining);
        }

        // Update Metrics (FR 6)
        long duration = System.currentTimeMillis() - startTime;
        stats.recordAcquisition(duration);
        stats.incrementActive();
        stats.setIdleCount(idleQueue.size());

        return new PoolConnectionDecorator(conn, this);
    }

    @Override
    public void release(Connection conn) {
        if (conn == null) {
            return;
        }

        stats.decrementActive();

        if (isShutdown.get()) {
            conn.close();
            return;
        }

        if (conn.isValid()) {
            if (!idleQueue.offer(conn)) {
                conn.close();
                totalConnection.decrementAndGet();
            } else {
                stats.setIdleCount(idleQueue.size());
            }
        } else {
            conn.close();
            totalConnection.decrementAndGet();
        }
    }

    @Override
    public void shutDown() {
        if (isShutdown.compareAndSet(false, true)) {
            Connection conn;
            while ((conn = idleQueue.poll()) != null) {
                conn.close();
            }
            totalConnection.set(0);
            stats.setIdleCount(0);
        }
    }

    @Override
    public Stats getStats() {
        return stats;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getTotalConnectionCount() {
        return totalConnection.get();
    }

    protected Connection createPhysicalConnection() {
        String id = "conn-" + UUID.randomUUID().toString().substring(0, 8);
        return new BasicConnection(id);
    }

    private void checkNotShutdown() {
        if (isShutdown.get()) {
            throw new IllegalStateException("Connection pool has been shut down.");
        }
    }
}
