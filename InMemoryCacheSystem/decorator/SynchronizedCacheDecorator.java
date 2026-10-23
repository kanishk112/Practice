package LLD.InMemoryCacheSystem.decorator;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

import LLD.InMemoryCacheSystem.core.Cache;

public class SynchronizedCacheDecorator<K, V> extends AbstractCacheDecorator<K, V> {

    private final ReentrantLock lock = new ReentrantLock();

    public SynchronizedCacheDecorator(Cache<K, V> delegate) {
        super(delegate);
    }

    @Override
    public Optional<V> get(K key) {
        lock.lock();
        try {
            return super.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(K key, V value) {
        lock.lock();
        try {
            super.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean remove(K key) {
        lock.lock();
        try {
            return super.remove(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            super.clear();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        lock.lock();
        try {
            return super.size();
        } finally {
            lock.unlock();
        }
    }

}
