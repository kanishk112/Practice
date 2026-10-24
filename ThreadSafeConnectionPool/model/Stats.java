package LLD.ThreadSafeConnectionPool.model;

import java.util.concurrent.atomic.LongAdder;

public class Stats {

    private final LongAdder activeConnections = new LongAdder();
    private final LongAdder idleConnection = new LongAdder();
    private final LongAdder acquisationTime = new LongAdder();
    private final LongAdder validationFailures = new LongAdder();
    private final LongAdder totalAcquisitions = new LongAdder();

    public void incrementActive() {
        activeConnections.increment();
    }

    public void decrementActive() {
        activeConnections.decrement();
    }

    public void incrementIdle() {
        idleConnection.increment();
    }

    public void decrementIdle() {
        idleConnection.decrement();
    }

    public void setIdleCount(int count) {
        idleConnection.reset();
        idleConnection.add(count);
    }

    public void recordAcquisition(long durationMs) {
        totalAcquisitions.increment();
        acquisationTime.add(durationMs);
    }

    public void recordValidationFailure() {
        validationFailures.increment();
    }

    public long getActiveConnections() {
        return activeConnections.sum();
    }

    public long getIdleConnections() {
        return idleConnection.sum();
    }

    public long getTotalAcquisitionTimeMs() {
        return acquisationTime.sum();
    }

    public long getTotalAcquisitions() {
        return totalAcquisitions.sum();
    }

    public long getValidationFailures() {
        return validationFailures.sum();
    }

    public double getAverageAcquisitionTimeMs() {
        long count = totalAcquisitions.sum();
        return count == 0 ? 0.0 : (double) acquisationTime.sum() / count;
    }

    @Override
    public String toString() {
        return String.format(
                "Stats{activeConnections=%d, idleConnections=%d, avgAcquisitionTime=%.2f ms, totalAcquisitions=%d, validationFailures=%d}",
                getActiveConnections(),
                getIdleConnections(),
                getAverageAcquisitionTimeMs(),
                getTotalAcquisitions(),
                getValidationFailures());
    }
}
