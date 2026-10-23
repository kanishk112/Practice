package LLD.InMemoryCacheSystem.model;

import java.util.concurrent.atomic.LongAdder;

public class CacheStats {
    private final LongAdder hits = new LongAdder();
    private final LongAdder misses = new LongAdder();
    private final LongAdder puts = new LongAdder();
    private final LongAdder removals = new LongAdder();
    private final LongAdder totalLatencyNanos = new LongAdder();

    public void recordHit(long nanos) {
        hits.increment();
        totalLatencyNanos.add(nanos);
    }

    public void recordMiss(long nanos) {
        misses.increment();
        totalLatencyNanos.add(nanos);
    }

    public void recordPut(long nanos) {
        puts.increment();
        totalLatencyNanos.add(nanos);
    }

    public void recordRemoval() {
        removals.increment();
    }

    public long getHits() {
        return hits.sum();
    }

    public long getMisses() {
        return misses.sum();
    }

    public long getPuts() {
        return puts.sum();
    }

    public long getRemovals() {
        return removals.sum();
    }

    public double getHitRatio() {
        long totalReads = getHits() + getMisses();
        return totalReads == 0 ? 0.0 : (double) getHits() / totalReads;
    }

    public double getAverageLatencyMicros() {
        long totalOps = getHits() + getMisses() + getPuts();
        return totalOps == 0 ? 0.0 : (totalLatencyNanos.sum() / 1_000.0) / totalOps;
    }

    @Override
    public String toString() {
        return String.format(
                "CacheStats{hits=%d, misses=%d, puts=%d, removals=%d, hitRatio=%.2f%%, avgLatency=%.2f µs}",
                getHits(), getMisses(), getPuts(), getRemovals(), getHitRatio() * 100, getAverageLatencyMicros());
    }
}
