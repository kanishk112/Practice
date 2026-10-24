package LLD.ThreadSafeConnectionPool.decorator;

import LLD.ThreadSafeConnectionPool.core.Connection;
import LLD.ThreadSafeConnectionPool.model.Stats;

public class MetricsDecorator extends AbstractConnectionDecorator {

    private final Stats stats;

    public MetricsDecorator(Connection delegate, Stats stats) {
        super(delegate);
        this.stats = stats;
    }

    @Override
    public String executeQuery(String sql) throws Exception {
        long startTime = System.nanoTime();
        try {
            return super.executeQuery(sql);
        } finally {
            long durationMicros = (System.nanoTime() - startTime) / 1_000;
            System.out.printf("[MetricsDecorator] SQL: \"%s\" executed in %.2f ms%n",
                    sql, durationMicros / 1000.0);
        }
    }
}
