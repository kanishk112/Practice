package com.meetingscheduler.strategies;

import com.meetingscheduler.models.Room;
import java.util.List;

public interface BookingStrategy {
    Room selectRoom(List<Room> availableRooms, int capacityRequired);
}
