package com.notificationsystem.repositories;

import com.notificationsystem.models.Notification;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository {
    void save(Notification notification);
    Notification findById(String notificationId);
    List<Notification> getNotifications(LocalDateTime from, LocalDateTime to);
    List<Notification> listAll();
}
