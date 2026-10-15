package com.meetingscheduler.models;

import java.util.UUID;

public class Room {
    private UUID id;
    private String name;
    private Integer capacity;
    private Status status;
    private Calendar calendar;

    public Room(UUID id, String name, Integer capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.status = Status.ACTIVE;
        this.calendar = new Calendar();
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public Integer getCapacity() { return capacity; }
    public Status getStatus() { return status; }
    public Calendar getCalendar() { return calendar; }
    
    public void setStatus(Status status) { this.status = status; }
}
