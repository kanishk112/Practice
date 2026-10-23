package LLD.InMemoryCacheSystem.decorator;

import java.util.Objects;
import java.util.Optional;

import LLD.InMemoryCacheSystem.core.Cache;

public abstract class AbstractCacheDecorator<K, V> implements Cache<K, V> {

    protected final Cache<K, V> delegate;

    protected AbstractCacheDecorator(Cache<K, V> delegate) {
        this.delegate = Objects.requireNonNull(delegate, "Delegate cache cannot be null");
    }

    @Override
    public Optional<V> get(K key) {
        return delegate.get(key);
    }

    @Override
    public void put(K key, V value) {
        delegate.put(key, value);
    }

    @Override
    public boolean remove(K key) {
        return delegate.remove(key);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public int size() {
        return delegate.size();
    }
}