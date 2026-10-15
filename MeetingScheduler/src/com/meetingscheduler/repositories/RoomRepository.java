package com.meetingscheduler.repositories;

import com.meetingscheduler.models.Room;
import java.util.List;
import java.util.UUID;

public interface RoomRepository {
    List<Room> findAll();
    Room findById(UUID id);
    void save(Room room);
}
