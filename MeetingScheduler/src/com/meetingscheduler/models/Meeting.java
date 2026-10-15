package com.meetingscheduler.models;

import java.util.List;
import java.util.UUID;

public class Meeting {
    private UUID id;
    private String title;
    private User organiser;
    private Room room;
    private TimeSlot slot;
    private List<User> attendees;
    private MeetingStatus status;

    public Meeting(UUID id, String title, User organiser, Room room, TimeSlot slot, List<User> attendees) {
        this.id = id;
        this.title = title;
        this.organiser = organiser;
        this.room = room;
        this.slot = slot;
        this.attendees = attendees;
        this.status = MeetingStatus.SCHEDULED;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public User getOrganiser() { return organiser; }
    public Room getRoom() { return room; }
    public TimeSlot getSlot() { return slot; }
    public List<User> getAttendees() { return attendees; }
    public MeetingStatus getStatus() { return status; }

    public void setStatus(MeetingStatus status) { this.status = status; }
}
