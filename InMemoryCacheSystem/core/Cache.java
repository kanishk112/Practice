package LLD.InMemoryCacheSystem.core;

import java.util.Optional;

public interface Cache<K, V> {
    Optional<V> get(K key);

    void put(K key, V Value);

    boolean remove(K key);

    void clear();

    int size();
}