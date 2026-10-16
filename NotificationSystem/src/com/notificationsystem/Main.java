package com.notificationsystem;

import com.notificationsystem.models.*;
import com.notificationsystem.observers.AuditLoggingEventListener;
import com.notificationsystem.repositories.*;
import com.notificationsystem.services.NotificationService;
import com.notificationsystem.strategies.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
        public static void main(String[] args) {

                // 1. Initialize In-Memory Repositories
                ClientRepository clientRepo = new InMemoryClientRepository();
                NotificationRepository notificationRepo = new InMemoryNotificationRepository();
                TemplateRepository templateRepo = new InMemoryTemplateRepository();

                // 2. Initialize Notification Orchestrator Service
                NotificationService notificationService = new NotificationService(clientRepo, notificationRepo,
                                templateRepo);

                // 3. Register Channel Senders (Strategy Pattern)
                notificationService.registerSender(new SMSSender());
                notificationService.registerSender(new EmailSender());
                notificationService.registerSender(new PushSender());
                notificationService.registerSender(new InAppSender());

                // 4. Attach Observer for Audit & Lifecycle Events
                notificationService.addNotificationEventListener(new AuditLoggingEventListener());

                // 5. Onboard Business Clients (Tenants)
                Client uber = new Client("client_uber", "Uber Technologies", "eng@uber.com", "Rides and Mobility");
                Client swiggy = new Client("client_swiggy", "Swiggy Foods", "alerts@swiggy.in",
                                "Food & Instant Commerce");

                clientRepo.save(uber);
                clientRepo.save(swiggy);
                System.out.println("Successfully onboarded clients: " + clientRepo.listAllClients().size() + "\n");

                // 6. Clients Register Their Message Templates
                System.out.println("--- Registering Client Templates ---");

                Template uberOtpSms = new Template(
                                "UBER_OTP_SMS",
                                uber.getClientId(),
                                "Your Uber verification code is {{otp}}. Valid for 5 minutes. Do not share.",
                                ChannelMedium.SMS,
                                TemplateType.OTP);
                notificationService.registerTemplate(uber.getClientId(), uberOtpSms);

                Template swiggyOrderEmail = new Template(
                                "SWIGGY_ORDER_EMAIL",
                                swiggy.getClientId(),
                                "Hello {{customerName}}, your order #{{orderId}} from '{{restaurant}}' is placed! Amount Paid: {{amount}}.",
                                ChannelMedium.EMAIL,
                                TemplateType.TRANSACTIONAL);
                notificationService.registerTemplate(swiggy.getClientId(), swiggyOrderEmail);

                Template swiggyFlashSalePush = new Template(
                                "SWIGGY_SALE_PUSH",
                                swiggy.getClientId(),
                                "Flash Sale {{discount}}% OFF! Order your favorite snack now with code {{coupon}}.",
                                ChannelMedium.PUSH,
                                TemplateType.FLASH_SALE);
                notificationService.registerTemplate(swiggy.getClientId(), swiggyFlashSalePush);

                System.out.println();

                // 7. Query Templates by Client and Channel
                System.out.println("--- Querying Templates for Swiggy (EMAIL) ---");
                List<Template> emailTemplates = notificationService.getTemplatesByClient(swiggy.getClientId(),
                                ChannelMedium.EMAIL);
                for (Template t : emailTemplates) {
                        System.out.println(
                                        "Found: " + t.getTemplateId() + " -> Raw: " + t.getContentWithPlaceholders());
                }
                System.out.println();

                // 8. End-Users (Recipients)
                Recipient alice = new Recipient("user_alice", "Alice Sharma", "alice@example.com", "+1-415-555-0199");
                Recipient bob = new Recipient("user_bob", "Bob Roy", "bob.roy@example.com", "+91-9876543210");
                Recipient charlie = new Recipient("user_charlie", "Charlie Patel", "charlie@example.com",
                                "+91-9123456780");

                // 9. Client sends Immediate Notifications
                System.out.println("--- Scenario 1: Uber triggers OTP SMS to Alice ---");
                Map<String, String> uberOtpParams = new HashMap<>();
                uberOtpParams.put("otp", "482910");
                String notifId1 = notificationService.sendNotification(
                                uber.getClientId(),
                                "UBER_OTP_SMS",
                                alice,
                                uberOtpParams);
                System.out.println("Dispatched Notification Tracking ID: " + notifId1 + "\n");

                System.out.println("--- Scenario 2: Swiggy triggers Order Confirmation Email to Bob ---");
                Map<String, String> swiggyEmailParams = new HashMap<>();
                swiggyEmailParams.put("customerName", bob.getRecipientName());
                swiggyEmailParams.put("orderId", "SWG-88231");
                swiggyEmailParams.put("restaurant", "Truffles Burger Joint");
                swiggyEmailParams.put("amount", "$24.50");

                String notifId2 = notificationService.sendNotification(
                                swiggy.getClientId(),
                                "SWIGGY_ORDER_EMAIL",
                                bob,
                                swiggyEmailParams);
                System.out.println("Dispatched Notification Tracking ID: " + notifId2 + "\n");

                // 10. Schedule and Cancel Notification
                System.out.println("--- Scenario 3: Swiggy schedules a Push Notification for Charlie ---");
                Map<String, String> pushParams = new HashMap<>();
                pushParams.put("discount", "40");
                pushParams.put("coupon", "YUMMY40");

                LocalDateTime scheduledTime = LocalDateTime.now().plusHours(3);
                String scheduledNotifId = notificationService.scheduleNotification(
                                swiggy.getClientId(),
                                "SWIGGY_SALE_PUSH",
                                charlie,
                                pushParams,
                                scheduledTime);
                System.out.println("Created Scheduled Notification Tracking ID: " + scheduledNotifId);

                System.out.println("\n--- Scenario 4: Swiggy Cancels the Scheduled Notification ---");
                notificationService.cancelNotifications(scheduledNotifId);
                Notification cancelledNotif = notificationRepo.findById(scheduledNotifId);
                System.out.println(
                                "Current Status of " + scheduledNotifId + " is: " + cancelledNotif.getStatus() + "\n");

                // 11. Edge Case: Invalid Client / Missing Template
                System.out.println("--- Scenario 5: Error Handling for Non-Existent Template ---");
                try {
                        notificationService.sendNotification(uber.getClientId(), "UNKNOWN_TMPL", alice, Map.of());
                } catch (IllegalArgumentException e) {
                        System.out.println("Caught expected exception: " + e.getMessage());
                }
        }
}
