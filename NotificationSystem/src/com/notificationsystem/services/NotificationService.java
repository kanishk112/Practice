package com.notificationsystem.services;

import com.notificationsystem.models.*;
import com.notificationsystem.observers.NotificationEventListener;
import com.notificationsystem.repositories.ClientRepository;
import com.notificationsystem.repositories.NotificationRepository;
import com.notificationsystem.repositories.TemplateRepository;
import com.notificationsystem.strategies.NotificationSender;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationService {
    private final ClientRepository clientRepo;
    private final NotificationRepository notificationRepo;
    private final TemplateRepository templateRepo;
    private final List<NotificationEventListener> listeners = new CopyOnWriteArrayList<>();
    private final List<NotificationSender> senders = new CopyOnWriteArrayList<>();

    public NotificationService(ClientRepository clientRepo, 
                               NotificationRepository notificationRepo, 
                               TemplateRepository templateRepo) {
        this.clientRepo = clientRepo;
        this.notificationRepo = notificationRepo;
        this.templateRepo = templateRepo;
    }

    public void registerSender(NotificationSender sender) {
        if (sender != null) {
            this.senders.add(sender);
        }
    }

    public void addNotificationEventListener(NotificationEventListener listener) {
        if (listener != null) {
            this.listeners.add(listener);
        }
    }

    public String registerTemplate(String clientId, Template template) {
        Client client = clientRepo.findById(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Cannot register template: Client not found with ID: " + clientId);
        }
        if (template.getTemplateId() == null || template.getTemplateId().trim().isEmpty()) {
            template.setTemplateId("TMPL_" + UUID.randomUUID().toString().substring(0, 8));
        }
        template.setClientId(clientId);
        templateRepo.save(template);
        System.out.println("[NotificationService] Template registered: " + template.getTemplateId() +
                " for Client: " + client.getClientName() + " on Channel: " + template.getChannel());
        return template.getTemplateId();
    }

    public String sendNotification(String clientId, String templateId, Recipient recipient, Map<String, String> params) {
        Client client = clientRepo.findById(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Client not found: " + clientId);
        }

        Template template = templateRepo.findById(templateId);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + templateId);
        }

        if (recipient == null) {
            throw new IllegalArgumentException("Recipient cannot be null");
        }

        // 1. Render template with parameters
        String renderedContent = template.render(params);

        // 2. Create Notification entity
        String notificationId = "NOTIF_" + UUID.randomUUID().toString().substring(0, 8);
        Notification notification = new Notification(
                notificationId,
                clientId,
                recipient,
                template.getChannel(),
                renderedContent,
                NotificationStatus.IN_PROGRESS,
                LocalDateTime.now()
        );

        // 3. Save initial state
        notificationRepo.save(notification);

        // 4. Resolve the right sender strategy using supports()
        NotificationSender matchingSender = senders.stream()
                .filter(s -> s.supports(template.getChannel()))
                .findFirst()
                .orElse(null);

        if (matchingSender == null) {
            notification.setStatus(NotificationStatus.ERROR);
            notificationRepo.save(notification);
            notifyFailed(notification, "No sender configured for channel: " + template.getChannel());
            throw new IllegalStateException("No active sender configured for channel: " + template.getChannel());
        }

        // 5. Dispatch
        boolean isSuccess = matchingSender.send(notification);
        if (isSuccess) {
            notification.setStatus(NotificationStatus.DELIVERED);
            notificationRepo.save(notification);
            notifySent(notification);
        } else {
            notification.setStatus(NotificationStatus.ERROR);
            notificationRepo.save(notification);
            notifyFailed(notification, "Sender strategy failed delivery");
        }

        return notificationId;
    }

    public String scheduleNotification(String clientId, String templateId, Recipient recipient, 
                                       Map<String, String> params, LocalDateTime scheduleTime) {
        Client client = clientRepo.findById(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Client not found: " + clientId);
        }

        Template template = templateRepo.findById(templateId);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + templateId);
        }

        String renderedContent = template.render(params);
        String notificationId = "SCHED_" + UUID.randomUUID().toString().substring(0, 8);

        Notification notification = new Notification(
                notificationId,
                clientId,
                recipient,
                template.getChannel(),
                renderedContent,
                NotificationStatus.SCHEDULED,
                LocalDateTime.now()
        );
        notification.setScheduledAt(scheduleTime);

        notificationRepo.save(notification);
        notifyScheduled(notification);

        return notificationId;
    }

    public void cancelNotifications(String notificationId) {
        Notification notification = notificationRepo.findById(notificationId);
        if (notification == null) {
            System.err.println("[NotificationService] Cannot cancel: Notification ID not found: " + notificationId);
            return;
        }

        if (notification.getStatus() == NotificationStatus.DELIVERED) {
            System.err.println("[NotificationService] Cannot cancel: Notification already DELIVERED: " + notificationId);
            return;
        }

        notification.setStatus(NotificationStatus.CANCELLED);
        notificationRepo.save(notification);
        notifyCancelled(notification);
    }

    public List<Template> getTemplatesByClient(String clientId, ChannelMedium channel) {
        return templateRepo.findByClientIdAndChannel(clientId, channel);
    }

    // Observer helper methods
    private void notifySent(Notification n) {
        for (NotificationEventListener l : listeners) {
            l.onNotificationSent(n);
        }
    }

    private void notifyFailed(Notification n, String reason) {
        for (NotificationEventListener l : listeners) {
            l.onNotificationFailed(n, reason);
        }
    }

    private void notifyScheduled(Notification n) {
        for (NotificationEventListener l : listeners) {
            l.onNotificationScheduled(n);
        }
    }

    private void notifyCancelled(Notification n) {
        for (NotificationEventListener l : listeners) {
            l.onNotificationCancelled(n);
        }
    }
}
