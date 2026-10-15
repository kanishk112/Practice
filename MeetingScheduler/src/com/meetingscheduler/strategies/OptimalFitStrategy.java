package com.meetingscheduler.strategies;

import com.meetingscheduler.models.Room;
import java.util.List;
import java.util.Comparator;

public class OptimalFitStrategy implements BookingStrategy {
    @Override
    public Room selectRoom(List<Room> availableRooms, int capacityRequired) {
        return availableRooms.stream()
            .filter(room -> room.getCapacity() >= capacityRequired)
            .min(Comparator.comparingInt(Room::getCapacity))
            .orElse(null);
    }
}
