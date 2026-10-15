package com.meetingscheduler.models;

import java.util.ArrayList;
import java.util.List;

public class Calendar {
    private List<Meeting> meetings; // IntervalTree in an optimized version

    public Calendar() {
        this.meetings = new ArrayList<>();
    }

    public boolean isAvailable(TimeSlot t) {
        for (Meeting m : meetings) {
            if (m.getSlot().overlaps(t) && m.getStatus() != MeetingStatus.CANCELLED) {
                return false;
            }
        }
        return true;
    }

    public boolean areAllAvailable(List<TimeSlot> slots) {
        for (TimeSlot slot : slots) {
            if (!isAvailable(slot)) {
                return false;
            }
        }
        return true;
    }

    public void addMeeting(Meeting m) {
        meetings.add(m);
    }

    public void bulkAddMeetings(List<Meeting> newMeetings) {
        meetings.addAll(newMeetings);
    }

    public void removeMeeting(Meeting m) {
        meetings.remove(m);
    }

    public void bulkRemoveMeetings(List<Meeting> meetingsToRemove) {
        meetings.removeAll(meetingsToRemove);
    }
}
