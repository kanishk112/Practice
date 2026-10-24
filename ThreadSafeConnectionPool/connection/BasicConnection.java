package LLD.ThreadSafeConnectionPool.connection;

import LLD.ThreadSafeConnectionPool.core.Connection;

public class BasicConnection implements Connection {

    private final String connectionId;
    private volatile boolean closed;
    private volatile boolean healthy = true;

    public BasicConnection(String connectionId) {
        this.connectionId = connectionId;
        this.closed = false;
    }

    public String getConnectionId() {
        return connectionId;
    }

    @Override
    public String executeQuery(String sql) throws Exception {
        if (closed) {
            throw new IllegalStateException("Cannot execute query on closed connection: " + connectionId);
        }
        if (!healthy) {
            throw new RuntimeException("Connection " + connectionId + " is broken / disconnected!");
        }
        // Simulated query execution
        return "Result of [" + sql + "] on connection " + connectionId;
    }

    @Override
    public boolean isValid() {
        return !closed && healthy;
    }

    @Override
    public void close() {
        this.closed = true;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    @Override
    public String toString() {
        return "BasicConnection{" +
                "connectionId='" + connectionId + '\'' +
                ", closed=" + closed +
                ", healthy=" + healthy +
                '}';
    }
}
