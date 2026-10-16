package com.notificationsystem.strategies;

import com.notificationsystem.models.ChannelMedium;
import com.notificationsystem.models.Notification;

public interface NotificationSender {
    boolean supports(ChannelMedium channel);
    boolean send(Notification notification);
}
