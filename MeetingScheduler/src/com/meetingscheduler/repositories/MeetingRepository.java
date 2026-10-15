package com.meetingscheduler.repositories;

import com.meetingscheduler.models.Meeting;
import com.meetingscheduler.models.MeetingStatus;
import java.util.UUID;

public interface MeetingRepository {
    void save(Meeting meeting);
    Meeting findById(UUID id);
    void updateStatus(UUID id, MeetingStatus status);
}
