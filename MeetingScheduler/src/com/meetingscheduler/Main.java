package com.meetingscheduler;

import com.meetingscheduler.models.*;
import com.meetingscheduler.repositories.*;
import com.meetingscheduler.services.MeetingRoomScheduler;
import com.meetingscheduler.strategies.OptimalFitStrategy;
import com.meetingscheduler.observers.Notification;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize Repositories (In-Memory for demonstration)
        RoomRepository roomRepo = new InMemoryRoomRepository();
        MeetingRepository meetingRepo = new InMemoryMeetingRepository();

        // 2. Add some Rooms to our Database
        Room boardroom = new Room(UUID.randomUUID(), "Boardroom A", 20);
        Room huddleRoom1 = new Room(UUID.randomUUID(), "Huddle Room 1", 4);
        Room huddleRoom2 = new Room(UUID.randomUUID(), "Huddle Room 2", 4);
        Room conferenceRoom = new Room(UUID.randomUUID(), "Conference Room", 10);
        
        roomRepo.save(boardroom);
        roomRepo.save(huddleRoom1);
        roomRepo.save(huddleRoom2);
        roomRepo.save(conferenceRoom);

        // 3. Initialize the Scheduler Service
        // We inject the OptimalFitStrategy so it always picks the smallest room that fits.
        MeetingRoomScheduler scheduler = new MeetingRoomScheduler(roomRepo, meetingRepo, new OptimalFitStrategy());
        
        // 4. Attach Observers (Notification Service)
        scheduler.addListener(new Notification());

        // 5. Create Users
        User alice = new User(UUID.randomUUID(), "Alice Organiser", "alice@company.com");
        User bob = new User(UUID.randomUUID(), "Bob Attendee", "bob@company.com");
        User charlie = new User(UUID.randomUUID(), "Charlie Attendee", "charlie@company.com");

        // --- DEMONSTRATION ---
        
        System.out.println("=== Booking a Meeting ===");
        TimeSlot slot1 = new TimeSlot(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        
        // Alice wants to book a meeting for 3 people. 
        // With OptimalFitStrategy, it should pick a Huddle Room (capacity 4), NOT the Boardroom (capacity 20).
        Meeting meeting1 = scheduler.bookMeeting(
            alice, 
            "Quarterly Sync", 
            slot1, 
            3, 
            Arrays.asList(bob, charlie)
        );
        
        System.out.println("\nSuccessfully booked! Room assigned: " + meeting1.getRoom().getName());
        System.out.println("Meeting ID: " + meeting1.getId());

        System.out.println("\n=== Booking another Meeting (Same Time) ===");
        try {
            // Trying to book another meeting for 8 people at the EXACT same time.
            // Huddle Room 1 is occupied. It should skip it and pick the Conference Room (capacity 10).
            Meeting meeting2 = scheduler.bookMeeting(
                bob, 
                "Design Review", 
                slot1, 
                8, 
                Arrays.asList(alice)
            );
            System.out.println("\nSuccessfully booked! Room assigned: " + meeting2.getRoom().getName());
        } catch (Exception e) {
            System.out.println("Failed to book: " + e.getMessage());
        }

        System.out.println("\n=== Cancelling a Meeting ===");
        scheduler.cancelMeeting(meeting1.getId());
        System.out.println("\nMeeting " + meeting1.getId() + " status is now: " + meeting1.getStatus());
    }
}
