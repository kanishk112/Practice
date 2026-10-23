package LLD.InMemoryCacheSystem;

import LLD.InMemoryCacheSystem.core.Cache;
import LLD.InMemoryCacheSystem.decorator.LoggingCacheDecorator;
import LLD.InMemoryCacheSystem.decorator.MetricsCacheDecorator;
import LLD.InMemoryCacheSystem.decorator.SynchronizedCacheDecorator;
import LLD.InMemoryCacheSystem.model.CacheStats;
import LLD.InMemoryCacheSystem.storage.InMemoryLRUCache;

public class Main {
    public static void main(String[] args) {
        CacheStats stats = new CacheStats();

        // Layering: Raw Cache -> Synchronized -> Metrics -> Logging
        Cache<String, String> userCache = new LoggingCacheDecorator<>(
                new MetricsCacheDecorator<>(
                        new SynchronizedCacheDecorator<>(
                                new InMemoryLRUCache<>(2)),
                        stats),
                "UserSessionCache");

        userCache.put("user_1", "Alice");
        userCache.put("user_2", "Bob");
        userCache.get("user_1"); // Hit -> Alice becomes MRU
        userCache.put("user_3", "Charlie"); // Evicts user_2 (LRU)

        System.out.println("Contains user_2? " + userCache.get("user_2").isPresent()); // Miss
        System.out.println("Contains user_1? " + userCache.get("user_1").isPresent()); // Hit
        System.out.println("Stats: " + stats);
    }
}
