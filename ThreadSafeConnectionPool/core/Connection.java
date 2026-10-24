package LLD.ThreadSafeConnectionPool.core;

public interface Connection extends AutoCloseable {

    String executeQuery(String sql) throws Exception;

    boolean isValid();

    @Override
    void close();
}
