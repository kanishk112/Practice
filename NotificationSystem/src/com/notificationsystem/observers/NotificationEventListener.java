package com.notificationsystem.observers;

import com.notificationsystem.models.Notification;

public interface NotificationEventListener {
    void onNotificationScheduled(Notification notification);
    void onNotificationCancelled(Notification notification);
    void onNotificationSent(Notification notification);
    void onNotificationFailed(Notification notification, String error);
}
