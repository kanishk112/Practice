package LLD.InMemoryCacheSystem.decorator;

import java.util.Optional;

import LLD.InMemoryCacheSystem.core.Cache;
import LLD.InMemoryCacheSystem.model.CacheStats;

public class MetricsCacheDecorator<K, V> extends AbstractCacheDecorator<K, V> {

    private final CacheStats cacheStats;

    public MetricsCacheDecorator(Cache<K, V> delegate, CacheStats cacheStats) {
        super(delegate);
        this.cacheStats = cacheStats;
    }

    @Override
    public Optional<V> get(K key) {
        long startNanos = System.nanoTime();
        Optional<V> result = super.get(key);
        long elapsedNanos = System.nanoTime() - startNanos;

        if (result.isPresent()) {
            cacheStats.recordHit(elapsedNanos);
        } else {
            cacheStats.recordMiss(elapsedNanos);
        }
        return result;
    }

    @Override
    public void put(K key, V value) {
        long startNanos = System.nanoTime();
        super.put(key, value);
        long elapsedNanos = System.nanoTime() - startNanos;
        cacheStats.recordPut(elapsedNanos);
    }

    @Override
    public boolean remove(K key) {
        boolean removed = super.remove(key);
        if (removed) {
            cacheStats.recordRemoval();
        }
        return removed;
    }

    public CacheStats getStats() {
        return cacheStats;
    }
}
