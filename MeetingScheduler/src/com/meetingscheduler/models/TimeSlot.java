package com.meetingscheduler.models;

import java.time.LocalDateTime;

public class TimeSlot {
    private LocalDateTime start;
    private LocalDateTime end;

    public TimeSlot(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }

    public boolean overlaps(TimeSlot other) {
        return this.start.isBefore(other.getEnd()) && other.getStart().isBefore(this.end);
    }
}
