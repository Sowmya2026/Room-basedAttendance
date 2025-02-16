package com.example.roombasedattendance;
public class HostelEvent {
    private String eventId;
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String eventVenue;
    private String eventDetails;

    // Empty constructor for Firebase
    public HostelEvent() {
    }

    // Constructor with parameters
    public HostelEvent(String eventId, String eventName, String eventDate, String eventTime, String eventVenue, String eventDetails) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventVenue = eventVenue;
        this.eventDetails = eventDetails;
    }

    // Getter and Setter methods
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }
}
