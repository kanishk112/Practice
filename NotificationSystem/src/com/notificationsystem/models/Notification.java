package com.notificationsystem.models;

import java.time.LocalDateTime;

public class Notification {
    private String notificationId;
    private String clientId;
    private Recipient recipient;
    private ChannelMedium channelMedium;
    private String finalContent;
    private NotificationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime scheduledAt;

    public Notification(String notificationId, String clientId, Recipient recipient, 
                        ChannelMedium channelMedium, String finalContent, 
                        NotificationStatus status, LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.clientId = clientId;
        this.recipient = recipient;
        this.channelMedium = channelMedium;
        this.finalContent = finalContent;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public ChannelMedium getChannelMedium() {
        return channelMedium;
    }

    public void setChannelMedium(ChannelMedium channelMedium) {
        this.channelMedium = channelMedium;
    }

    public String getFinalContent() {
        return finalContent;
    }

    public void setFinalContent(String finalContent) {
        this.finalContent = finalContent;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + notificationId + '\'' +
                ", client='" + clientId + '\'' +
                ", recipient=" + (recipient != null ? recipient.getRecipientName() : "null") +
                ", channel=" + channelMedium +
                ", status=" + status +
                ", content='" + finalContent + '\'' +
                '}';
    }
}
