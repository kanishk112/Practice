package com.meetingscheduler.repositories;

import com.meetingscheduler.models.Room;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InMemoryRoomRepository implements RoomRepository {
    private Map<UUID, Room> rooms = new HashMap<>();

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(rooms.values());
    }

    @Override
    public Room findById(UUID id) {
        return rooms.get(id);
    }

    @Override
    public void save(Room room) {
        rooms.put(room.getId(), room);
    }
}
