package LLD.ThreadSafeConnectionPool.core;

import LLD.ThreadSafeConnectionPool.model.Stats;

import java.util.concurrent.TimeoutException;

public interface ConnectionPool {

    Connection acquire(long timeoutMs) throws InterruptedException, TimeoutException;

    void release(Connection conn);

    void shutDown();

    Stats getStats();
}
