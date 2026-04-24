package com.example.eventorganizerapp;

public class Event {
    private final long id;
    private final String event;

    public Event(long id, String event) {
        this.id = id;
        this.event = event;
    }

    public long getId() { return id; }
    public String getEvent() { return event; }
}
