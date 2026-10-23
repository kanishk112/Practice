package LLD.InMemoryCacheSystem.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import LLD.InMemoryCacheSystem.core.Cache;

public class InMemoryLRUCache<K, V> implements Cache<K, V> {

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Map<K, Node<K, V>> map;
    private final Node<K, V> head;
    private final Node<K, V> tail;

    public InMemoryLRUCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.capacity = capacity;
        this.map = new HashMap<>(capacity);
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    @Override
    public Optional<V> get(K key) {
        Objects.requireNonNull(key, "Cache key cannot be null");
        Node<K, V> node = map.get(key);
        if (node == null) {
            return Optional.empty();
        }
        // Move accessed node to head (mark as Most Recently Used)
        removeNode(node);
        addToHead(node);
        return Optional.of(node.value);
    }

    @Override
    public void put(K key, V value) {
        Objects.requireNonNull(key, "Cache key cannot be null");
        Objects.requireNonNull(value, "Cache value cannot be null");

        Node<K, V> existing = map.get(key);
        if (existing != null) {
            existing.value = value;
            removeNode(existing);
            addToHead(existing);
            return;
        }

        if (map.size() >= capacity) {
            Node<K, V> lruNode = tail.prev;
            removeNode(lruNode);
            map.remove(lruNode.key);
        }

        Node<K, V> newNode = new Node<>(key, value);
        addToHead(newNode);
        map.put(key, newNode);
    }

    @Override
    public boolean remove(K key) {
        Objects.requireNonNull(key, "Cache key cannot be null");
        Node<K, V> node = map.remove(key);
        if (node == null) {
            return false;
        }
        removeNode(node);
        return true;
    }

    @Override
    public void clear() {
        map.clear();
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public int size() {
        return map.size();
    }

    private void addToHead(Node<K, V> node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

}
