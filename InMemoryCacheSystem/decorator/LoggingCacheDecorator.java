package LLD.InMemoryCacheSystem.decorator;

import java.util.Optional;
import java.util.logging.Logger;

import LLD.InMemoryCacheSystem.core.Cache;

public class LoggingCacheDecorator<K, V> extends AbstractCacheDecorator<K, V> {

    private static final Logger LOGGER = Logger.getLogger(LoggingCacheDecorator.class.getName());

    private final String cacheName;

    public LoggingCacheDecorator(Cache<K, V> delegate, String cacheName) {
        super(delegate);
        this.cacheName = cacheName;
    }

    @Override
    public Optional<V> get(K key) {
        long startNanos = System.nanoTime();
        Optional<V> value = super.get(key);
        long durationMicros = (System.nanoTime() - startNanos) / 1_000;

        if (value.isPresent()) {
            LOGGER.info(() -> String.format("[%s] GET key='%s' -> HIT (%d µs)", cacheName, key, durationMicros));
        } else {
            LOGGER.info(() -> String.format("[%s] GET key='%s' -> MISS (%d µs)", cacheName, key, durationMicros));
        }
        return value;
    }

    @Override
    public void put(K key, V value) {
        long startNanos = System.nanoTime();
        super.put(key, value);
        long durationMicros = (System.nanoTime() - startNanos) / 1_000;
        LOGGER.info(
                () -> String.format("[%s] PUT key='%s', value='%s' (%d µs)", cacheName, key, value, durationMicros));
    }

    @Override
    public boolean remove(K key) {
        boolean removed = super.remove(key);
        LOGGER.info(() -> String.format("[%s] REMOVE key='%s' -> removed=%b", cacheName, key, removed));
        return removed;
    }
}
