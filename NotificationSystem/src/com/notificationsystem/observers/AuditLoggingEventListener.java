package com.notificationsystem.observers;

import com.notificationsystem.models.Notification;

public class AuditLoggingEventListener implements NotificationEventListener {

    @Override
    public void onNotificationScheduled(Notification notification) {
        System.out.println("[AUDIT EVENT] Notification SCHEDULED -> ID: " + notification.getNotificationId() +
                " for Recipient: " + notification.getRecipient().getRecipientName() +
                " at: " + notification.getScheduledAt());
    }

    @Override
    public void onNotificationCancelled(Notification notification) {
        System.out.println("[AUDIT EVENT] Notification CANCELLED -> ID: " + notification.getNotificationId());
    }

    @Override
    public void onNotificationSent(Notification notification) {
        System.out.println("[AUDIT EVENT] Notification DELIVERED -> ID: " + notification.getNotificationId() +
                " via: " + notification.getChannelMedium());
    }

    @Override
    public void onNotificationFailed(Notification notification, String error) {
        System.err.println("[AUDIT EVENT] Notification ERROR -> ID: " + notification.getNotificationId() +
                " | Reason: " + error);
    }
}
