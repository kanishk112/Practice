package com.notificationsystem.repositories;

import com.notificationsystem.models.Notification;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryNotificationRepository implements NotificationRepository {
    private final Map<String, Notification> store = new ConcurrentHashMap<>();

    @Override
    public void save(Notification notification) {
        if (notification != null && notification.getNotificationId() != null) {
            store.put(notification.getNotificationId(), notification);
        }
    }

    @Override
    public Notification findById(String notificationId) {
        return store.get(notificationId);
    }

    @Override
    public List<Notification> getNotifications(LocalDateTime from, LocalDateTime to) {
        return store.values().stream()
                .filter(n -> {
                    LocalDateTime created = n.getCreatedAt();
                    if (created == null) return false;
                    boolean afterFrom = (from == null || !created.isBefore(from));
                    boolean beforeTo = (to == null || !created.isAfter(to));
                    return afterFrom && beforeTo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Notification> listAll() {
        return new ArrayList<>(store.values());
    }
}
