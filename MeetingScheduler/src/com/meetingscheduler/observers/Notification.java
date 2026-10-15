package com.meetingscheduler.observers;

import com.meetingscheduler.models.Meeting;
import com.meetingscheduler.models.User;

public class Notification implements MeetingEventListener {
    
    @Override
    public void onMeetingScheduled(Meeting m) {
        sendEmail(m.getOrganiser(), "Meeting scheduled: " + m.getTitle());
        for (User attendee : m.getAttendees()) {
            sendEmail(attendee, "You are invited to: " + m.getTitle());
        }
    }

    @Override
    public void onMeetingCancelled(Meeting m) {
        sendEmail(m.getOrganiser(), "Meeting cancelled: " + m.getTitle());
        for (User attendee : m.getAttendees()) {
            sendEmail(attendee, "Meeting cancelled: " + m.getTitle());
        }
    }

    private void sendEmail(User u, String message) {
        // In reality, this would integrate with an SMTP server or email API
        System.out.println("Sending email to " + u.getEmail() + ": " + message);
    }
}
