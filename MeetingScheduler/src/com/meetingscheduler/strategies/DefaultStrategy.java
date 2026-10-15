package com.meetingscheduler.strategies;

import com.meetingscheduler.models.Room;
import java.util.List;

public class DefaultStrategy implements BookingStrategy {
    @Override
    public Room selectRoom(List<Room> availableRooms, int capacityRequired) {
        for (Room room : availableRooms) {
            if (room.getCapacity() >= capacityRequired) {
                return room;
            }
        }
        return null;
    }
}
