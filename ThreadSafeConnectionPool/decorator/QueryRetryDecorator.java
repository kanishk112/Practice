package LLD.ThreadSafeConnectionPool.decorator;

import LLD.ThreadSafeConnectionPool.core.Connection;

public class QueryRetryDecorator extends AbstractConnectionDecorator {

    private final int maxRetries;

    public QueryRetryDecorator(Connection delegate, int maxRetries) {
        super(delegate);
        this.maxRetries = Math.max(1, maxRetries);
    }

    @Override
    public String executeQuery(String sql) throws Exception {
        int attempts = 0;
        Exception lastException = null;

        while (attempts < maxRetries) {
            attempts++;
            try {
                return super.executeQuery(sql);
            } catch (Exception e) {
                lastException = e;
                System.err.printf("[QueryRetryDecorator] Attempt %d/%d failed for query \"%s\": %s%n",
                        attempts, maxRetries, sql, e.getMessage());
                if (attempts >= maxRetries) {
                    break;
                }
                Thread.sleep(50L * attempts);
            }
        }

        throw new RuntimeException("Query execution failed after " + maxRetries + " attempts", lastException);
    }
}
