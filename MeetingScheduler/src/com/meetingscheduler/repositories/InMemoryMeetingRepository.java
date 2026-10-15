package com.meetingscheduler.repositories;

import com.meetingscheduler.models.Meeting;
import com.meetingscheduler.models.MeetingStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryMeetingRepository implements MeetingRepository {
    private Map<UUID, Meeting> meetings = new HashMap<>();

    @Override
    public void save(Meeting meeting) {
        meetings.put(meeting.getId(), meeting);
    }

    @Override
    public Meeting findById(UUID id) {
        return meetings.get(id);
    }

    @Override
    public void updateStatus(UUID id, MeetingStatus status) {
        Meeting meeting = meetings.get(id);
        if (meeting != null) {
            meeting.setStatus(status);
        }
    }
}
