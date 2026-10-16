package com.notificationsystem.strategies;

import com.notificationsystem.models.ChannelMedium;
import com.notificationsystem.models.Notification;

public class InAppSender implements NotificationSender {

    @Override
    public boolean supports(ChannelMedium channel) {
        return ChannelMedium.IN_APP.equals(channel);
    }

    @Override
    public boolean send(Notification notification) {
        if (notification == null || notification.getRecipient() == null) {
            return false;
        }
        System.out.println("[InAppSender - WebSocket/In-App Feed] Pushing In-App Banner to recipient: " + 
                           notification.getRecipient().getRecipientId() + " | Text: \"" + notification.getFinalContent() + "\"");
        return true;
    }
}
