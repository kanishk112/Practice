package com.meetingscheduler.services;

import com.meetingscheduler.models.*;
import com.meetingscheduler.repositories.MeetingRepository;
import com.meetingscheduler.repositories.RoomRepository;
import com.meetingscheduler.strategies.BookingStrategy;
import com.meetingscheduler.observers.MeetingEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MeetingRoomScheduler {
    private RoomRepository rooms;
    private MeetingRepository meetings;
    private List<MeetingEventListener> listeners;
    private BookingStrategy strategy;

    public MeetingRoomScheduler(RoomRepository rooms, MeetingRepository meetings, BookingStrategy strategy) {
        this.rooms = rooms;
        this.meetings = meetings;
        this.strategy = strategy;
        this.listeners = new ArrayList<>();
    }

    public void addListener(MeetingEventListener listener) {
        this.listeners.add(listener);
    }

    public List<Room> searchAvailableRooms(TimeSlot slot, int requiredCapacity) {
        List<Room> allRooms = rooms.findAll();
        return allRooms.stream()
                .filter(room -> room.getStatus() == Status.ACTIVE)
                .filter(room -> room.getCapacity() >= requiredCapacity)
                .filter(room -> room.getCalendar().isAvailable(slot))
                .collect(Collectors.toList());
    }

    public Meeting bookMeeting(User organiser, String title, TimeSlot slot, int capacity, List<User> attendees) {
        List<Room> availableRooms = searchAvailableRooms(slot, capacity);
        Room selectedRoom = strategy.selectRoom(availableRooms, capacity);
        
        if (selectedRoom == null) {
            throw new RuntimeException("No available room found for the given capacity and time.");
        }

        Meeting meeting = new Meeting(UUID.randomUUID(), title, organiser, selectedRoom, slot, attendees);
        selectedRoom.getCalendar().addMeeting(meeting);
        
        meetings.save(meeting);
        rooms.save(selectedRoom);
        
        for (MeetingEventListener listener : listeners) {
            listener.onMeetingScheduled(meeting);
        }
        
        return meeting;
    }

    public void cancelMeeting(UUID meetingId) {
        Meeting meeting = meetings.findById(meetingId);
        if (meeting == null) {
            throw new RuntimeException("Meeting not found.");
        }

        meeting.setStatus(MeetingStatus.CANCELLED);
        meetings.updateStatus(meetingId, MeetingStatus.CANCELLED);
        
        Room room = meeting.getRoom();
        room.getCalendar().removeMeeting(meeting);
        rooms.save(room);

        for (MeetingEventListener listener : listeners) {
            listener.onMeetingCancelled(meeting);
        }
    }
}
