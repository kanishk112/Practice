package com.notificationsystem.strategies;

import com.notificationsystem.models.ChannelMedium;
import com.notificationsystem.models.Notification;

public class SMSSender implements NotificationSender {

    @Override
    public boolean supports(ChannelMedium channel) {
        return ChannelMedium.SMS.equals(channel);
    }

    @Override
    public boolean send(Notification notification) {
        if (notification == null || notification.getRecipient() == null) {
            return false;
        }
        String phone = notification.getRecipient().getPhoneNo();
        System.out.println("[SMSSender - Telecom Gateway] Dispatching SMS to " + phone + 
                           " | Message: \"" + notification.getFinalContent() + "\"");
        return true;
    }
}
