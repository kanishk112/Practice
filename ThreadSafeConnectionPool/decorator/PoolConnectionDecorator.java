package LLD.ThreadSafeConnectionPool.decorator;

import LLD.ThreadSafeConnectionPool.core.Connection;
import LLD.ThreadSafeConnectionPool.core.ConnectionPool;

import java.util.concurrent.atomic.AtomicBoolean;

public class PoolConnectionDecorator extends AbstractConnectionDecorator {

    private final ConnectionPool pool;
    private final AtomicBoolean isReturned = new AtomicBoolean(false);

    public PoolConnectionDecorator(Connection delegate, ConnectionPool pool) {
        super(delegate);
        if (pool == null) {
            throw new IllegalArgumentException("Connection pool cannot be null");
        }
        this.pool = pool;
    }

    @Override
    public String executeQuery(String sql) throws Exception {
        checkNotReturned();
        return super.executeQuery(sql);
    }

    @Override
    public boolean isValid() {
        if (isReturned.get()) {
            return false;
        }
        return super.isValid();
    }

    @Override
    public void close() {
        if (isReturned.compareAndSet(false, true)) {
            pool.release(delegate);
        }
    }

    private void checkNotReturned() {
        if (isReturned.get()) {
            throw new IllegalStateException("Connection has already been closed and returned to the pool.");
        }
    }
}
