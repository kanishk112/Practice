package com.meetingscheduler.observers;

import com.meetingscheduler.models.Meeting;

public interface MeetingEventListener {
    void onMeetingScheduled(Meeting m);
    void onMeetingCancelled(Meeting m);
}
