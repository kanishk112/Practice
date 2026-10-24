package LLD.ThreadSafeConnectionPool.decorator;

import LLD.ThreadSafeConnectionPool.core.Connection;

public abstract class AbstractConnectionDecorator implements Connection {

    protected final Connection delegate;

    public AbstractConnectionDecorator(Connection delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("Delegate connection cannot be null");
        }
        this.delegate = delegate;
    }

    public Connection getDelegate() {
        return delegate;
    }

    @Override
    public String executeQuery(String sql) throws Exception {
        return delegate.executeQuery(sql);
    }

    @Override
    public boolean isValid() {
        return delegate.isValid();
    }

    @Override
    public void close() {
        delegate.close();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " -> " + delegate.toString();
    }
}
