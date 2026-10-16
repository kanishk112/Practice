package com.notificationsystem.strategies;

import com.notificationsystem.models.ChannelMedium;
import com.notificationsystem.models.Notification;

public class EmailSender implements NotificationSender {

    @Override
    public boolean supports(ChannelMedium channel) {
        return ChannelMedium.EMAIL.equals(channel);
    }

    @Override
    public boolean send(Notification notification) {
        if (notification == null || notification.getRecipient() == null) {
            return false;
        }
        String email = notification.getRecipient().getEmail();
        System.out.println("[EmailSender - SMTP/Sendgrid] Dispatching Email to <" + email + ">" +
                           " | Subject: [Notification from " + notification.getClientId() + "]" +
                           " | Body: \"" + notification.getFinalContent() + "\"");
        return true;
    }
}
