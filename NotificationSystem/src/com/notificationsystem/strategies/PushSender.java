package com.notificationsystem.strategies;

import com.notificationsystem.models.ChannelMedium;
import com.notificationsystem.models.Notification;

public class PushSender implements NotificationSender {

    @Override
    public boolean supports(ChannelMedium channel) {
        return ChannelMedium.PUSH.equals(channel);
    }

    @Override
    public boolean send(Notification notification) {
        if (notification == null || notification.getRecipient() == null) {
            return false;
        }
        String recipientName = notification.getRecipient().getRecipientName();
        System.out.println("[PushSender - FCM/APNS Gateway] Dispatching Mobile Push to user: " + recipientName + 
                           " | Payload: \"" + notification.getFinalContent() + "\"");
        return true;
    }
}
